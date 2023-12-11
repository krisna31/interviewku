package com.capstone.interviewku.ui.activities.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityProfileBinding
import com.capstone.interviewku.ui.fragments.account.AccountFragment
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
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val viewModel by viewModels<ProfileViewModel>()

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
        binding.spinnerGenderProfile.adapter = ArrayAdapter(
            this,
            R.layout.spinner_item,
            Helpers.getGenders(this)

        )


        binding.btnSaveProfile.apply {
            isEnabled = false
            setOnClickListener {
                val email = binding.etEmailPreview.text.toString()
                val firstName = binding.etFirstname.text.toString()
                val lastName = binding.etLastname.text.toString()
                val gender = (binding.spinnerGenderProfile.selectedItem as SpinnerModel?)?.value
                val birthdate = viewModel.birthDate
                val currentCity = binding.tvCurrentCity.text.toString()
                val jobPositionId =
                    (binding.spinnerJobPosition.selectedItem as SpinnerModel?)?.value?.toIntOrNull()

                viewModel.editUserIdentity(firstName, lastName, jobPositionId ?: -1, gender ?: "", birthdate ?: "", currentCity ?: "")
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

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        binding.tvBirthdate.setOnClickListener {
            DatePickerFragment(
                object : DatePickerListener {
                    override fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
                        viewModel.birthDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            .format(Calendar.getInstance().run {
                                set(year, month, dayOfMonth)
                                time
                            })
                        binding.tvBirthdate.text = viewModel.birthDate
                    }
                }
            ).show(supportFragmentManager, "")
        }
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

        viewModel.userIdentity.observe(this) { userInfoResult ->
            when (userInfoResult) {
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    val userIdentity = userInfoResult.data

                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = true

                }
                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    userInfoResult.exception.getData()?.handleHttpException(this)

                }
            }
        }
        viewModel.editUserIdentityState.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    showToast("Perubahan telah di simpan")
                    startActivity(Intent(this, AccountFragment::class.java))
                    finish()
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Error -> {
                    binding.btnSaveProfile.isEnabled = false
                    binding.progressBar.isVisible = false
                    result.exception.getData()?.handleHttpException(this)
                }
            }
        }

        viewModel.getJobPositions()
        viewModel.getUserIdentity()

        }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}