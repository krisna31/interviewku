package com.capstone.interviewku.ui.activities.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.response.JobPositionsResponse
import com.capstone.interviewku.data.network.response.UserIdentity
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _editUserIdentityState = MutableLiveData<Result<Unit>>()
    val editUserIdentityState: LiveData<Result<Unit>>
        get() = _editUserIdentityState

    private val _jobPositionState = MutableLiveData<Result<JobPositionsResponse>>()
    val jobPositionState: LiveData<Result<JobPositionsResponse>>
        get() = _jobPositionState

    private val _userIdentity = MutableLiveData<Result<UserIdentity>>()
    val userIdentity: LiveData<Result<UserIdentity>> get() = _userIdentity

    var birthDate: String? = null

    fun getUserIdentity() {
        _userIdentity.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = userRepository.getUserIdentity()

                response.data?.let {
                    _userIdentity.value = Result.Success(it)
                } ?: run {
                    throw Exception()
                }
            } catch (e: Exception) {
                _userIdentity.value = Result.Error(SingleEvent(e))
            }
        }
    }

    fun editUserIdentity(
        firstName: String,
        lastName: String?,
        jobPositionId: Int,
        gender: String,
        dateBirth: String,
        currentCity: String
    ) = viewModelScope.launch {
        _editUserIdentityState.value = Result.Loading

        try {
            userRepository.editUserIdentity(
                firstName,
                lastName,
                jobPositionId,
                gender,
                dateBirth,
                currentCity
            )
            _editUserIdentityState.value = Result.Success(Unit)
        } catch (e: Exception) {
            _editUserIdentityState.value = Result.Error(SingleEvent(e))
        }
    }

    fun getJobPositions() = viewModelScope.launch {
        _jobPositionState.value = Result.Loading

        try {
            val result = jobRepository.getJobPositions()
            _jobPositionState.value = Result.Success(result)
        } catch (e: Exception) {
            _jobPositionState.value = Result.Error(SingleEvent(e))
        }
    }
}