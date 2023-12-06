package com.capstone.interviewku.ui.activities.recoverpass

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
}