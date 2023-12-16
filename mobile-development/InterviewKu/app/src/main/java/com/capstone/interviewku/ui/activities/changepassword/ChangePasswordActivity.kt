package com.capstone.interviewku.ui.activities.changepassword

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.ActivityChangePasswordBinding
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Extensions.hideKeyboard
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    private val viewModel by viewModels<ChangePasswordViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.change_password)
        }

        setButtonEnabled()
        setupInputWatchers()
        setupObservers()

        binding.btnChangePassword.setOnClickListener {
            val oldPassword = binding.etOldPassword.text.toString()
            val newPassword = binding.etNewPassword.text.toString()

            viewModel.changePassword(oldPassword, newPassword)

            hideKeyboard(it)
            setInputEnabled(false)
            it.isEnabled = true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun setButtonEnabled() {
        binding.apply {
            btnChangePassword.isEnabled =
                etOldPassword.text.isNotEmpty()
                        && Helpers.isPasswordValid(etNewPassword.text.toString())
        }
    }

    private fun setInputEnabled(isEnabled: Boolean) {
        binding.tilOldPassword.isEnabled = isEnabled
        binding.tilNewPassword.isEnabled = isEnabled
    }

    private fun setupInputWatchers() {
        binding.etNewPassword.apply {
            addTextChangedListener(afterTextChanged = {
                error = if (!Helpers.isPasswordValid(it.toString()) && it.toString().isNotEmpty()) {
                    getString(R.string.password_invalid)
                } else {
                    null
                }
                setButtonEnabled()
            })
        }
    }

    private fun setupObservers() {
        viewModel.changePasswordState.observe(this) { result ->
            binding.progressBar.isVisible = result is Result.Loading

            when (result) {
                is Result.Success -> {
                    binding.etOldPassword.setText("")
                    binding.etNewPassword.setText("")
                    setButtonEnabled()

                    Toast.makeText(
                        this,
                        getString(R.string.password_change_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Result.Loading -> {}

                is Result.Error -> {
                    setInputEnabled(true)
                    setButtonEnabled()

                    result.exception.getData()?.handleHttpException(this)?.let { message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}


