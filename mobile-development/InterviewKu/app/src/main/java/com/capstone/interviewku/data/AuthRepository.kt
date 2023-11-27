package com.capstone.interviewku.data

import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.response.LoginData
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences
) {
    suspend fun clearLoginData() {
        appPreferences.clearToken()
    }

    suspend fun changePassword(oldPassword: String, newPassword: String) =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.changePassword(
                appPreferences.getBearerToken().first(),
                oldPassword,
                newPassword
            )
        }

    suspend fun isLoggedIn() = appPreferences.getAccessToken().first() != null

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun logout() = apiService.logout(appPreferences.getRefreshToken().first() ?: "")

    suspend fun requestPasswordReset(email: String) = apiService.requestPasswordReset(email)

    suspend fun recoverPassword(email: String, newPassword: String) =
        apiService.recoverPassword(email, newPassword)

    suspend fun saveLoginData(loginData: LoginData) {
        appPreferences.setAccessToken(loginData.accessToken)
        appPreferences.setRefreshToken(loginData.refreshToken)
    }

    suspend fun verifyPasswordReset(email: String, otpCode: String) =
        apiService.verifyPasswordReset(email, otpCode)
}