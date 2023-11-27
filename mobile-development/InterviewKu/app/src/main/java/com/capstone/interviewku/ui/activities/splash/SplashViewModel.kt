package com.capstone.interviewku.ui.activities.splash

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    suspend fun isLoggedIn() = authRepository.isLoggedIn()
}