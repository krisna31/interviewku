package com.capstone.interviewku.ui.activities.recoverpass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.network.response.BaseResponse
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _recoverPasswordState = MutableLiveData<Result<BaseResponse>>()
    val recoverPasswordState: LiveData<Result<BaseResponse>>
        get() = _recoverPasswordState

    private val _verifyPasswordResetState = MutableLiveData<Result<BaseResponse>>()
    val verifyPasswordResetState: LiveData<Result<BaseResponse>>
        get() = _verifyPasswordResetState

    fun recoverPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            _recoverPasswordState.value = Result.Loading
            try {
                _recoverPasswordState.value = Result.Success(
                    authRepository.recoverPassword(email, newPassword)
                )
            } catch (e: Exception) {
                _recoverPasswordState.value = Result.Error(SingleEvent(e))
            }
        }
    }

    fun verifyPasswordReset(email: String, otpCode: String) {
        viewModelScope.launch {
            _verifyPasswordResetState.value = Result.Loading
            try {
                _verifyPasswordResetState.value = Result.Success(
                    authRepository.verifyPasswordReset(email, otpCode)
                )
            } catch (e: Exception) {
                _verifyPasswordResetState.value = Result.Error(SingleEvent(e))
            }
        }
    }
}
