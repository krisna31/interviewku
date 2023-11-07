package com.capstone.interviewku.data.network

import com.capstone.interviewku.BuildConfig
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIUtil {
    private const val BASE_URL = "http://localhost:5000"

    fun getInterviewKuAPIService(): InterviewKuAPIService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        )

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofitClient = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

        return retrofitClient.create(InterviewKuAPIService::class.java)
    }

    suspend fun <T> unauthorizedErrorHandler(
        apiService: InterviewKuAPIService,
        appPreferences: AppPreferences,
        apiFunction: () -> T
    ): T {
        try {
            return apiFunction()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                val refreshToken = appPreferences.getRefreshToken()
                val accessToken =
                    apiService.refreshAccessToken(refreshToken.first() ?: "").data?.accessToken

                accessToken?.let {
                    appPreferences.setAccessToken(it)
                }

                return apiFunction()
            }

            throw e
        }
    }
}