package com.capstone.interviewku.ui.fragments.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.capstone.interviewku.databinding.FragmentAccountBinding
import com.capstone.interviewku.ui.activities.changepassword.ChangePasswordActivity
import com.capstone.interviewku.ui.activities.interviewhistory.InterviewHistoryActivity
import com.capstone.interviewku.ui.activities.login.LoginActivity
import com.capstone.interviewku.ui.activities.profile.ProfileActivity
import com.capstone.interviewku.ui.activities.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.capstone.interviewku.util.Result
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<AccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clProfile.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.clTextToSpeach.setOnClickListener {
            val intent = Intent(requireContext(), InterviewHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.clResetPassword.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.clLogout.setOnClickListener {
            viewModel.logout()
            navigateToLoginScreen()
        }
        viewModel.logoutState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    navigateToLoginScreen()
                }
                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    val error = result.exception
                }
            }
        }
    }
    private fun navigateToLoginScreen() {
        val loginIntent = Intent(requireContext(), LoginActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(loginIntent)
        }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}