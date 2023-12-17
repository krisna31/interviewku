package com.capstone.interviewku.ui.fragments.interviewinstruction

import android.app.ActionBar
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.CustomAlertInstructionBinding

class InterviewInstructionFragment(
    private val dialogType: String,
    private val onViewCreated: () -> Unit,
    private val onContinueClick: () -> Unit,
    private val onTTSReinitClick: () -> Unit,
    private val onTTSCheckClick: () -> Unit,
    private val onMicCheckClick: () -> Unit,
) : DialogFragment() {
    private var _binding: CustomAlertInstructionBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomAlertInstructionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()

        binding.tvInstructions.text = context.resources.getString(
            when (dialogType) {
                TYPE_TRAIN -> {
                    R.string.train_instruction
                }

                TYPE_TEST -> {
                    R.string.test_instruction
                }

                else -> {
                    R.string.train_instruction
                }
            }
        )

        binding.btnContinue.text = context.resources.getString(
            when (dialogType) {
                TYPE_TRAIN -> {
                    R.string.start_train
                }

                TYPE_TEST -> {
                    R.string.start_test
                }

                else -> {
                    R.string.start_train
                }
            }
        )

        binding.btnTtsReinit.setOnClickListener {
            onTTSReinitClick()
        }

        binding.btnTtsCheck.setOnClickListener {
            onTTSCheckClick()
        }

        binding.btnMicRecord.setOnClickListener {
            onMicCheckClick()
        }

        binding.btnContinue.setOnClickListener {
            onContinueClick()
            dismiss()
        }

        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (event.action == KeyEvent.ACTION_UP) {
                    dismiss()
                    requireActivity().finish()
                    return@setOnKeyListener true
                }
            }

            false
        }

        onViewCreated()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    fun setIsRecording(isRecording: Boolean) {
        if (isRecording) {
            binding.btnMicRecord.text = getString(R.string.check_mic_stop)
        } else {
            binding.btnMicRecord.text = getString(R.string.check_mic_start)
        }
    }

    fun setIsTTSReady(isReady: Boolean) {
        binding.btnContinue.isEnabled = isReady
    }

    fun setTTSStatus(status: String) {
        binding.tvTtsStatus.text = getString(R.string.tts_status_template, status)
    }

    companion object {
        const val TYPE_TRAIN = "TYPE_TRAIN"
        const val TYPE_TEST = "TYPE_TEST"
    }
}