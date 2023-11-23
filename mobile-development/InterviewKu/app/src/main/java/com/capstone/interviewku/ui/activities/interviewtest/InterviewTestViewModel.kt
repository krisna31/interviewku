package com.capstone.interviewku.ui.activities.interviewtest

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterviewTestViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val interviewRepository: InterviewRepository
) : ViewModel() {
}