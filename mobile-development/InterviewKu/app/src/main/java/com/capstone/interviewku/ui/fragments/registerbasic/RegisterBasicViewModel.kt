package com.capstone.interviewku.ui.fragments.registerbasic

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterBasicViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
}