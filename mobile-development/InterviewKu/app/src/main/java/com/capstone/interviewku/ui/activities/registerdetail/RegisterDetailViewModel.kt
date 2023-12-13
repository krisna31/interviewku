package com.capstone.interviewku.ui.activities.registerdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.response.JobPositionsResponse
import com.capstone.interviewku.data.network.response.UserIdentityResponse
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterDetailViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _addUserIdentityState = MutableLiveData<Result<UserIdentityResponse>>()
    val addUserIdentityState: LiveData<Result<UserIdentityResponse>>
        get() = _addUserIdentityState

    private val _jobPositionState = MutableLiveData<Result<JobPositionsResponse>>()
    val jobPositionState: LiveData<Result<JobPositionsResponse>>
        get() = _jobPositionState

    var birthDate: String? = null

    fun addUserIdentity(
        jobPositionId: Int,
        gender: String,
        dateBirth: String,
        currentCity: String
    ) = viewModelScope.launch {
        _addUserIdentityState.value = Result.Loading

        try {
            val response = userRepository.addUserIdentity(
                jobPositionId,
                gender,
                dateBirth,
                currentCity
            )

            userRepository.setHasUserIdentity(true)
            _addUserIdentityState.value = Result.Success(response)
        } catch (e: Exception) {
            _addUserIdentityState.value = Result.Error(SingleEvent(e))
        }
    }

    fun getJobPositions() = viewModelScope.launch {
        _jobPositionState.value = Result.Loading

        try {
            _jobPositionState.value = Result.Success(jobRepository.getJobPositions())
        } catch (e: Exception) {
            _jobPositionState.value = Result.Error(SingleEvent(e))
        }
    }
}