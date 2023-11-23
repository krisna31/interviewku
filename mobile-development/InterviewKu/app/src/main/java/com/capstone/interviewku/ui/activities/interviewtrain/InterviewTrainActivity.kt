package com.capstone.interviewku.ui.activities.interviewtrain

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityInterviewTrainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterviewTrainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterviewTrainBinding

    private val viewModel by viewModels<InterviewTrainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterviewTrainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}