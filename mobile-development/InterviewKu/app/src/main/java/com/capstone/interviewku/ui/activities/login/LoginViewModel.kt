package com.capstone.interviewku.ui.activities.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.network.response.LoginResponse
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loginState = MutableLiveData<Result<SingleEvent<LoginResponse>>>()
    val loginState: LiveData<Result<SingleEvent<LoginResponse>>>
        get() = _loginState

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginState.value = Result.Loading

        try {
            val response = authRepository.login(email, password)

            response.data?.let {
                authRepository.saveLoginData(it)
            } ?: run {
                throw Exception()
            }

            _loginState.value = Result.Success(SingleEvent(response))
        } catch (e: Exception) {
            _loginState.value = Result.Error(SingleEvent(e))
        }
    }
}