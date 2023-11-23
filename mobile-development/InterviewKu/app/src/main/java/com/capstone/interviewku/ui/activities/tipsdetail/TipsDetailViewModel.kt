package com.capstone.interviewku.ui.activities.tipsdetail

import androidx.lifecycle.ViewModel
import com.capstone.interviewku.data.TipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TipsDetailViewModel @Inject constructor(
    private val tipsRepository: TipsRepository
) : ViewModel() {
}