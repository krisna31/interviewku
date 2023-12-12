package com.capstone.interviewku.ui.activities.recoverpass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.util.SingleEvent
import com.capstone.interviewku.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _recoverPasswordState = MutableLiveData<Result<Unit>>()
    val recoverPasswordState: LiveData<Result<Unit>>
        get() = _recoverPasswordState

    private val _verifyPasswordResetState = MutableLiveData<Result<Unit>>()
    val verifyPasswordResetState: LiveData<Result<Unit>>
        get() = _verifyPasswordResetState

    fun recoverPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            _recoverPasswordState.value = Result.Loading
            try {
                authRepository.recoverPassword(email, newPassword)
                _recoverPasswordState.value = Result.Success(Unit)
            } catch (e: Exception) {
                _recoverPasswordState.value = Result.Error(SingleEvent(e))
            }
        }
    }

    fun verifyPasswordReset(email: String, otpCode: String) {
        viewModelScope.launch {
            _verifyPasswordResetState.value = Result.Loading
            try {
                authRepository.verifyPasswordReset(email, otpCode)
                _verifyPasswordResetState.value = Result.Success(Unit)
            } catch (e: Exception) {
                _verifyPasswordResetState.value = Result.Error(SingleEvent(e))
            }
        }
    }
}
