package com.capstone.interviewku.ui.activities.forgetpass

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
}