package com.capstone.interviewku.ui.activities.login

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
}