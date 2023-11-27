package com.capstone.interviewku.data

import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.network.types.InterviewMode
import com.capstone.interviewku.data.preferences.AppPreferences
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
    suspend fun startInterviewTrainSession() =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.startInterviewSession(
                appPreferences.getBearerToken().first(),
                InterviewMode.TRAIN.mode
            )
        }

    suspend fun startInterviewTestSession() =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.startInterviewSession(
                appPreferences.getBearerToken().first(),
                InterviewMode.TEST.mode
            )
        }

    suspend fun getInterviewQuestions(interviewId: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getCurrentInterviewQuestions(
                appPreferences.getBearerToken().first(),
                interviewId
            )
        }

    suspend fun sendInterviewAnswer(
        token: String,
        audio: File,
        jobPositionName: String,
        retryAttempt: Int,
        question: String,
        questionOrder: Int,
    ) = APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
        apiService.sendInterviewAnswer(
            token = token.toRequestBody("text/plain".toMediaType()),
            audio = MultipartBody.Part.createFormData(
                "audio",
                audio.name,
                audio.asRequestBody("audio/*".toMediaType())
            ),
            jobPositionName = jobPositionName.toRequestBody("text/plain".toMediaType()),
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

    suspend fun getInterviewResult(interviewId: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getInterviewResult(
                appPreferences.getBearerToken().first(),
                interviewId
            )
        }
}