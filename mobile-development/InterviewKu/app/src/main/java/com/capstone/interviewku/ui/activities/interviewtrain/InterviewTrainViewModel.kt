package com.capstone.interviewku.ui.activities.interviewtrain

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterviewTrainViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val interviewRepository: InterviewRepository
) : ViewModel() {
}