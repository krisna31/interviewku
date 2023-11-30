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
    suspend fun getAllInterviews() =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getAllInterviews(
                appPreferences.getBearerToken().first()
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

    suspend fun getInterviewQuestions(interviewId: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getCurrentInterviewQuestions(
                appPreferences.getBearerToken().first(),
                interviewId
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

    suspend fun getInterviewResult(interviewId: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getInterviewResult(
                appPreferences.getBearerToken().first(),
                interviewId
            )
        }
}