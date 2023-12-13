package com.capstone.interviewku.ui.activities.splash

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    suspend fun isLoggedIn() = authRepository.isLoggedIn()

    suspend fun isHasUserIdentity() = userRepository.isHasUserIdentity()
}