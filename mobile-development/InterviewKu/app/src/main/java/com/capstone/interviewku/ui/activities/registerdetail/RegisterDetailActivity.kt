package com.capstone.interviewku.ui.activities.registerdetail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityRegisterDetailBinding
import com.capstone.interviewku.ui.activities.main.MainActivity
import com.capstone.interviewku.ui.fragments.datepicker.DatePickerFragment
import com.capstone.interviewku.ui.fragments.datepicker.DatePickerListener
import com.capstone.interviewku.util.Constants
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Extensions.hideKeyboard
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

        setButtonEnabled()
        setupInputWatchers()
        setupObservers()

        setInputEnabled(false)

        binding.spinnerGender.adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            Helpers.getGenders(this)
        )

        binding.btnSaveProfile.setOnClickListener {
            (binding.spinnerGender.selectedItem as SpinnerModel?)?.value?.let { gender ->
                viewModel.birthDate?.let { birthdate ->
                    (binding.spinnerJobPosition.selectedItem as SpinnerModel?)
                        ?.value
                        ?.toIntOrNull()
                        ?.let { jobPositionId ->
                            viewModel.addUserIdentity(
                                jobPositionId,
                                gender,
                                birthdate,
                                binding.etCurrentCity.text.toString()
                            )
                        }
                }
            }

            hideKeyboard(it)
            setInputEnabled(false)
            it.isEnabled = false
        }

        binding.clBirthdate.setOnClickListener {
            DatePickerFragment(
                object : DatePickerListener {
                    override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
                        viewModel.birthDate =
                            SimpleDateFormat(Constants.BIRTHDATE_FORMAT, Locale.getDefault())
                                .format(Calendar.getInstance().run {
                                    set(year, month, dayOfMonth)
                                    time
                                })
                        binding.tvBirthdate.text = viewModel.birthDate
                        setButtonEnabled()
                    }
                }
            ).show(supportFragmentManager, "")
        }

        viewModel.getJobPositions()
    }

    private fun setButtonEnabled() {
        val gender =
            (binding.spinnerGender.selectedItem as SpinnerModel?)?.value
        val birthdate = viewModel.birthDate
        val currentCity = binding.etCurrentCity.text.toString()
        val jobPositionId =
            (binding.spinnerJobPosition.selectedItem as SpinnerModel?)?.value?.toIntOrNull()

        binding.btnSaveProfile.isEnabled =
            gender?.isNotEmpty() == true
                    && birthdate != null
                    && currentCity.isNotEmpty()
                    && jobPositionId != null
                    && jobPositionId != -1
    }

    private fun setInputEnabled(isEnabled: Boolean) {
        binding.spinnerGender.isEnabled = isEnabled
        binding.clBirthdate.isEnabled = isEnabled
        binding.etCurrentCity.isEnabled = isEnabled
        binding.spinnerJobPosition.isEnabled = isEnabled
    }

    private fun setupInputWatchers() {
        binding.spinnerGender.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    setButtonEnabled()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.spinnerJobPosition.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    setButtonEnabled()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        binding.etCurrentCity.addTextChangedListener(afterTextChanged = {
            setButtonEnabled()
        })
    }

    private fun setupObservers() {
        viewModel.jobPositionState.observe(this) { jobPositionsResponseResult ->
            binding.progressBar.isVisible = jobPositionsResponseResult is Result.Loading
            when (jobPositionsResponseResult) {
                is Result.Success -> {
                    jobPositionsResponseResult.data.data?.let { jobPositionsResponseData ->
                        val spinnerData = mutableListOf<SpinnerModel>().apply {
                            add(SpinnerModel("-1", getString(R.string.please_choose)))
                            add(jobPositionsResponseData.jobPositions[0].let {
                                // General
                                SpinnerModel(it.id.toString(), it.name)
                            })
                            addAll(
                                jobPositionsResponseData.jobPositions.subList(
                                    1,
                                    jobPositionsResponseData.jobPositions.size
                                ).sortedBy {
                                    it.name
                                }.map { jobPosition ->
                                    SpinnerModel(jobPosition.id.toString(), jobPosition.name)
                                }
                            )
                        }

                        binding.spinnerJobPosition.adapter = ArrayAdapter(
                            this,
                            R.layout.spinner_item,
                            spinnerData
                        )

                        setInputEnabled(true)
                    }
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    val alertDialog = AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle(getString(R.string.error_title))
                        .setMessage(
                            jobPositionsResponseResult.exception.peekData()
                                .handleHttpException(this)
                        )
                        .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                            viewModel.getJobPositions()
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

        viewModel.addUserIdentityState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading

            when (it) {
                is Result.Success -> {
                    Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    setInputEnabled(true)
                    setButtonEnabled()
                    it.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}