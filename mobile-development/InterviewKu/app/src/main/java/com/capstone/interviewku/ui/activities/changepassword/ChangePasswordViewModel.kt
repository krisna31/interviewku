package com.capstone.interviewku.ui.activities.changepassword

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
}