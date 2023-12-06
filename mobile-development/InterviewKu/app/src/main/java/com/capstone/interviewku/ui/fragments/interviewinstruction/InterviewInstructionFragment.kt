package com.capstone.interviewku.ui.fragments.interviewinstruction

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
    private val onButtonClick: () -> Unit,
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

        binding.btnContinue.setOnClickListener {
            onButtonClick()
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
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        const val TYPE_TRAIN = "TYPE_TRAIN"
        const val TYPE_TEST = "TYPE_TEST"
    }
}