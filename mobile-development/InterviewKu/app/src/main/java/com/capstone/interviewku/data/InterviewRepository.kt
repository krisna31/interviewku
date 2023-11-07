package com.capstone.interviewku.data

import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences

class InterviewRepository private constructor(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences
) {
    companion object {
        @Volatile
        private var instance: InterviewRepository? = null

        fun getInstance(apiService: InterviewKuAPIService, appPreferences: AppPreferences) =
            instance ?: synchronized(this) {
                InterviewRepository(apiService, appPreferences).also {
                    instance = it
                }
            }
    }
}