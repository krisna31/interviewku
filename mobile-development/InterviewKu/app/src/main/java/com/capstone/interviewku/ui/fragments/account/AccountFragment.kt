package com.capstone.interviewku.ui.fragments.account

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.FragmentAccountBinding
import com.capstone.interviewku.ui.activities.changepassword.ChangePasswordActivity
import com.capstone.interviewku.ui.activities.interviewhistory.InterviewHistoryActivity
import com.capstone.interviewku.ui.activities.landing.LandingActivity
import com.capstone.interviewku.ui.activities.profile.ProfileActivity
import com.capstone.interviewku.util.Extensions.handleHttpException
import com.capstone.interviewku.util.Result
import dagger.hilt.android.AndroidEntryPoint

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

        setupObservers()

        val activity = requireActivity()

        binding.clProfile.setOnClickListener {
            val intent = Intent(activity, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.clTextToSpeach.setOnClickListener {
            val intent = Intent(activity, InterviewHistoryActivity::class.java)
            startActivity(intent)
        }

        binding.clResetPassword.setOnClickListener {
            val intent = Intent(activity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.clLogout.setOnClickListener {
            showLogoutConfirmationDialog(requireActivity())
        }

        binding.toolbar.apply {
            setLogo(R.mipmap.ic_launcher_round)
        }

        viewModel.logoutState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(activity, getString(R.string.logout_success), Toast.LENGTH_SHORT)
                        .show()
                    navigateToLandingScreen(activity)
                }

                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    result.exception.getData()?.handleHttpException(activity)
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.getUser()
        viewModel.getUserState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    binding.tvFullname.text = "${result.data?.firstname} ${result.data?.lastname}"
                }

                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(context, "Error: ${result.exception}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLogoutConfirmationDialog(activity: Activity) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alert_logout, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.btn_confirm_logout).setOnClickListener {
            viewModel.logout()
            dialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btn_cancel_logout).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun navigateToLandingScreen(activity: Activity) {
        val loginIntent = Intent(requireContext(), LandingActivity::class.java)
        startActivity(loginIntent)
        activity.finishAffinity()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}