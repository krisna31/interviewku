package com.capstone.interviewku.ui.activities.interviewtrain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.request.InterviewAnswer
import com.capstone.interviewku.data.network.response.InterviewAnswerSubmitResponse
import com.capstone.interviewku.data.network.response.InterviewQuestionsData
import com.capstone.interviewku.data.network.response.InterviewQuestionsResponse
import com.capstone.interviewku.util.JobPositionModel
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InterviewTrainViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository,
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _currentQuestionOrder = MutableLiveData(1)
    val currentQuestionOrder: LiveData<Int>
        get() = _currentQuestionOrder

    val currentAnswerIsAnswered = currentQuestionOrder.map {
        interviewAnswers[it].audio != null
    }

    private val _prepareInterviewState = MutableLiveData<Result<JobPositionModel>>()
    val prepareInterviewState: LiveData<Result<JobPositionModel>>
        get() = _prepareInterviewState

    private val _startInterviewState = MutableLiveData<Result<InterviewQuestionsResponse>>()
    val startInterviewState: LiveData<Result<InterviewQuestionsResponse>>
        get() = _startInterviewState

    private val _submitInterviewState = MutableLiveData<Result<InterviewAnswerSubmitResponse>>()
    val submitInterviewState: LiveData<Result<InterviewAnswerSubmitResponse>>
        get() = _submitInterviewState

    private val interviewAnswers = mutableListOf<InterviewAnswer>()
    private var interviewData: InterviewQuestionsData? = null

    private fun sendInterviewAnswer(
        audio: File,
        jobPositionName: String,
        retryAttempt: Int,
        question: String,
        questionOrder: Int
    ) = viewModelScope.launch {
        _submitInterviewState.value = Result.Loading

        try {
            interviewData?.let { interviewQuestionsData ->
                val response = interviewRepository.sendInterviewAnswer(
                    interviewQuestionsData.token,
                    audio,
                    jobPositionName,
                    retryAttempt,
                    question,
                    questionOrder
                )

                _submitInterviewState.value = Result.Success(response)
            } ?: run {
                throw Exception()
            }
        } catch (e: Exception) {
            _submitInterviewState.value = Result.Error(SingleEvent(e))
        }
    }

    fun moveToNextQuestion() {
        _currentQuestionOrder.value = _currentQuestionOrder.value?.plus(1)
    }

    fun prepareInterview() = viewModelScope.launch {
        _prepareInterviewState.value = Result.Loading

        try {
            val userDetailResponse = userRepository.getUserIdentity()
            val jobPositionsResponse = jobRepository.getJobPositions()

            userDetailResponse.data?.let { userIdentity ->
                jobPositionsResponse.data?.let { jobPositionsResponseData ->
                    _prepareInterviewState.value = Result.Success(
                        JobPositionModel(
                            userIdentity.jobPositionId ?: -1,
                            jobPositionsResponseData.jobPositions
                        )
                    )
                } ?: run {
                    throw Exception()
                }
            } ?: run {
                throw Exception()
            }
        } catch (e: Exception) {
            _prepareInterviewState.value = Result.Error(SingleEvent(e))
        }
    }

    fun setAnswer(audio: File) {
        currentQuestionOrder.value?.let {
            interviewAnswers[it].audio = audio
        }
    }

    fun sendAnswer() {
        currentQuestionOrder.value?.let {
            interviewAnswers[it].apply {
                audio?.let { audioFile ->
                    sendInterviewAnswer(
                        audioFile,
                        jobPositionName,
                        retryAttempt,
                        question,
                        questionOrder
                    )
                }
            }
        }
    }

    fun startInterviewSession() = viewModelScope.launch {
        _startInterviewState.value = Result.Loading

        try {
            val response = interviewRepository.startInterviewTrainSession()

            response.data?.let {
                interviewData = it
                interviewAnswers.add(
                    InterviewAnswer(
                        jobPositionName = "",
                        question = it.questions[0].question,
                        questionOrder = it.questions[0].questionOrder,
                    )
                )
            } ?: run {
                throw Exception()
            }

            _startInterviewState.value = Result.Success(response)

        } catch (e: Exception) {
            _startInterviewState.value = Result.Error(SingleEvent(e))
        }
    }
}