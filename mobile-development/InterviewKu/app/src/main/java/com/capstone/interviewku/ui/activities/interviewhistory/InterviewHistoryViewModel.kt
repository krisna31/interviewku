package com.capstone.interviewku.ui.activities.interviewhistory

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.InterviewRepository
import javax.inject.Inject

class InterviewHistoryViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository
) : ViewModel() {
}