package com.capstone.interviewku.ui.activities.interviewtrain

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.JobRepository

class InterviewTrainViewModel(
    private val jobRepository: JobRepository,
    private val interviewRepository: InterviewRepository
) : ViewModel() {
}