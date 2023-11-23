package com.capstone.interviewku.ui.activities.tipsdetail

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.TipsRepository
import javax.inject.Inject

class TipsDetailViewModel @Inject constructor(
    private val tipsRepository: TipsRepository
) : ViewModel() {
}