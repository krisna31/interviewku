package com.capstone.interviewku.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.response.InterviewHistoryData
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import kotlinx.coroutines.flow.first

class InterviewHistoryPagingSource(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences
) :
    PagingSource<Int, InterviewHistoryData>() {
    override fun getRefreshKey(state: PagingState<Int, InterviewHistoryData>): Int? =
        state.anchorPosition?.let {
            val closestPage = state.closestPageToPosition(it)
            return@let closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InterviewHistoryData> =
        try {
            val currentPos = params.key ?: 1
            val response = APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
                apiService.getAllInterviewResults(
                    appPreferences.getBearerToken().first(),
                    currentPos,
                    params.loadSize
                )
            }

            LoadResult.Page(
                data = response.data ?: emptyList(),
                prevKey = if (currentPos == 1) null else currentPos - 1,
                nextKey = if (response.data.isNullOrEmpty()) null else currentPos + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}