package com.capstone.interviewku.ui.activities.interviewhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.interviewku.data.InterviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterviewHistoryViewModel @Inject constructor(
    interviewRepository: InterviewRepository
) : ViewModel() {
    val interviewResults =
        interviewRepository.getPagedInterviewResults().cachedIn(viewModelScope)
}