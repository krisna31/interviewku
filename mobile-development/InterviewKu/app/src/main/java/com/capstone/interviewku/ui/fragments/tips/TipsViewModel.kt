package com.capstone.interviewku.ui.fragments.tips

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.TipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TipsViewModel @Inject constructor(
    private val tipsRepository: TipsRepository
) : ViewModel() {
}