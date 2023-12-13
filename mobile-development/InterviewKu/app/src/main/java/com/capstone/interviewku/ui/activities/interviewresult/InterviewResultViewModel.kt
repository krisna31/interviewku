package com.capstone.interviewku.ui.activities.interviewresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.network.response.InterviewResultData
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterviewResultViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository
) : ViewModel() {
    private val _interviewResultState = MutableLiveData<Result<InterviewResultData>>()
    val interviewResultState: LiveData<Result<InterviewResultData>>
        get() = _interviewResultState

    private var interviewId: String? = null

    fun getInterviewResultById() = viewModelScope.launch {
        _interviewResultState.value = Result.Loading

        try {
            interviewId?.let { id ->
                interviewRepository.getInterviewResultById(id).data?.let {
                    _interviewResultState.value = Result.Success(it)
                } ?: run {
                    throw Exception()
                }
            }
        } catch (e: Exception) {
            _interviewResultState.value = Result.Error(SingleEvent(e))
        }
    }

    fun setInterviewResult(interviewResultData: InterviewResultData) {
        _interviewResultState.value = Result.Success(interviewResultData)
    }

    fun setInterviewResultId(interviewId: String) {
        this.interviewId = interviewId
    }
}