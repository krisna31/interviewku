package com.capstone.interviewku.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityLoginBinding
import com.capstone.interviewku.ui.activities.forgetpass.ForgetPasswordActivity
import com.capstone.interviewku.ui.activities.main.MainActivity
import com.capstone.interviewku.ui.activities.registerdetail.RegisterDetailActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Extensions.hideKeyboard
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.logo_transparent)
            .into(binding.civLogo)

        setButtonEnabled()
        setupTextWatchers()
        setupObservers()

        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            viewModel.login(email, password)

            hideKeyboard(it)
            setInputEnabled(false)
            it.isEnabled = false
        }

        binding.tvRecovery.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setButtonEnabled() {
        binding.btnLogin.isEnabled =
            Helpers.isEmailValid(binding.etLoginEmail.text.toString())
                    && binding.etLoginPassword.text.isNotEmpty()
    }

    private fun setInputEnabled(isEnabled: Boolean) {
        binding.etLoginEmail.isEnabled = isEnabled
        binding.tilPassword.isEnabled = isEnabled
    }

    private fun setupObservers() {
        viewModel.loginState.observe(this) {
            binding.progressBar.isVisible = it is Result.Loading

            when (it) {
                is Result.Success -> {
                    lifecycleScope.launch {
                        if (viewModel.isHasUserIdentity()) {
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    MainActivity::class.java
                                )
                            )
                        } else {
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    RegisterDetailActivity::class.java
                                )
                            )
                        }
                        finishAffinity()
                    }
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
        binding.etLoginEmail.apply {
            addTextChangedListener(afterTextChanged = {
                error = if (!Helpers.isEmailValid(it.toString()) && it.toString().isNotEmpty()) {
                    getString(R.string.email_invalid)
                } else {
                    null
                }
                setButtonEnabled()
            })
        }
        binding.etLoginPassword.addTextChangedListener(afterTextChanged = {
            setButtonEnabled()
        })
    }
}
