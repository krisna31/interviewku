package com.capstone.interviewku.ui.activities.interviewhistory

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.interviewku.databinding.ActivityInterviewHistoryBinding
import com.capstone.interviewku.ui.activities.interviewresult.InterviewResultActivity
import com.capstone.interviewku.ui.adapters.ItemInterviewHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterviewHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInterviewHistoryBinding

    private val viewModel by viewModels<InterviewHistoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInterviewHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val interviewHistoryAdapter = ItemInterviewHistoryAdapter {
            startActivity(
                Intent(this, InterviewResultActivity::class.java).apply {
                    putExtra(InterviewResultActivity.INTERVIEW_ID_KEY, it.interviewId)
                }
            )
        }

        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@InterviewHistoryActivity)
            adapter = interviewHistoryAdapter
        }

        viewModel.allInterviewResults.observe(this) {
            interviewHistoryAdapter.submitData(lifecycle, it)
        }
    }
}