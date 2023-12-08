package com.capstone.interviewku.ui.fragments.registerbasic

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.fragment.app.viewModels
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.FragmentRegisterBasicBinding
import com.capstone.interviewku.ui.fragments.registerdetail.RegisterDetailFragment
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Helpers
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterBasicFragment : Fragment() {
    private var _binding: FragmentRegisterBasicBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<RegisterBasicViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBasicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        viewModel.registerState.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                    parentFragmentManager.commitNow(allowStateLoss = true) {
                        replace(R.id.frame_layout_root, RegisterDetailFragment())
                    }
                }

                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    it.exception.getData()?.handleHttpException(requireContext())
                }
            }
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val firstName = binding.etFirstname.text.toString()
                val lastName = binding.etLastname.text.toString()

                binding.btnRegister.isEnabled = validateInputs(email, password, firstName, lastName)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
        binding.etFirstname.addTextChangedListener(textWatcher)
        binding.etLastname.addTextChangedListener(textWatcher)
    }

    private fun validateInputs(email: String, password: String, firstName: String, lastName: String): Boolean {
        var isValid = true

        if (firstName.isEmpty()) {
            binding.etFirstname.error = "First name tidak boleh kosong"
            isValid = false
        } else {
            binding.etFirstname.error = null
        }

        if (lastName.isEmpty()) {
            binding.etLastname.error = "Last name tidak boleh kosong"
            isValid = false
        } else {
            binding.etLastname.error = null
        }

        if (!Helpers.isEmailValid(email)) {
            binding.etEmail.error = "Email tidak valid"
            isValid = false
        } else {
            binding.etEmail.error = null
        }

        if (!Helpers.isPasswordValid(password)) {
            binding.etPassword.error = "Password harus minimal 8 karakter, terdapat 1 huruf kapital, dan 1 karakter simbol"
            isValid = false
        } else {
            binding.etPassword.error = null
        }

        return isValid
    }



    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}