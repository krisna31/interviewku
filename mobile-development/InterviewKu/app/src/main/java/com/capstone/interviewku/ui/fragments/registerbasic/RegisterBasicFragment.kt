package com.capstone.interviewku.ui.fragments.registerbasic

import android.os.Bundle
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

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val firstName = binding.etFirstname.text.toString()
            val lastName = binding.etLastname.text.toString()

            val errorMessages = mutableListOf<String>()

            if (firstName.isEmpty()) {
                errorMessages.add("First name tidak boleh kosong")
                binding.etFirstname.error = errorMessages.last()
            }

            if (lastName.isEmpty()) {
                errorMessages.add("Last name tidak boleh kosong")
                binding.etLastname.error = errorMessages.last()
            }

            if (!Helpers.isEmailValid(email)) {
                errorMessages.add("Email tidak valid")
                binding.etEmail.error = errorMessages.last()
            }

            if (!Helpers.isPasswordValid(password)) {
                errorMessages.add("Password harus minimal 8 karakter dan terdapat minimal 1 huruf kapital dan 1 karakter angka")
                binding.etPassword.error = errorMessages.last()
            }

            if (errorMessages.isEmpty()) {
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}