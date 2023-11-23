package com.capstone.interviewku.ui.activities.interviewtest

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.JobRepository
import javax.inject.Inject

class InterviewTestViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val interviewRepository: InterviewRepository
) : ViewModel() {
}