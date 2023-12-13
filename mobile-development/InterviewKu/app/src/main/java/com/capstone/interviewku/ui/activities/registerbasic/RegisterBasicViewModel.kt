package com.capstone.interviewku.ui.activities.registerbasic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.UserRepository
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
    private val _registerState = MutableLiveData<Result<UserRegisterResponse>>()
    val registerState: LiveData<Result<UserRegisterResponse>>
        get() = _registerState

    fun register(email: String, password: String, firstName: String, lastName: String?) =
        viewModelScope.launch {
            _registerState.value = Result.Loading

            try {
                val registerResponse = userRepository.register(email, password, firstName, lastName)
                val loginResponse = authRepository.login(email, password)

                loginResponse.data?.let { loginData ->
                    authRepository.saveLoginData(loginData)
                } ?: run {
                    throw Exception()
                }

                _registerState.value = Result.Success(registerResponse)
            } catch (e: Exception) {
                _registerState.value = Result.Error(SingleEvent(e))
            }
        }
}