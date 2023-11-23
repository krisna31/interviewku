package com.capstone.interviewku.ui.activities.interviewhistory

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityInterviewHistoryBinding
import com.capstone.interviewku.ui.ViewModelFactory

class InterviewHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterviewHistoryBinding

    private val viewModel by viewModels<InterviewHistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterviewHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}