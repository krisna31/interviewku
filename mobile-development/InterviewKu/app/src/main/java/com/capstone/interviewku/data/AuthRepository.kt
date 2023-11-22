package com.capstone.interviewku.data

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.response.LoginResponse
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class AuthRepository private constructor(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences
) {
    suspend fun changePassword(oldPassword: String, newPassword: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.changePassword(
                appPreferences.getBearerToken().first(),
                oldPassword,
                newPassword
            )
        }

    fun isLoggedIn() = appPreferences.getAccessToken().asLiveData().map {
        it != null
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val result = apiService.login(email, password)

        result.data?.let {
            appPreferences.setAccessToken(it.accessToken)
            appPreferences.setRefreshToken(it.refreshToken)
        }

        return result
    }

    suspend fun logout() {
        try {
            apiService.logout(appPreferences.getRefreshToken().first() ?: "")
        } catch (_: HttpException) {
        }

        appPreferences.clearToken()
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(apiService: InterviewKuAPIService, appPreferences: AppPreferences) =
            instance ?: synchronized(this) {
                AuthRepository(apiService, appPreferences).also {
                    instance = it
                }
            }
    }
}