package com.capstone.interviewku.ui.activities.interviewresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.network.response.InterviewResultResponse
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewResultViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository
) : ViewModel() {
    private val _interviewResultState = MutableLiveData<Result<InterviewResultResponse>>()
    val interviewResultState: LiveData<Result<InterviewResultResponse>>
        get() = _interviewResultState

    fun getInterviewResultById(interviewId: String) = viewModelScope.launch {
        _interviewResultState.value = Result.Loading

        try {
            val response = interviewRepository.getInterviewResultById(interviewId)
            _interviewResultState.value = Result.Success(response)
        } catch (e: Exception) {
            _interviewResultState.value = Result.Error(SingleEvent(e))
        }
    }
}