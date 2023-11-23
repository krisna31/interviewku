package com.capstone.interviewku.data

import com.capstone.interviewku.data.network.service.InterviewKuAPIService
import com.capstone.interviewku.data.preferences.AppPreferences
import com.capstone.interviewku.data.room.InterviewKuDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TipsRepository @Inject constructor(
    private val apiService: InterviewKuAPIService,
    private val appPreferences: AppPreferences,
    private val database: InterviewKuDatabase
) {
    companion object {
        @Volatile
        private var instance: TipsRepository? = null

        fun getInstance(
            apiService: InterviewKuAPIService,
            appPreferences: AppPreferences,
            database: InterviewKuDatabase
        ) = instance ?: synchronized(this) {
            TipsRepository(apiService, appPreferences, database).also {
                instance = it
            }
        }
    }
}