package com.capstone.interviewku.ui.activities.registerbasic

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityRegisterBasicBinding
import com.capstone.interviewku.ui.activities.registerdetail.RegisterDetailActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Extensions.hideKeyboard
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterBasicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBasicBinding

    private val viewModel by viewModels<RegisterBasicViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBasicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.logo_transparent)
            .into(binding.civLogo)

        setButtonEnabled()
        setupTextWatchers()
        setupObservers()

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val firstName = binding.etFirstname.text.toString()
            val lastName = binding.etLastname.text.toString()

            viewModel.register(
                email,
                password,
                firstName,
                lastName.ifEmpty {
                    null
                }
            )

            hideKeyboard(it)
            setInputEnabled(false)
            it.isEnabled = false
        }
    }

    private fun setInputEnabled(isEnabled: Boolean) {
        binding.etEmail.isEnabled = isEnabled
        binding.tilPassword.isEnabled = isEnabled
        binding.etFirstname.isEnabled = isEnabled
        binding.etLastname.isEnabled = isEnabled
    }

    private fun setButtonEnabled() {
        binding.apply {
            btnRegister.isEnabled =
                Helpers.isEmailValid(etEmail.text.toString())
                        && Helpers.isPasswordValid(etPassword.text.toString())
                        && etFirstname.text.isNotEmpty()
        }
    }

    private fun setupObservers() {
        viewModel.registerState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading

            when (it) {
                is Result.Success -> {
                    Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, RegisterDetailActivity::class.java))
                    finishAffinity()
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

    private fun setupTextWatchers() {
        binding.etEmail.apply {
            addTextChangedListener(afterTextChanged = {
                error = if (!Helpers.isEmailValid(it.toString()) && it.toString().isNotEmpty()) {
                    getString(R.string.email_invalid)
                } else {
                    null
                }
                setButtonEnabled()
            })
        }
        binding.etPassword.apply {
            addTextChangedListener(afterTextChanged = {
                error = if (!Helpers.isPasswordValid(it.toString()) && it.toString().isNotEmpty()) {
                    getString(R.string.password_invalid)
                } else {
                    null
                }
                setButtonEnabled()
            })
        }
        binding.etFirstname.apply {
            addTextChangedListener(afterTextChanged = {
                error = if (it.toString().isEmpty()) {
                    getString(R.string.firstname_empty)
                } else {
                    null
                }
                setButtonEnabled()
            })
        }
    }
}