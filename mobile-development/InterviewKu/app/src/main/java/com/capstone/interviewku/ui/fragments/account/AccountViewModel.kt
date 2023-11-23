package com.capstone.interviewku.ui.fragments.account

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
}