package com.capstone.interviewku.ui.activities.interviewtrain

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.interviewku.databinding.ActivityInterviewTrainBinding
import com.capstone.interviewku.ui.ViewModelFactory

class InterviewTrainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterviewTrainBinding

    private val viewModel by viewModels<InterviewTrainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterviewTrainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}