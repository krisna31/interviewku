package com.capstone.interviewku.ui.activities.profile

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityProfileBinding
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
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val viewModel by viewModels<ProfileViewModel>()

    private var jobFieldSpinnerData: MutableList<SpinnerModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.profile)
        }

        setButtonEnabled()
        setupInputWatchers()
        setupObservers()

        setInputEnabled(false)

        binding.spinnerGenderProfile.adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            Helpers.getGenders(this)
        )

        binding.btnSaveProfile.apply {
            setOnClickListener {
                val firstName = binding.etFirstname.text.toString()
                val lastName = binding.etLastname.text.toString()
                val currentCity = binding.etCurrentCity.text.toString()

                (binding.spinnerGenderProfile.selectedItem as SpinnerModel?)?.value?.let { gender ->
                    viewModel.birthDate?.let { birthdate ->
                        (binding.spinnerJobPosition.selectedItem as SpinnerModel?)
                            ?.value
                            ?.toIntOrNull()
                            ?.let { jobPositionId ->
                                viewModel.editUserIdentity(
                                    firstName,
                                    lastName,
                                    jobPositionId,
                                    gender,
                                    birthdate,
                                    currentCity
                                )
                            }
                    }
                }

                hideKeyboard(it)
                setInputEnabled(false)
                it.isEnabled = true
            }
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
            ).show(supportFragmentManager, null)
        }

        viewModel.getJobPositions()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun setButtonEnabled() {
        val firstName = binding.etFirstname.text.toString()
        val gender = (binding.spinnerGenderProfile.selectedItem as SpinnerModel?)?.value
        val birthdate = viewModel.birthDate
        val currentCity = binding.etCurrentCity.text.toString()
        val jobPositionId = (binding.spinnerJobPosition.selectedItem as SpinnerModel?)
            ?.value
            ?.toIntOrNull()

        binding.btnSaveProfile.isEnabled =
            firstName.isNotEmpty()
                    && gender?.isNotEmpty() == true
                    && birthdate != null
                    && currentCity.isNotEmpty()
                    && jobPositionId != null
                    && jobPositionId != -1
    }

    private fun setInputEnabled(isEnabled: Boolean) {
        binding.etFirstname.isEnabled = isEnabled
        binding.etLastname.isEnabled = isEnabled
        binding.spinnerGenderProfile.isEnabled = isEnabled
        binding.clBirthdate.isEnabled = isEnabled
        binding.etCurrentCity.isEnabled = isEnabled
        binding.spinnerJobPosition.isEnabled = isEnabled
    }

    private fun setupInputWatchers() {
        binding.etFirstname.addTextChangedListener(afterTextChanged = {
            setButtonEnabled()
        })

        binding.spinnerGenderProfile.onItemSelectedListener =
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
    }

    private fun setupObservers() {
        viewModel.jobPositionState.observe(this) { jobPositionsResponseResult ->
            binding.progressBar.isVisible = jobPositionsResponseResult is Result.Loading

            when (jobPositionsResponseResult) {
                is Result.Success -> {
                    jobPositionsResponseResult.data.data?.let { jobPositionsResponseData ->
                        jobFieldSpinnerData = mutableListOf<SpinnerModel>().apply {
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

                            binding.spinnerJobPosition.adapter = ArrayAdapter(
                                this@ProfileActivity,
                                R.layout.spinner_item,
                                this
                            )
                        }

                        viewModel.getUserIdentity()
                    }
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    jobPositionsResponseResult.exception
                        .getData()
                        ?.handleHttpException(this)
                        ?.let { message ->
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        viewModel.userIdentity.observe(this) { userInfoResult ->
            binding.progressBar.isVisible = userInfoResult is Result.Loading

            when (userInfoResult) {
                is Result.Success -> {
                    val userIdentity = userInfoResult.data

                    binding.etEmailPreview.setText(userIdentity.email)
                    binding.etFirstname.setText(userIdentity.firstname)
                    binding.etLastname.setText(userIdentity.lastname ?: "")
                    binding.spinnerGenderProfile.setSelection(
                        Helpers.getGenders(this).indexOfFirst {
                            it.value == userIdentity.gender
                        }
                    )
                    userIdentity.dateBirth?.let { birthDate ->
                        binding.tvBirthdate.text = birthDate
                        viewModel.birthDate = birthDate
                    }
                    binding.etCurrentCity.setText(userIdentity.currentCity ?: "")
                    binding.spinnerJobPosition.setSelection(
                        jobFieldSpinnerData?.let {
                            it.indexOfFirst { model ->
                                model.value.toIntOrNull() == userIdentity.jobPositionId
                            }
                        } ?: 0
                    )

                    setInputEnabled(true)
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    userInfoResult.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.editUserIdentityState.observe(this) { result ->
            binding.progressBar.isVisible = result is Result.Loading

            if (result !is Result.Loading) {
                setInputEnabled(true)
            }

            when (result) {
                is Result.Success -> {
                    Toast.makeText(
                        this,
                        getString(R.string.user_profile_saved),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    setButtonEnabled()
                    result.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
