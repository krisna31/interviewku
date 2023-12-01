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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityInterviewTestBinding
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
                if (activityResult.data?.getStringArrayListExtra(TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES)
                        ?.firstOrNull { it.contains("ind-idn") } != null
                ) {
                    textToSpeech = TextToSpeech(
                        this,
                        { initCode ->
                            if (initCode == TextToSpeech.SUCCESS) {
                                textToSpeech?.apply {
                                    language = Locale.forLanguageTag("id-ID")
                                    setOnUtteranceProgressListener(object :
                                        UtteranceProgressListener() {
                                        override fun onStart(utteranceId: String?) {}

                                        override fun onDone(utteranceId: String?) {
                                            runOnUiThread {
                                                startRecording()
                                            }
                                        }

                                        override fun onError(utteranceId: String?) {}
                                    })
                                }
                            } else {
                                Toast.makeText(
                                    this@InterviewTestActivity,
                                    "Silahkan mengecek fitur Google TTS di perangkat anda",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        "com.google.android.tts"
                    )
                } else {
                    // no indonesia data
                    Toast.makeText(
                        this@InterviewTestActivity,
                        "Anda belum memasang data suara Bahasa Indonesia. Silahkan memasang data suara Bahasa Indonesia",
                        Toast.LENGTH_SHORT
                    ).show()

                    try {
                        startActivity(Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA).apply {
                            setPackage("com.google.android.tts")
                        })
                    } catch (e: Exception) {
                        if (e is ActivityNotFoundException) {
                            Toast.makeText(
                                this,
                                "Silahkan mengunduh Google TTS di Play Store",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                // no data at all
                Toast.makeText(
                    this@InterviewTestActivity,
                    "Anda tidak memiliki data suara. Silahkan memasang data suara Bahasa Indonesia",
                    Toast.LENGTH_SHORT
                ).show()

                try {
                    startActivity(Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA).apply {
                        setPackage("com.google.android.tts")
                    })
                } catch (e: Exception) {
                    if (e is ActivityNotFoundException) {
                        Toast.makeText(
                            this,
                            "Silahkan mengunduh Google TTS di Play Store",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    private val microphonePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(
                this,
                if (it) {
                    "Anda dapat merekam suara anda"
                } else {
                    "Izin merekam suara harus diperbolehkan agar fitur perekaman dapat digunakan"
                },
                Toast.LENGTH_SHORT
            ).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterviewTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        initializeJobFieldPicker()
        initializeTTS()
        observeViewmodelData()

        binding.civNext.setOnClickListener {
            stopRecording()
            viewModel.sendAnswer()
        }

        binding.ivRepeatQuestion.setOnClickListener {
            stopRecording()
            viewModel.repeatQuestion()
            viewModel.currentQuestion.value?.let { question ->
                textToSpeech?.speak(
                    question,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    ""
                ) ?: run {
                    Toast.makeText(
                        this@InterviewTestActivity,
                        "TTS belum diinisialisasi",
                        Toast.LENGTH_SHORT
                    ).show()

                    initializeTTS()
                }
            }
        }

        viewModel.prepareInterview()
    }

    private fun initializeJobFieldPicker() {
        jobPickerFragment = JobPickerFragment { jobFieldId ->
            InterviewInstructionFragment(InterviewInstructionFragment.TYPE_TRAIN) {
                viewModel.startInterviewSession(jobFieldId)
            }.show(supportFragmentManager, null)
        }
        jobPickerFragment.show(supportFragmentManager, null)
    }

    private fun initializeTTS() {
        try {
            Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA).apply {
                setPackage("com.google.android.tts")
                checkTTSDataLauncher.launch(this)
            }
        } catch (e: Exception) {
            if (e is ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    "Silahkan mengunduh Google TTS di Play Store",
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

                    // alertdialog ulang
                }
            }
        }

        viewModel.startInterviewState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading

            if (it is Result.Error) {
                it.exception.getData()?.handleHttpException(this)
                // alertdialog ulang
            }
        }

        viewModel.submitInterviewState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading
            binding.civNext.isClickable = it is Result.Success
            binding.ivRepeatQuestion.isClickable = it is Result.Success

            when (it) {
                is Result.Success -> {
                    if (viewModel.isEndOfInterview) {
                        binding.civNext.isVisible = false
                        binding.ivRepeatQuestion.isVisible = false
                        viewModel.endInterviewSession()
                    }
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    it.exception.getData()?.handleHttpException(this)
                    // alertdialog ulang
                }
            }
        }

        viewModel.endInterviewState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading

            when (it) {
                is Result.Success -> {
                    // to result activity
                    Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    it.exception.getData()?.handleHttpException(this)
                    // alertdialog ulang
                }
            }
        }

        viewModel.currentQuestion.observe(this) {
            lifecycleScope.launch {
                delay(1000)

                textToSpeech?.speak(
                    it,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    ""
                ) ?: run {
                    Toast.makeText(
                        this@InterviewTestActivity,
                        "TTS belum diinisialisasi",
                        Toast.LENGTH_SHORT
                    ).show()

                    initializeTTS()
                }
            }
        }

        viewModel.isRecording.observe(this) { isRecording ->
            binding.civNext.isVisible = isRecording
            binding.ivRepeatQuestion.isVisible = isRecording
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