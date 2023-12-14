@file:Suppress("unused")

package com.capstone.interviewku.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.paging.ChatbotHistoryPagingSource
import com.capstone.interviewku.data.preferences.AppPreferences
import com.capstone.interviewku.util.Constants
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatbotRepository @Inject constructor(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences
) {
    fun getPagedChatHistory() = Pager(
        config = PagingConfig(
            pageSize = Constants.CHAT_PAGE_SIZE,
            initialLoadSize = Constants.CHAT_PAGE_SIZE
        ),
        pagingSourceFactory = {
            ChatbotHistoryPagingSource(apiService, appPreferences)
        }
    ).liveData

    suspend fun sendQuestion(question: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.sendQuestion(
                appPreferences.getBearerToken().first(),
                question
            )
        }

    suspend fun getChatById(chatId: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getChatById(
                appPreferences.getBearerToken().first(),
                chatId
            )
        }
}