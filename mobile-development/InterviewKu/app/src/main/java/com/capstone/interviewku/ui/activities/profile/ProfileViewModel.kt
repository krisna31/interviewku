package com.capstone.interviewku.ui.activities.profile

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.UserRepository

class ProfileViewModel(
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository
) : ViewModel() {
}