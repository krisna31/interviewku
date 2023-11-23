package com.capstone.interviewku.ui.activities.interviewresult

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityInterviewResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterviewResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterviewResultBinding

    private val viewModel by viewModels<InterviewResultViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterviewResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}