package com.capstone.interviewku.data

import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import kotlinx.coroutines.flow.first

class JobRepository private constructor(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences,
) {
    suspend fun getJobFields() =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getJobFields(appPreferences.getBearerToken().first())
        }

    suspend fun getJobPositions() =
        APIUtil.unauthorizedErrorHandler(apiService, appPreferences) {
            apiService.getJobPositions(appPreferences.getBearerToken().first())
        }

    companion object {
        @Volatile
        private var instance: JobRepository? = null

        fun getInstance(apiService: InterviewKuAPIService, appPreferences: AppPreferences) =
            instance ?: synchronized(this) {
                JobRepository(apiService, appPreferences).also {
                    instance = it
                }
            }
    }
}