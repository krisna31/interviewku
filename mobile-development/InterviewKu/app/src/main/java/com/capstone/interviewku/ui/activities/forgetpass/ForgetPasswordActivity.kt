package com.capstone.interviewku.ui.activities.forgetpass

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityForgetPasswordBinding
import com.capstone.interviewku.ui.activities.recoverpass.RecoverPasswordActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
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

        binding.btnSend.setOnClickListener {
            val email = binding.etEmail.text.toString()
            viewModel.requestPasswordReset(email)
        }

        observePasswordResetState()
    }

    private fun observePasswordResetState() {
        viewModel.passwordResetState.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    showToast(getString(R.string.otp_sending_success))
                    navigateToRecoverPasswordActivity()
                }

                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    result.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun navigateToRecoverPasswordActivity() {
        val intent = Intent(this, RecoverPasswordActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}