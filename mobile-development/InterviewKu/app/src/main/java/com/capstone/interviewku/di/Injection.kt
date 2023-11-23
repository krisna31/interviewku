package com.capstone.interviewku.di

import android.content.Context
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.TipsRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.APIUtil
import com.capstone.interviewku.data.preferences.AppPreferences
import com.capstone.interviewku.data.room.InterviewKuDatabase
import com.capstone.interviewku.util.Extensions.dataStore

object Injection {
    private fun provideAppPreferences(context: Context) =
        AppPreferences.getInstance(context.dataStore)

    private fun provideInterviewKuAPIService() = APIUtil.getInterviewKuAPIService()

    private fun provideInterviewKuDatabase(context: Context) =
        InterviewKuDatabase.getInstance(context)

    fun provideAuthRepository(context: Context) =
        AuthRepository.getInstance(provideInterviewKuAPIService(), provideAppPreferences(context))

    fun provideInterviewRepository(context: Context) =
        InterviewRepository.getInstance(
            provideInterviewKuAPIService(),
            provideAppPreferences(context)
        )

    fun provideJobRepository(context: Context) =
        JobRepository.getInstance(
            provideInterviewKuAPIService(),
            provideAppPreferences(context)
        )

    fun provideTipsRepository(context: Context) =
        TipsRepository.getInstance(
            provideInterviewKuAPIService(),
            provideAppPreferences(context),
            provideInterviewKuDatabase(context)
        )

    fun provideUserRepository(context: Context) =
        UserRepository.getInstance(
            provideInterviewKuAPIService(),
            provideAppPreferences(context)
        )
}