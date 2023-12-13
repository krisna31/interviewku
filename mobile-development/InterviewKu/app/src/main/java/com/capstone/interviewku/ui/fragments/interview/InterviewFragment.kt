package com.capstone.interviewku.ui.fragments.interview

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.capstone.interviewku.R
import com.capstone.interviewku.databinding.FragmentInterviewBinding
import com.capstone.interviewku.ui.activities.interviewhistory.InterviewHistoryActivity
import com.capstone.interviewku.ui.activities.interviewtest.InterviewTestActivity
import com.capstone.interviewku.ui.activities.interviewtrain.InterviewTrainActivity
import com.capstone.interviewku.util.Extensions.isPermissionGranted

class InterviewFragment : Fragment() {
    private lateinit var microphonePermissionLauncher: ActivityResultLauncher<String>
    private var _binding: FragmentInterviewBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = requireActivity()

        microphonePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    Toast.makeText(
                        activity,
                        getString(R.string.mic_permission_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.mic_permission_prompt),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()

        binding.toolbar.apply {
            inflateMenu(R.menu.menu_fragment_interview)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_fragment_interview_history -> {
                        startActivity(Intent(context, InterviewHistoryActivity::class.java))
                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }

        binding.clTrainSession.setOnClickListener {
            activity.apply {
                if (isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
                    startActivity(Intent(this, InterviewTrainActivity::class.java))
                } else {
                    showMicrophonePermissionDialog(this)
                }
            }
        }

        binding.clTestSession.setOnClickListener {
            context.apply {
                startActivity(Intent(this, InterviewTestActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun showMicrophonePermissionDialog(activity: Activity) {
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle(getString(R.string.permission_title))
            .setMessage(getString(R.string.mic_permission_prompt))
            .setPositiveButton(getString(R.string.try_again)) { _, _ ->
                microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            .setNegativeButton(getString(R.string.abort)) { dialog, _ ->
                Toast.makeText(
                    activity,
                    getString(R.string.mic_permission_prompt),
                    Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()
            }
            .create()

        if (!activity.isFinishing) {
            alertDialog.show()
        }
    }
}