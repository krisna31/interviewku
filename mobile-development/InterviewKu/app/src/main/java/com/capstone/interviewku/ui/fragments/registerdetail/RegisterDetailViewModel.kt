package com.capstone.interviewku.ui.fragments.registerdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.response.JobPositionsResponse
import com.capstone.interviewku.data.network.response.UserIdentityModifyResponse
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
    private val _addUserIdentityState = MutableLiveData<Result<UserIdentityModifyResponse>>()
    val addUserIdentityState: LiveData<Result<UserIdentityModifyResponse>>
        get() = _addUserIdentityState

    private val _jobPositionState = MutableLiveData<Result<JobPositionsResponse>>()
    val jobPositionState: LiveData<Result<JobPositionsResponse>>
        get() = _jobPositionState

    fun addUserIdentity(
        jobPositionId: Int,
        gender: String,
        dateBirth: String,
        currentCity: String
    ) = viewModelScope.launch {
        _addUserIdentityState.value = Result.Loading

        try {
            _addUserIdentityState.value = Result.Success(
                userRepository.addUserIdentity(
                    jobPositionId,
                    gender,
                    dateBirth,
                    currentCity
                )
            )
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