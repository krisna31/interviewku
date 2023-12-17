@file:Suppress("SameParameterValue")

package com.capstone.interviewku.ui.activities.interviewtest

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityInterviewTestBinding
import com.capstone.interviewku.ui.activities.interviewresult.InterviewResultActivity
import com.capstone.interviewku.ui.fragments.interviewinstruction.InterviewInstructionFragment
import com.capstone.interviewku.ui.fragments.jobpicker.JobPickerFragment
import com.capstone.interviewku.util.Constants
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Extensions.isPermissionGranted
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

@AndroidEntryPoint
class InterviewTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterviewTestBinding
    private lateinit var interviewInstructionFragment: InterviewInstructionFragment
    private lateinit var jobPickerFragment: JobPickerFragment
    private lateinit var mediaPlayer: MediaPlayer

    private var audioFile: File? = null
    private var isTestRecording = false
    private var mediaRecorder: MediaRecorder? = null
    private var textToSpeech: TextToSpeech? = null

    private val viewModel by viewModels<InterviewTestViewModel>()

    private val checkTTSDataLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                if (activityResult
                        .data
                        ?.getStringArrayListExtra(TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES)
                        ?.firstOrNull { it.contains(Constants.TTS_EXTRA_VOICE_INDONESIA) } != null
                ) {
                    textToSpeech = TextToSpeech(
                        this,
                        { initCode ->
                            if (initCode == TextToSpeech.SUCCESS) {
                                textToSpeech?.apply {
                                    language = Locale.forLanguageTag(
                                        Constants.INDONESIA_LANGUAGE_TAG
                                    )
                                    setOnUtteranceProgressListener(object :
                                        UtteranceProgressListener() {
                                        override fun onStart(utteranceId: String?) {
                                            runOnUiThread {
                                                binding.lottieSpeakingAnim.apply {
                                                    isVisible = true
                                                    playAnimation()
                                                }
                                            }
                                        }

                                        override fun onDone(utteranceId: String?) {
                                            runOnUiThread {
                                                binding.lottieSpeakingAnim.apply {
                                                    isVisible = false
                                                    cancelAnimation()
                                                }
                                                startRecording()
                                            }
                                        }

                                        @Deprecated("Deprecated in Java")
                                        override fun onError(utteranceId: String?) {
                                            runOnUiThread {
                                                binding.lottieSpeakingAnim.apply {
                                                    isVisible = false
                                                    cancelAnimation()
                                                }
                                            }
                                        }
                                    })
                                }
                                interviewInstructionFragment.setTTSStatus(
                                    getString(R.string.ready)
                                )
                                interviewInstructionFragment.setIsTTSReady(true)
                            } else {
                                interviewInstructionFragment.setTTSStatus(
                                    getString(R.string.tts_init_failed)
                                )
                                val alertDialog = AlertDialog.Builder(this)
                                    .setCancelable(false)
                                    .setTitle(getString(R.string.error_title))
                                    .setMessage(getString(R.string.tts_init_failed))
                                    .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                                        initializeTTS()
                                    }
                                    .setNegativeButton(getString(R.string.exit)) { _, _ ->
                                        finish()
                                    }
                                    .create()

                                if (!isFinishing) {
                                    alertDialog.show()
                                }
                            }
                        },
                        Constants.GOOGLE_TTS_PACKAGE_NAME
                    )
                } else {
                    // no indonesia data
                    interviewInstructionFragment.setTTSStatus(
                        getString(R.string.no_indonesian_tts_data)
                    )
                    showInstallTTSDataDialog()
                }
            } else {
                // no data at all
                interviewInstructionFragment.setTTSStatus(
                    getString(R.string.no_tts_data)
                )
                showInstallTTSDataDialog()
            }
        }
    private val microphonePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(
                    this,
                    getString(R.string.mic_permission_granted),
                    Toast.LENGTH_SHORT
                ).show()
                initializeAll()
            } else {
                showMicrophonePermissionDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterviewTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.interview_test)
        }

        onBackPressedDispatcher.addCallback {
            onExitDialog()
        }

        interviewInstructionFragment = InterviewInstructionFragment(
            dialogType = InterviewInstructionFragment.TYPE_TEST,
            onViewCreated = {
                initializeTTS()
            },
            onContinueClick = {
                if (isTestRecording) {
                    stopRecording(true)
                }
                textToSpeech?.stop()
                viewModel.startInterviewSession()
            },
            onTTSReinitClick = {
                initializeTTS()
            },
            onTTSCheckClick = {
                textToSpeech?.speak(
                    getString(R.string.tts_sample_sound),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    ""
                )
            },
            onMicCheckClick = {
                if (isTestRecording) {
                    stopRecording(true)
                    playRecording()
                } else {
                    startRecording(true)
                }
            },
        )
        jobPickerFragment = JobPickerFragment(
            onJobSelected = { jobFieldId ->
                viewModel.setJobFieldId(jobFieldId)
                interviewInstructionFragment.show(supportFragmentManager, null)
            },
        )

        if (!isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            initializeAll()
        }
    }

    override fun onStop() {
        textToSpeech?.stop()
        mediaPlayer.stop()
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        onExitDialog()
        return super.onSupportNavigateUp()
    }

    private fun initializeAll() {
        initializeMediaPlayer()
        observeViewmodelData()

        binding.ivNext.setOnClickListener {
            stopRecording()
            viewModel.sendAnswer()
        }

        binding.ivRepeatQuestion.setOnClickListener {
            stopRecording()
            viewModel.repeatQuestion()
            viewModel.currentQuestion.value?.let { question ->
                speakTTS(question)
            }
        }

        jobPickerFragment.show(supportFragmentManager, null)
        viewModel.prepareInterview()
    }

    private fun initializeMediaPlayer() {
        mediaPlayer = if (Build.VERSION.SDK_INT >= 34) {
            MediaPlayer(this)
        } else {
            MediaPlayer()
        }

        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
    }

    private fun initializeTTS() {
        try {
            interviewInstructionFragment.setTTSStatus(getString(R.string.tts_not_initialized))
            interviewInstructionFragment.setIsTTSReady(false)
            checkTTSDataLauncher.launch(
                Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA).apply {
                    setPackage(Constants.GOOGLE_TTS_PACKAGE_NAME)
                }
            )
        } catch (e: Exception) {
            if (e is ActivityNotFoundException) {
                showTTSDownloadDialog()
            }
        }
    }

    private fun observeViewmodelData() {
        viewModel.prepareInterviewState.observe(this) {
            when (it) {
                is Result.Success -> {
                    jobPickerFragment.setData(it.data)
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(
                            getString(
                                R.string.prepare_interview_failed,
                                it.exception.peekData().handleHttpException(this)
                            )
                        )
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.prepareInterview()
                        }
                        .setNegativeButton(getString(R.string.exit)) { _, _ ->
                            finish()
                        }
                        .create()

                    if (!isFinishing) {
                        alertDialog.show()
                    }
                }
            }
        }

        viewModel.startInterviewState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading

            when (it) {
                is Result.Success -> {
                    viewModel.moveToNextQuestion()
                    binding.tvInterviewTestInstruction.isVisible = true
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(
                            getString(
                                R.string.start_interview_failed,
                                it.exception.peekData().handleHttpException(this)
                            )
                        )
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.startInterviewSession()
                        }
                        .setNegativeButton(getString(R.string.exit)) { _, _ ->
                            finish()
                        }
                        .create()

                    if (!isFinishing) {
                        alertDialog.show()
                    }
                }
            }
        }

        viewModel.submitInterviewState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading

            when (it) {
                is Result.Success -> {
                    if (viewModel.isEndOfInterview) {
                        binding.tvInterviewTestInstruction.isVisible = false
                        binding.ivNext.isVisible = false
                        binding.ivRepeatQuestion.isVisible = false

                        viewModel.endInterviewSession()
                    } else {
                        viewModel.moveToNextQuestion()
                    }
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(
                            getString(
                                R.string.submit_answer_failed,
                                it.exception.peekData().handleHttpException(this)
                            )
                        )
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.sendAnswer()
                        }
                        .setNegativeButton(getString(R.string.exit)) { _, _ ->
                            finish()
                        }
                        .create()

                    if (!isFinishing) {
                        alertDialog.show()
                    }
                }
            }
        }

        viewModel.endInterviewState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading

            when (it) {
                is Result.Success -> {
                    Toast.makeText(this, getString(R.string.interview_finished), Toast.LENGTH_SHORT)
                        .show()
                    it.data.data?.let { data ->
                        startActivity(
                            Intent(this, InterviewResultActivity::class.java).apply {
                                putExtra(
                                    InterviewResultActivity.EXTRA_INTERVIEW_ID_KEY,
                                    data.interviewId
                                )
                            }
                        )
                    }
                    finish()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(
                            getString(
                                R.string.end_interview_failed,
                                it.exception.peekData().handleHttpException(this)
                            )
                        )
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.endInterviewSession()
                        }
                        .setNegativeButton(getString(R.string.exit)) { _, _ ->
                            finish()
                        }
                        .create()

                    if (!isFinishing) {
                        alertDialog.show()
                    }
                }
            }
        }

        viewModel.currentDuration.observe(this) {
            if (it >= Constants.INTERVIEW_MAX_DURATION_SECONDS) {
                stopRecording()
                viewModel.sendAnswer()
            }
        }

        viewModel.currentQuestion.observe(this) {
            lifecycleScope.launch {
                if (it.isNotEmpty()) {
                    delay(1000)
                    speakTTS(it)
                }
            }
        }

        viewModel.isRecording.observe(this) { isRecording ->
            binding.ivNext.isVisible = isRecording
            binding.ivRecording.isVisible = isRecording
            binding.ivRepeatQuestion.isVisible = isRecording
        }
    }

    private fun onExitDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.exit_from_session))
            .setMessage(getString(R.string.interview_exit_confirm))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                finish()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        if (!isFinishing) {
            alertDialog.show()
        }
    }

    private fun playRecording() {
        audioFile?.let {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(it.inputStream().fd)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }

    private fun showInstallTTSDataDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.download_tts_data_title))
            .setMessage(getString(R.string.download_tts_data_explanation))
            .setPositiveButton(getString(R.string.download)) { _, _ ->
                try {
                    startActivity(Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA).apply {
                        setPackage(Constants.GOOGLE_TTS_PACKAGE_NAME)
                    })
                } catch (e: Exception) {
                    if (e is ActivityNotFoundException) {
                        showTTSDownloadDialog()
                    }
                }
            }
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                finish()
            }
            .create()

        if (!isFinishing) {
            alertDialog.show()
        }
    }

    private fun showMicrophonePermissionDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.permission_title))
            .setMessage(getString(R.string.mic_permission_prompt))
            .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                finish()
            }
            .create()

        if (!isFinishing) {
            alertDialog.show()
        }
    }

    private fun showTTSDownloadDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle(getString(R.string.error_title))
            .setMessage(getString(R.string.no_google_tts))
            .setNegativeButton(getString(R.string.exit)) { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                finish()
            }
            .create()

        if (!isFinishing) {
            alertDialog.show()
        }
    }

    private fun speakTTS(text: String) {
        textToSpeech?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            ""
        )
    }

    private fun startRecording(isTesting: Boolean = false) {
        try {
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(this)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }.apply {
                setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
                setOutputFormat(MediaRecorder.OutputFormat.AMR_WB)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
                setAudioSamplingRate(Constants.AUDIO_SAMPLE_RATE)

                audioFile = File.createTempFile(
                    System.currentTimeMillis().toString(),
                    Constants.AUDIO_FORMAT_SUFFIX,
                    cacheDir
                ).also {
                    setOutputFile(
                        it.outputStream().fd
                    )
                }

                prepare()
                start()
            }

            if (!isTesting) {
                viewModel.startRecording()
            } else {
                isTestRecording = true
            }
        } catch (e: Exception) {
            audioFile = null
            isTestRecording = false

            Toast.makeText(
                this,
                getString(R.string.record_failed),
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        } finally {
            if (isTesting) {
                interviewInstructionFragment.setIsRecording(isTestRecording)
            }
        }
    }

    private fun stopRecording(isTesting: Boolean = false) {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }

            audioFile?.let {
                if (!isTesting) {
                    viewModel.setAnswer(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaRecorder = null

            if (isTesting) {
                isTestRecording = false
                interviewInstructionFragment.setIsRecording(false)
            } else {
                viewModel.stopRecording()
            }
        }
    }
}