package com.capstone.interviewku.ui.activities.changepassword

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
}