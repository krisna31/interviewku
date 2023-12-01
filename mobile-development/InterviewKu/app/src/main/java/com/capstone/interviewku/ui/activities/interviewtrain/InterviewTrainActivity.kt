package com.capstone.interviewku.ui.activities.interviewtrain

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityInterviewTrainBinding
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
class InterviewTrainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterviewTrainBinding
    private lateinit var jobPickerFragment: JobPickerFragment

    private var audioFile: File? = null
    private var audioFilename: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var textToSpeech: TextToSpeech? = null
    private val viewModel by viewModels<InterviewTrainViewModel>()

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
                                }
                            } else {
                                Toast.makeText(
                                    this@InterviewTrainActivity,
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
                        this@InterviewTrainActivity,
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
                    this@InterviewTrainActivity,
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

        binding = ActivityInterviewTrainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
            microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        initializeJobFieldPicker()
        initializeTTS()
        observeViewmodelData()

        binding.civNext.setOnClickListener {
            viewModel.sendAnswer()
        }

        binding.ivRepeatAnswer.setOnClickListener {
            viewModel.setAnswer(null)
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

            when (it) {
                is Result.Success -> {
                    viewModel.moveToNextQuestion()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    it.exception.getData()?.handleHttpException(this)
                    // alertdialog ulang
                }
            }
        }

        viewModel.submitInterviewState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading
            binding.civNext.isClickable = it is Result.Success
            binding.ivRepeatAnswer.isClickable = it is Result.Success

            when (it) {
                is Result.Success -> {
                    if (viewModel.isEndOfInterview) {
                        binding.civNext.isVisible = false
                        binding.ivRepeatAnswer.isVisible = false
                        viewModel.endInterviewSession()
                    } else {
                        viewModel.moveToNextQuestion()
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

        viewModel.currentDuration.observe(this) {
            binding.tvTimer.text = it
        }

        viewModel.currentQuestion.observe(this) {
            lifecycleScope.launch {
                binding.tvQuestion.text = it

                delay(1000)

                textToSpeech?.speak(
                    it,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    ""
                ) ?: run {
                    Toast.makeText(
                        this@InterviewTrainActivity,
                        "TTS belum diinisialisasi",
                        Toast.LENGTH_SHORT
                    ).show()

                    initializeTTS()
                }
            }
        }

        viewModel.currentQuestionOrder.observe(this) {
            binding.tvQuestionOrder.text = getString(R.string.question_number, it)
        }

        viewModel.currentQuestionIsAnswered.observe(this) { isAnswered ->
            binding.civNext.isVisible = isAnswered
            binding.ivRepeatAnswer.isVisible = isAnswered

            binding.civRecord.isVisible = !isAnswered
        }

        viewModel.isRecording.observe(this) { isRecording ->
            binding.civRecord.apply {
                setImageDrawable(
                    if (isRecording) {
                        AppCompatResources.getDrawable(
                            this@InterviewTrainActivity,
                            R.drawable.baseline_mic_24
                        )
                    } else {
                        AppCompatResources.getDrawable(
                            this@InterviewTrainActivity,
                            R.drawable.baseline_mic_off_24
                        )
                    }
                )

                setOnClickListener {
                    if (isRecording) {
                        stopRecording()
                    } else {
                        startRecording()
                    }
                }
            }
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