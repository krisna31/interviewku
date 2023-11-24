package com.capstone.interviewku.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityLoginBinding
import com.capstone.interviewku.ui.activities.main.MainActivity
import com.capstone.interviewku.ui.activities.register.RegisterActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
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

        viewModel.loginState.observe(this) {
            when (it) {
                is Result.Success -> {
                    binding.progressBar.isVisible = false

                    it.data.getData()?.let { loginResponse ->
                        loginResponse.data?.let { loginData ->
                            lifecycleScope.launch {
                                viewModel.saveLoginData(loginData)

                                Toast.makeText(
                                    this@LoginActivity,
                                    loginResponse.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            }
                        } ?: run {
                            Toast.makeText(
                                this,
                                getString(R.string.unexpected_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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

        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.registerText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}