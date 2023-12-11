package com.capstone.interviewku.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.TipsRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.response.JobField
import com.capstone.interviewku.util.Constants
import com.capstone.interviewku.util.HomeScreenModel
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository,
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository,
    private val tipsRepository: TipsRepository
) : ViewModel() {
    private val _allDataState = MutableLiveData<Result<HomeScreenModel>>()
    val allDataState: LiveData<Result<HomeScreenModel>>
        get() = _allDataState

    private val _selectedJobField = MutableLiveData<JobField?>(null)
    val selectedJobField: LiveData<JobField?>
        get() = _selectedJobField

    fun loadAllData() = viewModelScope.launch {
        _allDataState.value = Result.Loading

        try {
            val userIdentityResponse = userRepository.getUserIdentity()
            val jobFieldsResponse = jobRepository.getJobFields()
            val interviewHistoryResponse =
                interviewRepository.getInterviewResults(1, Constants.INTERVIEW_PERFORMANCE_COUNT)
            val articlesResponse = tipsRepository.getArticles(1, Constants.TIPS_HOME_COUNT)

            if (userIdentityResponse.data != null
                && jobFieldsResponse.data != null
                && interviewHistoryResponse.data != null
                && articlesResponse.data != null
            ) {
                _allDataState.value = Result.Success(
                    HomeScreenModel(
                        userIdentityResponse.data,
                        jobFieldsResponse.data.jobFields,
                        interviewHistoryResponse.data,
                        articlesResponse.data,
                    )
                )
            } else {
                throw Exception()
            }

        } catch (e: Exception) {
            _allDataState.value = Result.Error(SingleEvent(e))
        }
    }

    fun setInterestJobField(jobField: JobField?) {
        _selectedJobField.value = jobField
    }
}