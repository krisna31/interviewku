@file:Suppress("unused")

package com.capstone.interviewku.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.paging.ArticlesRemoteMediator
import com.capstone.interviewku.data.preferences.AppPreferences
import com.capstone.interviewku.data.room.InterviewKuDatabase
import com.capstone.interviewku.util.Constants
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class TipsRepository @Inject constructor(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences,
    private val database: InterviewKuDatabase
) {
    suspend fun getArticles(page: Int, limit: Int) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getArticles(
                appPreferences.getBearerToken().first(),
                page,
                limit
            )
        }

    fun getPagedArticles() = Pager(
        config = PagingConfig(
            initialLoadSize = Constants.ARTICLE_PAGE_SIZE,
            pageSize = Constants.ARTICLE_PAGE_SIZE,
        ),
        remoteMediator = ArticlesRemoteMediator(apiService, appPreferences, database),
        pagingSourceFactory = {
            database.getArticlesDao().getAllArticles()
        }
    ).liveData

    suspend fun getArticleById(articleId: Int) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getArticleById(
                appPreferences.getBearerToken().first(),
                articleId
            )
        }
}