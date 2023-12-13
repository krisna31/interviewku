package com.capstone.interviewku.ui.fragments.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.AuthRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.response.User
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _logoutState = MutableLiveData<Result<Unit>>()
    val logoutState: LiveData<Result<Unit>>
        get() = _logoutState

    private val _getUserState = MutableLiveData<Result<User>>()
    val getUserState: LiveData<Result<User>>
        get() = _getUserState

    fun logout() = viewModelScope.launch {
        _logoutState.value = Result.Loading

        try {
            try {
                authRepository.logout()
            } catch (httpException: HttpException) {
                if (httpException.code() !in 400..500) {
                    throw httpException
                }
            }

            authRepository.clearLoginData()

            _logoutState.value = Result.Success(Unit)
        } catch (e: Exception) {
            _logoutState.value = Result.Error(SingleEvent(e))
        }

    }

    fun getUser() = viewModelScope.launch {
        _getUserState.value = Result.Loading

        try {
            val userResult = userRepository.getUser()
            userResult.data?.let { userData ->
                _getUserState.value = Result.Success(userData)
            } ?: run {
                throw Exception("")
            }
        } catch (e: Exception) {
            _getUserState.value = Result.Error(SingleEvent(e))
        }
    }
}