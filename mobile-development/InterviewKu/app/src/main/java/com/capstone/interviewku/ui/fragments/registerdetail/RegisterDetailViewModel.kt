package com.capstone.interviewku.ui.fragments.registerdetail

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterDetailViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository
) : ViewModel() {
}