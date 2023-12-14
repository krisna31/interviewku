package com.capstone.interviewku.ui.fragments.tips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.capstone.interviewku.data.TipsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TipsViewModel @Inject constructor(
    tipsRepository: TipsRepository
) : ViewModel() {
    val articles = tipsRepository.getPagedArticles().cachedIn(viewModelScope)
}