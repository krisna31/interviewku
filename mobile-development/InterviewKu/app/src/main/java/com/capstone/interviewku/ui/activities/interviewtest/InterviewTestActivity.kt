package com.capstone.interviewku.ui.activities.interviewtest

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
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
    private lateinit var jobPickerFragment: JobPickerFragment

    private var audioFile: File? = null
    private var audioFilename: String? = null
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
                                    language = Locale.forLanguageTag("id-ID")
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
                            } else {
                                Toast.makeText(
                                    this@InterviewTestActivity,
                                    getString(R.string.tts_init_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        Constants.GOOGLE_TTS_PACKAGE_NAME
                    )
                } else {
                    // no indonesia data
                    Toast.makeText(
                        this@InterviewTestActivity,
                        getString(R.string.no_indonesian_tts_data),
                        Toast.LENGTH_SHORT
                    ).show()
                    installTTSData()
                }
            } else {
                // no data at all
                Toast.makeText(
                    this@InterviewTestActivity,
                    getString(R.string.no_tts_data),
                    Toast.LENGTH_SHORT
                ).show()
                installTTSData()
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

        if (!isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            initializeAll()
        }
    }

    override fun onStop() {
        textToSpeech?.stop()
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        onExitDialog()
        return super.onSupportNavigateUp()
    }

    private fun initializeAll() {
        initializeJobFieldPicker()
        initializeTTS()
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

        viewModel.prepareInterview()
    }

    private fun initializeJobFieldPicker() {
        jobPickerFragment = JobPickerFragment { jobFieldId ->
            InterviewInstructionFragment(InterviewInstructionFragment.TYPE_TEST) {
                viewModel.setJobFieldId(jobFieldId)
                viewModel.startInterviewSession()
            }.show(supportFragmentManager, null)
        }
        jobPickerFragment.show(supportFragmentManager, null)
    }

    private fun initializeTTS() {
        try {
            checkTTSDataLauncher.launch(
                Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA).apply {
                    setPackage(Constants.GOOGLE_TTS_PACKAGE_NAME)
                }
            )
        } catch (e: Exception) {
            if (e is ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    getString(R.string.no_google_tts),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun installTTSData() {
        try {
            startActivity(Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA).apply {
                setPackage(Constants.GOOGLE_TTS_PACKAGE_NAME)
            })
        } catch (e: Exception) {
            if (e is ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    getString(R.string.no_google_tts),
                    Toast.LENGTH_SHORT
                ).show()
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
                    it.exception.getData()?.handleHttpException(this)

                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.prepareInterview()
                        }
                        .setNegativeButton(getString(R.string.exit)) { _, _ ->
                            finish()
                        }
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.prepare_interview_failed))
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
                    it.exception.getData()?.handleHttpException(this)

                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.startInterviewSession()
                        }
                        .setNegativeButton(getString(R.string.exit)) { _, _ ->
                            finish()
                        }
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.start_interview_failed))
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
                    it.exception.getData()?.handleHttpException(this)

                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.sendAnswer()
                        }
                        .setNegativeButton(getString(R.string.exit)) { _, _ ->
                            finish()
                        }
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.submit_answer_failed))
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
                                putExtra(InterviewResultActivity.INTERVIEW_ID_KEY, data.interviewId)
                            }
                        )
                    }
                    finish()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    it.exception.getData()?.handleHttpException(this)

                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.endInterviewSession()
                        }
                        .setNegativeButton(getString(R.string.exit)) { _, _ ->
                            finish()
                        }
                        .setTitle(getString(R.string.error_title))
                        .setMessage(getString(R.string.end_interview_failed))
                        .create()

                    if (!isFinishing) {
                        alertDialog.show()
                    }
                }
            }
        }

        viewModel.currentQuestion.observe(this) {
            lifecycleScope.launch {
                delay(1000)
                speakTTS(it)
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
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                finish()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .setTitle(getString(R.string.exit_from_session))
            .setMessage(getString(R.string.interview_exit_confirm))
            .create()

        if (!isFinishing) {
            alertDialog.show()
        }
    }

    private fun showMicrophonePermissionDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            .setNegativeButton(getString(R.string.exit)) { _, _ ->
                finish()
            }
            .setTitle(getString(R.string.permission_title))
            .setMessage(getString(R.string.mic_permission_prompt))
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
        ) ?: run {
            Toast.makeText(
                this@InterviewTestActivity,
                getString(R.string.tts_not_initialized),
                Toast.LENGTH_SHORT
            ).show()

            initializeTTS()
        }
    }

    private fun startRecording() {
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

                audioFilename = System.currentTimeMillis().toString().also { filename ->
                    audioFile =
                        File.createTempFile(filename, Constants.AUDIO_FORMAT_SUFFIX, cacheDir)
                            .also {
                                setOutputFile(
                                    it.outputStream().fd
                                )
                            }
                }

                prepare()
                start()
            }

            viewModel.startRecording()
        } catch (e: Exception) {
            audioFile = null
            audioFilename = null

            Toast.makeText(
                this,
                getString(R.string.record_failed),
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }

            audioFile?.let {
                viewModel.setAnswer(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaRecorder = null
            viewModel.stopRecording()
        }
    }
}