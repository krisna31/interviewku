package com.capstone.interviewku.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityLoginBinding
import com.capstone.interviewku.ui.activities.main.MainActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Extensions.hideKeyboard
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupTextWatchers()
    }
    private fun setupObservers() {
        viewModel.loginState.observe(this) {
            when (it) {
                is Result.Success -> {
                    binding.progressBar.isVisible = false

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finishAffinity()
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    it.exception.getData()?.handleHttpException(this)
                }
            }
        }



        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            viewModel.login(email, password)

            hideKeyboard(it)
        }
    }

    private fun setupTextWatchers() {
        val emailWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateInputs()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        val passwordWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateInputs()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.etLoginEmail.addTextChangedListener(emailWatcher)
        binding.etLoginPassword.addTextChangedListener(passwordWatcher)

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            viewModel.login(email, password)
        }
    }

    private fun validateInputs() {
        val email = binding.etLoginEmail.text.toString()
        val password = binding.etLoginPassword.text.toString()

        if (!Helpers.isEmailValid(email)) {
            binding.etLoginEmail.error = getString(R.string.eror_email)
            binding.btnLogin.isEnabled = false
        } else if (!Helpers.isPasswordValid(password)) {
            binding.etLoginPassword.error = getString(R.string.eror_password)
            binding.btnLogin.isEnabled = false
        } else {
            binding.etLoginEmail.error = null
            binding.etLoginPassword.error = null
            binding.btnLogin.isEnabled = true
            }
        }
}
