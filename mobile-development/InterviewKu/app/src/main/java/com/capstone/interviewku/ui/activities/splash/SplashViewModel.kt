package com.capstone.interviewku.ui.activities.splash

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
}