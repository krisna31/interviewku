package com.capstone.interviewku.ui.fragments.registerbasic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.response.LoginData
import com.capstone.interviewku.data.network.response.LoginResponse
import com.capstone.interviewku.data.network.response.UserRegisterResponse
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterBasicViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableLiveData<Result<SingleEvent<LoginResponse>>>()
    val loginState: LiveData<Result<SingleEvent<LoginResponse>>>
        get() = _loginState

    private val _registerState = MutableLiveData<Result<UserRegisterResponse>>()
    val registerState: LiveData<Result<UserRegisterResponse>>
        get() = _registerState

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginState.value = Result.Loading

        try {
            _loginState.value = Result.Success(SingleEvent(authRepository.login(email, password)))
        } catch (e: Exception) {
            _loginState.value = Result.Error(SingleEvent(e))
        }
    }

    fun register(email: String, password: String, firstName: String, lastName: String) =
        viewModelScope.launch {
            try {
                _registerState.value = Result.Loading
                _registerState.value = Result.Success(
                    userRepository.register(email, password, firstName, lastName)
                )
            } catch (e: Exception) {
                _registerState.value = Result.Error(SingleEvent(e))
            }
        }

    suspend fun saveLoginData(loginData: LoginData) {
        authRepository.saveLoginData(loginData)
    }
}