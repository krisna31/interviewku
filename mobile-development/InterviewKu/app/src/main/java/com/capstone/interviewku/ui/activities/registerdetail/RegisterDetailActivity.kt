package com.capstone.interviewku.ui.activities.registerdetail

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityRegisterDetailBinding
import com.capstone.interviewku.ui.activities.main.MainActivity
import com.capstone.interviewku.ui.fragments.datepicker.DatePickerFragment
import com.capstone.interviewku.ui.fragments.datepicker.DatePickerListener
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SpinnerModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class RegisterDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterDetailBinding

    private val viewModel by viewModels<RegisterDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinnerGender.adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            Helpers.getGenders(this)
        )

        binding.btnSaveProfile.apply {
            isEnabled = false
            setOnClickListener {
                val gender = (binding.spinnerGender.selectedItem as SpinnerModel?)?.value
                val birthdate = viewModel.birthDate
                val currentCity = binding.etCurrentCity.text.toString()
                val jobPositionId =
                    (binding.spinnerJobPosition.selectedItem as SpinnerModel?)?.value?.toIntOrNull()

                if (isValidInput(gender, birthdate, currentCity, jobPositionId)) {
                    viewModel.addUserIdentity(jobPositionId!!, gender!!, birthdate!!, currentCity)
                } else {
                    showToast("Semua kolom harus diisi dengan benar")
                }
            }
        }

        binding.spinnerJobPosition.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    updateSaveButtonStatus()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.clBirthdate.setOnClickListener {
            DatePickerFragment(
                object : DatePickerListener {
                    override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
                        viewModel.birthDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(Calendar.getInstance().run {
                                set(year, month, dayOfMonth)
                                time
                            })
                        binding.tvBirthdate.text = viewModel.birthDate
                        updateSaveButtonStatus()
                    }
                }
            ).show(supportFragmentManager, "")
        }

        binding.etCurrentCity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                updateSaveButtonStatus()
            }
        })

        viewModel.jobPositionState.observe(this) { jobPositionsResponseResult ->
            when (jobPositionsResponseResult) {
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    jobPositionsResponseResult.data.data?.let { jobPositionsResponseData ->
                        val spinnerData = mutableListOf<SpinnerModel>()
                        spinnerData.addAll(
                            jobPositionsResponseData.jobPositions.sortedBy {
                                it.name
                            }.map { jobPosition ->
                                SpinnerModel(jobPosition.id.toString(), jobPosition.name)
                            }
                        )
                        spinnerData.add(0, SpinnerModel("-1", "Silahkan Pilih"))

                        binding.btnSaveProfile.isEnabled = true
                        binding.spinnerJobPosition.adapter = ArrayAdapter(
                            this,
                            R.layout.spinner_item,
                            spinnerData
                        )
                    }
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    jobPositionsResponseResult.exception.getData()
                        ?.handleHttpException(this)
                }
            }
        }

        viewModel.addUserIdentityState.observe(this) {
            when (it) {
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Error -> {
                    binding.btnSaveProfile.isEnabled = false
                    binding.progressBar.isVisible = false
                    it.exception.getData()?.handleHttpException(this)
                }
            }
        }

        viewModel.getJobPositions()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateSaveButtonStatus() {
        val gender = (binding.spinnerGender.selectedItem as SpinnerModel).value
        val birthdate = viewModel.birthDate
        val currentCity = binding.etCurrentCity.text.toString()
        val jobPositionId =
            (binding.spinnerJobPosition.selectedItem as SpinnerModel).value.toIntOrNull()

        binding.btnSaveProfile.isEnabled =
            isValidInput(gender, birthdate, currentCity, jobPositionId)
    }

    private fun isValidInput(
        gender: String?,
        birthdate: String?,
        currentCity: String,
        jobPositionId: Int?
    ): Boolean {
        return gender != null && birthdate != null && currentCity.isNotEmpty() && jobPositionId != null && jobPositionId != -1
    }
}