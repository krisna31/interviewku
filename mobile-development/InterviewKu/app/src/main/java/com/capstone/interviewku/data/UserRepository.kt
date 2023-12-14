package com.capstone.interviewku.data

import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences
) {
    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String?,
    ) = apiService.register(email, password, firstName, lastName)

    suspend fun getUser() = APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
        apiService.getUser(appPreferences.getBearerToken().first())
    }

    suspend fun getUserIdentity() = APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
        apiService.getUserIdentity(appPreferences.getBearerToken().first())
    }

    suspend fun addUserIdentity(
        jobPositionId: Int,
        gender: String,
        dateBirth: String,
        currentCity: String
    ) = APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
        apiService.addUserIdentity(
            appPreferences.getBearerToken().first(),
            jobPositionId,
            gender,
            dateBirth,
            currentCity
        )
    }

    suspend fun editUserIdentity(
        firstName: String,
        lastName: String?,
        jobPositionId: Int,
        gender: String,
        dateBirth: String,
        currentCity: String
    ) = APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
        apiService.editUserIdentity(
            appPreferences.getBearerToken().first(),
            firstName,
            lastName,
            jobPositionId,
            gender,
            dateBirth,
            currentCity
        )
    }

    suspend fun isHasUserIdentity() = appPreferences.isHasUserIdentity().first()

    suspend fun setHasUserIdentity(isHasUserIdentity: Boolean) {
        appPreferences.setHasUserIdentity(isHasUserIdentity)
    }
}