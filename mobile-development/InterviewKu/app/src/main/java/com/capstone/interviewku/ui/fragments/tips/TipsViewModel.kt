package com.capstone.interviewku.ui.fragments.tips

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.TipsRepository
import javax.inject.Inject

class TipsViewModel @Inject constructor(
    private val tipsRepository: TipsRepository
) : ViewModel() {
}