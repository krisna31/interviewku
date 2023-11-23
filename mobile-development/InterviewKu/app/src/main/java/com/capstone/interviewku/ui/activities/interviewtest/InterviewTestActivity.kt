package com.capstone.interviewku.ui.activities.interviewtest

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityInterviewTestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterviewTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterviewTestBinding

    private val viewModel by viewModels<InterviewTestViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterviewTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}