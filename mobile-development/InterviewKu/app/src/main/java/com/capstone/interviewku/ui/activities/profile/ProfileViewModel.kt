package com.capstone.interviewku.ui.activities.profile

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository
) : ViewModel() {
}