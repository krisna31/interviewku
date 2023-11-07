package com.capstone.interviewku.ui

import android.content.Context
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.TipsRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.di.Injection

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
    private val interviewRepository: InterviewRepository,
    private val tipsRepository: TipsRepository,
    private val userRepository: UserRepository,
) {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            ViewModelFactory(
                Injection.provideAuthRepository(context),
                Injection.provideInterviewRepository(context),
                Injection.provideTipsRepository(context),
                Injection.provideUserRepository(context),
            ).also {
                instance = it
            }
        }
    }
}