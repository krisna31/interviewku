package com.capstone.interviewku.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.TipsRepository
import com.capstone.interviewku.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tipsRepository: TipsRepository
) : ViewModel() {
}