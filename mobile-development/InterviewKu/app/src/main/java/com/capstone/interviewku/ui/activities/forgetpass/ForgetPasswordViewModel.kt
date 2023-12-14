package com.capstone.interviewku.ui.activities.forgetpass

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
class ForgetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _passwordResetState = MutableLiveData<Result<BaseResponse>>()
    val passwordResetState: LiveData<Result<BaseResponse>>
        get() = _passwordResetState

    fun requestPasswordReset(email: String) = viewModelScope.launch {
        _passwordResetState.value = Result.Loading

        try {
            _passwordResetState.value = Result.Success(
                authRepository.requestPasswordReset(email)
            )
        } catch (e: Exception) {
            _passwordResetState.value = Result.Error(SingleEvent(e))
        }
    }
}