package com.capstone.interviewku.ui.activities.registerbasic

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.capstone.interviewku.databinding.ActivityRegisterBasicBinding
import com.capstone.interviewku.ui.activities.registerdetail.RegisterDetailActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
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

        setupTextWatchers()

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val firstName = binding.etFirstname.text.toString()
            val lastName = binding.etLastname.text.toString()

            if (validateInputs(email, password, firstName, lastName)) {
                viewModel.register(email, password, firstName, lastName)
            }
        }

        viewModel.registerState.observe(this) {
            when (it) {
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, RegisterDetailActivity::class.java))
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
    }

    private fun setupTextWatchers() {
        binding.etEmail.addTextChangedListener { validateEmail() }
        binding.etPassword.addTextChangedListener { validatePassword() }
        binding.etFirstname.addTextChangedListener { validateFirstName() }
        binding.etLastname.addTextChangedListener { validateLastName() }
    }

    private fun validateInputs(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Boolean {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        val isFirstNameValid = validateFirstName()
        val isLastNameValid = validateLastName()

        binding.btnRegister.isEnabled = isEmailValid && isPasswordValid && isFirstNameValid && isLastNameValid

        return isEmailValid && isPasswordValid && isFirstNameValid && isLastNameValid
    }

    private fun EditText.validate(validator: (String) -> Boolean, errorMessage: String) {
        val text = this.text.toString()
        if (!validator(text)) {
            this.error = errorMessage
        } else {
            this.error = null
        }
    }

    private fun validateEmail(): Boolean {
        binding.etEmail.validate({ Helpers.isEmailValid(it) }, "Email tidak valid")
        return binding.etEmail.error == null
    }

    private fun validatePassword(): Boolean {
        binding.etPassword.validate({ Helpers.isPasswordValid(it) },
            "Password harus minimal 8 karakter, terdapat 1 huruf kapital, dan 1 karakter simbol"
        )
        return binding.etPassword.error == null
    }

    private fun validateFirstName(): Boolean {
        binding.etFirstname.validate({ it.isNotEmpty() }, "First name tidak boleh kosong")
        return binding.etFirstname.error == null
    }

    private fun validateLastName(): Boolean {
        binding.etLastname.validate({ it.isNotEmpty() }, "Last name tidak boleh kosong")
        return binding.etLastname.error==null
        }
}