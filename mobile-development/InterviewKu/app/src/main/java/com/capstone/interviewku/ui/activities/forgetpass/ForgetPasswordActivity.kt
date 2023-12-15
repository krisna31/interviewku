package com.capstone.interviewku.ui.activities.forgetpass

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityForgetPasswordBinding
import com.capstone.interviewku.ui.activities.recoverpass.RecoverPasswordActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding

    private val viewModel by viewModels<ForgetPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Glide.with(this)
            .load(R.drawable.logo_transparent)
            .into(binding.civLogo)

        binding.btnSend.isEnabled = false
        binding.btnSend.setOnClickListener {
            binding.etEmail.apply {
                isEnabled = false
                viewModel.requestPasswordReset(text.toString())
            }
            it.isEnabled = false
        }

        binding.etEmail.apply {
            addTextChangedListener(afterTextChanged = {
                error = if (!Helpers.isEmailValid(it.toString()) && it.toString().isNotEmpty()) {
                    getString(R.string.email_invalid)
                } else {
                    null
                }
                binding.btnSend.isEnabled = Helpers.isEmailValid(it.toString())
            })
        }

        viewModel.passwordResetState.observe(this) { result ->
            binding.progressBar.isVisible = result is Result.Loading

            when (result) {
                is Result.Success -> {
                    Toast.makeText(
                        this,
                        getString(R.string.otp_sending_success),
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this, RecoverPasswordActivity::class.java).apply {
                        putExtra(
                            RecoverPasswordActivity.EXTRA_EMAIL_KEY,
                            binding.etEmail.text.toString()
                        )
                        putExtra(
                            RecoverPasswordActivity.EXTRA_MESSAGE_KEY,
                            result.data.message
                        )
                    })
                    finish()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    binding.apply {
                        etEmail.isEnabled = true
                        btnSend.isEnabled = true
                    }
                    result.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}