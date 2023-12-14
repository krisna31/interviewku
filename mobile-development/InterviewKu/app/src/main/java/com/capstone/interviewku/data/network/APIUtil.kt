package com.capstone.interviewku.data.network

import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

object APIUtil {
    suspend fun <T> unauthorizedErrorHandler(
        apiService: InterviewKuAPIService,
        appPreferences: AppPreferences,
        apiFunction: suspend () -> T
    ): T {
        try {
            return apiFunction()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                appPreferences.getRefreshToken().first()?.let { refreshToken ->
                    apiService
                        .refreshAccessToken(refreshToken)
                        .data
                        ?.accessToken
                        ?.let { accessToken ->
                            appPreferences.setAccessToken(accessToken)
                        }

                    return apiFunction()
                }
            }

            throw e
        }
    }
}