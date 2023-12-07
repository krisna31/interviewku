package com.capstone.interviewku.ui.fragments.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.capstone.interviewku.util.Result
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _logoutState = MutableLiveData<Result<Unit>>()
    val logoutState: LiveData<Result<Unit>>
        get() = _logoutState

    fun logout() = viewModelScope.launch {
        _logoutState.value = Result.Loading

        try {
            authRepository.logout()
            authRepository.clearLoginData()

            _logoutState.value = Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("AccountViewModel", "Error during logout", e)
            _logoutState.value = Result.Error(SingleEvent(e))
        }

    }
}