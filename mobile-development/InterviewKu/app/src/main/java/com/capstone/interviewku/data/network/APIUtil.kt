package com.capstone.interviewku.data.network

import com.capstone.interviewku.BuildConfig
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.network.token.TokenPayload
import com.capstone.interviewku.data.preferences.AppPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIUtil {
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
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()

        return retrofitClient.create(InterviewKuAPIService::class.java)
    }

    fun tokenPayloadDecoder(token: String): TokenPayload? {
        return try {
            Gson().fromJson(token, TokenPayload::class.java)
        } catch (_: Exception) {
            null
        }
    }

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