package com.capstone.interviewku.ui.activities.interviewhistory

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.InterviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterviewHistoryViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository
) : ViewModel() {
}