package com.capstone.interviewku.ui.activities.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _changePasswordState = MutableLiveData<Result<Unit>>()
    val changePasswordState: LiveData<Result<Unit>>
        get() = _changePasswordState

    fun changePassword(oldPassword: String, newPassword: String) = viewModelScope.launch {
        _changePasswordState.value = Result.Loading

        try {
            authRepository.changePassword(oldPassword, newPassword)
            _changePasswordState.value = Result.Success(Unit)
        } catch (e: Exception) {
            _changePasswordState.value = Result.Error(SingleEvent(e))
        }
    }
}