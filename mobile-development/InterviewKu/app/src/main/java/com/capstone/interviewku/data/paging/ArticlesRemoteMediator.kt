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
import com.capstone.interviewku.data.room.entity.ArticleRemoteKey
import com.capstone.interviewku.util.Helpers
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
class ArticlesRemoteMediator(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences,
    private val database: InterviewKuDatabase,
) : RemoteMediator<Int, ArticleEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.PREPEND -> {
                    val firstItemRemoteKey = getFirstItemRemoteKey(state)
                    val prevKey = firstItemRemoteKey?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = firstItemRemoteKey != null)

                    prevKey
                }

                LoadType.REFRESH -> {
                    getClosestItemRemoteKey(state)?.nextKey?.minus(1) ?: 1
                }

                LoadType.APPEND -> {
                    val lastItemRemoteKey = getLastItemRemoteKey(state)
                    val nextKey = lastItemRemoteKey?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = lastItemRemoteKey != null)

                    nextKey
                }
            }

            val response = APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
                apiService.getArticles(
                    appPreferences.getBearerToken().first(),
                    page = page,
                    limit = state.config.pageSize
                )
            }

            val endOfPaginationReached = response.data?.isEmpty() == true

            val articlesDao = database.getArticlesDao()
            val articleRemoteKeysDao = database.getArticleRemoteKeysDao()

            database.withTransaction {
                response.data?.let { articles ->
                    if (loadType == LoadType.REFRESH) {
                        articlesDao.deleteAllArticles()
                        articleRemoteKeysDao.deleteAllArticleRemoteKeys()
                    }

                    val previousKey = if (page == 1) {
                        null
                    } else {
                        page - 1
                    }

                    val nextKey = if (endOfPaginationReached) {
                        null
                    } else {
                        page + 1
                    }

                    articlesDao.insertAllArticles(articles.map {
                        Helpers.articleResponseToArticleEntity(it)
                    })
                    articleRemoteKeysDao.insertArticleRemoteKeys(articles.map {
                        ArticleRemoteKey(it.id, previousKey, nextKey)
                    })
                }
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getFirstItemRemoteKey(state: PagingState<Int, ArticleEntity>) =
        state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { firstItem ->
            database.getArticleRemoteKeysDao().getArticleRemoteKeyById(firstItem.id)
        }

    private suspend fun getClosestItemRemoteKey(state: PagingState<Int, ArticleEntity>) =
        state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)
        }?.id?.let { closestItemId ->
            database.getArticleRemoteKeysDao().getArticleRemoteKeyById(closestItemId)
        }

    private suspend fun getLastItemRemoteKey(state: PagingState<Int, ArticleEntity>) =
        state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { lastItem ->
            database.getArticleRemoteKeysDao().getArticleRemoteKeyById(lastItem.id)
        }
}