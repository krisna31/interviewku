package com.capstone.interviewku.ui.fragments.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.ChatbotRepository
import com.capstone.interviewku.data.network.response.ChatbotDetailResponse
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val chatbotRepository: ChatbotRepository
) : ViewModel() {
    private val _sendQuestionState = MutableLiveData<Result<ChatbotDetailResponse>>()
    val sendQuestionState: LiveData<Result<ChatbotDetailResponse>>
        get() = _sendQuestionState

    fun sendQuestion(question: String) = viewModelScope.launch {
        _sendQuestionState.value = Result.Loading

        try {
            _sendQuestionState.value = Result.Success(
                chatbotRepository.sendQuestion(question)
            )
        } catch (e: Exception) {
            _sendQuestionState.value = Result.Error(SingleEvent(e))
        }
    }
}