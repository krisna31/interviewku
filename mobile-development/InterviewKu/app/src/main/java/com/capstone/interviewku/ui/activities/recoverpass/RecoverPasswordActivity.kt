package com.capstone.interviewku.ui.activities.recoverpass

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityRecoverPasswordBinding
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecoverPasswordBinding

    private val viewModel by viewModels<RecoverPasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecoverPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback {
            val alertDialog = AlertDialog.Builder(this@RecoverPasswordActivity)
                .setCancelable(false)
                .setTitle(getString(R.string.abort_recovery_title))
                .setMessage(getString(R.string.abort_recovery_confirmation))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    finish()
                }
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            if (!isFinishing) {
                alertDialog.show()
            }
        }

        if (!intent.hasExtra(EXTRA_EMAIL_KEY) || !intent.hasExtra(EXTRA_MESSAGE_KEY)) {
            finish()
            return
        }

        binding.tilNewPassword.isVisible = false
        binding.btnVerify.isEnabled = false

        binding.tvRecoveryInformation.text = intent.getStringExtra(EXTRA_MESSAGE_KEY)

        binding.btnVerify.setOnClickListener {
            val otpCode = binding.etOtp.text.toString()
            val email = intent.getStringExtra(EXTRA_EMAIL_KEY) ?: ""

            viewModel.verifyPasswordReset(email, otpCode)

            binding.etOtp.isEnabled = false
            it.isEnabled = false
        }

        binding.etOtp.apply {
            addTextChangedListener(afterTextChanged = {
                error = if (!Helpers.isOtpValid(it.toString()) && it.toString().isNotEmpty()) {
                    getString(R.string.otp_invalid)
                } else {
                    null
                }
                binding.btnVerify.isEnabled = Helpers.isOtpValid(it.toString())
            })
        }

        viewModel.verifyPasswordResetState.observe(this) { result ->
            binding.progressBar.isVisible = result is Result.Loading

            when (result) {
                is Result.Success -> {
                    Toast.makeText(this, getString(R.string.otp_code_correct), Toast.LENGTH_SHORT)
                        .show()

                    binding.btnVerify.setOnClickListener {
                        val email = intent.getStringExtra(EXTRA_EMAIL_KEY) ?: ""
                        val newPassword = binding.etNewPassword.text.toString()

                        viewModel.recoverPassword(email, newPassword)

                        binding.tilNewPassword.isEnabled = false
                        it.isEnabled = false
                    }

                    binding.tilNewPassword.isVisible = true
                    binding.etNewPassword.apply {
                        addTextChangedListener(afterTextChanged = {
                            error = if (!Helpers.isPasswordValid(it.toString())
                                && it.toString().isNotEmpty()
                            ) {
                                getString(R.string.password_invalid)
                            } else {
                                null
                            }
                            binding.btnVerify.isEnabled = Helpers.isPasswordValid(it.toString())
                        })
                    }
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    binding.etOtp.isEnabled = true
                    binding.btnVerify.isEnabled = true
                    result.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.recoverPasswordState.observe(this) { result ->
            binding.progressBar.isVisible = result is Result.Loading

            when (result) {
                is Result.Success -> {
                    Toast.makeText(
                        this,
                        getString(R.string.password_recovery_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    binding.tilNewPassword.isEnabled = true
                    binding.btnVerify.isEnabled = true
                    result.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_EMAIL_KEY = "EXTRA_EMAIL_KEY"
        const val EXTRA_MESSAGE_KEY = "EXTRA_MESSAGE_KEY"
    }
}