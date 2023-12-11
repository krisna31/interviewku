package com.capstone.interviewku.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.network.types.InterviewMode
import com.capstone.interviewku.data.paging.InterviewHistoryPagingSource
import com.capstone.interviewku.data.preferences.AppPreferences
import com.capstone.interviewku.util.Constants
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InterviewRepository @Inject constructor(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences
) {
    suspend fun getInterviewResults(page: Int, limit: Int) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getAllInterviewResults(
                appPreferences.getBearerToken().first(),
                page,
                limit,
            )
        }

    fun getPagedInterviewResults() = Pager(
        config = PagingConfig(
            pageSize = Constants.INTERVIEW_HISTORY_PAGE_SIZE
        ),
        pagingSourceFactory = {
            InterviewHistoryPagingSource(apiService, appPreferences)
        }
    ).liveData

    suspend fun getInterviewResultById(interviewId: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getInterviewResultById(
                appPreferences.getBearerToken().first(),
                interviewId
            )
        }

    suspend fun startInterviewTrainSession(jobFieldId: Int) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.startInterviewSession(
                appPreferences.getBearerToken().first(),
                InterviewMode.TRAIN.mode,
                jobFieldId
            )
        }

    suspend fun startInterviewTestSession(jobFieldId: Int) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.startInterviewSession(
                appPreferences.getBearerToken().first(),
                InterviewMode.TEST.mode,
                jobFieldId
            )
        }

    suspend fun sendInterviewAnswer(
        interviewId: String,
        token: String,
        audio: File,
        retryAttempt: Int,
        question: String,
        questionOrder: Int,
    ) = APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
        apiService.sendInterviewAnswer(
            bearerToken = appPreferences.getBearerToken().first(),
            interviewId = interviewId,
            token = token.toRequestBody("text/plain".toMediaType()),
            audio = MultipartBody.Part.createFormData(
                "audio",
                audio.name,
                audio.asRequestBody("audio/amr".toMediaType())
            ),
            retryAttempt = retryAttempt.toString().toRequestBody("text/plain".toMediaType()),
            question = question.toRequestBody("text/plain".toMediaType()),
            questionOrder = questionOrder.toString().toRequestBody("text/plain".toMediaType()),
        )
    }

    suspend fun endInterviewSession(interviewId: String, token: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.endInterviewSession(
                appPreferences.getBearerToken().first(),
                interviewId,
                true,
                token
            )
        }
}