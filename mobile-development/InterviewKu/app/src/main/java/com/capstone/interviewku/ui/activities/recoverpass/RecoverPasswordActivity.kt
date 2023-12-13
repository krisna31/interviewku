package com.capstone.interviewku.ui.activities.recoverpass

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityRecoverPasswordBinding
import com.capstone.interviewku.ui.activities.login.LoginActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
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


        binding.etNewPassword.visibility = View.GONE
        binding.btnVerify.setOnClickListener {
            val otpCode = binding.etOtp.text.toString()
            val email = binding.etOtp.text.toString()
            viewModel.verifyPasswordReset(otpCode, email)
        }
        binding.btnVerify.setOnClickListener {
            val email = binding.etNewPassword.text.toString()
            if (viewModel.verifyPasswordResetState.value is Result.Success) {
                binding.etNewPassword.visibility = View.VISIBLE
                val newPassword = binding.etNewPassword.text.toString()
                viewModel.recoverPassword(newPassword, email)
            } else {
                Toast.makeText(this, "Please verify OTP first", Toast.LENGTH_SHORT).show()
            }

        }

        viewModel.verifyPasswordResetState.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, getString(R.string.otp_code_correct), Toast.LENGTH_SHORT)
                        .show()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    result.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.recoverPasswordState.observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this,
                        getString(R.string.password_recovery_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    result.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}