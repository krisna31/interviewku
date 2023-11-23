package com.capstone.interviewku.ui.activities.interviewresult

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.InterviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterviewResultViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository
) : ViewModel() {
}