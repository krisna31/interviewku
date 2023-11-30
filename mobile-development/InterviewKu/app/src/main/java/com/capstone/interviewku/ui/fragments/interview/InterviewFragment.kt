package com.capstone.interviewku.ui.fragments.interview

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.interviewku.databinding.FragmentInterviewBinding
import com.capstone.interviewku.ui.activities.interviewtest.InterviewTestActivity
import com.capstone.interviewku.ui.activities.interviewtrain.InterviewTrainActivity

class InterviewFragment : Fragment() {
    private var _binding: FragmentInterviewBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInterviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext()

        binding.clTrainSession.setOnClickListener {
            context.apply {
                startActivity(Intent(this, InterviewTrainActivity::class.java))
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
}