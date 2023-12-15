package com.capstone.interviewku.ui.fragments.jobpicker

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.CustomAlertJobBinding
import com.capstone.interviewku.util.JobFieldModel
import com.capstone.interviewku.util.SpinnerModel

class JobPickerFragment(
    private val onJobSelected: (Int) -> Unit,
) : DialogFragment() {
    private var _binding: CustomAlertJobBinding? = null
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
        _binding = CustomAlertJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.isVisible = true
        binding.btnNext.isEnabled = false
        binding.btnNext.setOnClickListener {
            val jobFieldId = (binding.spinnerJobField.selectedItem as SpinnerModel?)
                ?.value
                ?.toIntOrNull()

            if (jobFieldId != null && jobFieldId != -1) {
                onJobSelected(jobFieldId)
                dismiss()
            }
        }

        binding.spinnerJobField.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.btnNext.isEnabled = position != 0
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.spinnerJobField.isEnabled = false

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

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    fun setData(jobFieldModel: JobFieldModel) {
        val spinnerData = mutableListOf<SpinnerModel>()
        spinnerData.addAll(
            jobFieldModel.jobFields.sortedBy {
                it.name
            }.map { jobField ->
                SpinnerModel(jobField.id.toString(), jobField.name)
            }
        )
        spinnerData.add(0, SpinnerModel("-1", getString(R.string.please_choose)))

        val selectedIndex = jobFieldModel.jobFields.indexOfFirst {
            it.id == jobFieldModel.selectedId
        }

        binding.progressBar.isVisible = false
        binding.spinnerJobField.apply {
            isEnabled = true
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                spinnerData
            )
            setSelection(selectedIndex + 1)
        }
    }
}