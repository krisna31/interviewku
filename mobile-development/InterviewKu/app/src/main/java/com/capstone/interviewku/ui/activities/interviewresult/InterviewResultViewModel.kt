package com.capstone.interviewku.ui.activities.interviewresult

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.InterviewRepository
import javax.inject.Inject

class InterviewResultViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository
) : ViewModel() {
}