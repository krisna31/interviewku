package com.capstone.interviewku.ui.activities.chathistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.interviewku.data.ChatbotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatbotHistoryViewModel @Inject constructor(
    chatbotRepository: ChatbotRepository
) : ViewModel() {
    val chatHistory = chatbotRepository.getPagedChatHistory().cachedIn(viewModelScope)
}