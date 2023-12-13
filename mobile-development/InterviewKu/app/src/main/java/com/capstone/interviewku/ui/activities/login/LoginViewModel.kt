package com.capstone.interviewku.ui.activities.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.response.LoginResponse
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _loginState = MutableLiveData<Result<SingleEvent<LoginResponse>>>()
    val loginState: LiveData<Result<SingleEvent<LoginResponse>>>
        get() = _loginState

    suspend fun isHasUserIdentity() = userRepository.isHasUserIdentity()

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginState.value = Result.Loading

        try {
            val loginResponse = authRepository.login(email, password)

            loginResponse.data?.let {
                authRepository.saveLoginData(it)
            } ?: run {
                throw Exception()
            }

            try {
                userRepository.getUserIdentity()
                userRepository.setHasUserIdentity(true)
            } catch (e: HttpException) {
                if (e.code() != 404) {
                    // user identity not found -> 404 from API, else is other errors
                    throw e
                }
            }

            _loginState.value = Result.Success(SingleEvent(loginResponse))
        } catch (e: Exception) {
            _loginState.value = Result.Error(SingleEvent(e))
        }
    }
}