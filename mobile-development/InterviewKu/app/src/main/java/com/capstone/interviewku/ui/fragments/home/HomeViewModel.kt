package com.capstone.interviewku.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.TipsRepository
import com.capstone.interviewku.data.UserRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tipsRepository: TipsRepository
) : ViewModel() {
}