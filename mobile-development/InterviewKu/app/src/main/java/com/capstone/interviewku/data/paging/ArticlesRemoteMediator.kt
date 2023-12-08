package com.capstone.interviewku.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import com.capstone.interviewku.data.room.InterviewKuDatabase
import com.capstone.interviewku.data.room.entity.ArticleEntity
import com.capstone.interviewku.util.Helpers
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
class ArticlesRemoteMediator(
    private val appPreferences: AppPreferences,
    private val interviewKuAPIService: InterviewKuAPIService,
    private val interviewKuDatabase: InterviewKuDatabase,
) : RemoteMediator<Int, ArticleEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        return try {
            val response = APIUtil.unauthorizedErrorHandler(interviewKuAPIService, appPreferences) {
                interviewKuAPIService.getAllArticles(
                    appPreferences.getBearerToken().first(),
                    page = 1,
                    limit = state.config.pageSize
                )
            }
            interviewKuDatabase.withTransaction {
                val dao = interviewKuDatabase.getArticlesDao()

                response.data?.let { articleItems ->
                    if (loadType == LoadType.REFRESH) {
                        dao.deleteAllArticles()
                    }

                    dao.insertAllArticles(articleItems.map {
                        Helpers.articleResponseToArticleEntity(it)
                    })
                }
            }

            return MediatorResult.Success(endOfPaginationReached = response.data?.isEmpty() == true)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}