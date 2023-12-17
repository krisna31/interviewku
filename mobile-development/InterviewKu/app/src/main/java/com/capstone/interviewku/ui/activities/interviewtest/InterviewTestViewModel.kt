package com.capstone.interviewku.ui.activities.interviewtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.interviewku.data.InterviewRepository
import com.capstone.interviewku.data.JobRepository
import com.capstone.interviewku.data.UserRepository
import com.capstone.interviewku.data.network.request.InterviewAnswer
import com.capstone.interviewku.data.network.response.InterviewAnswerSubmitResponse
import com.capstone.interviewku.data.network.response.InterviewQuestionsData
import com.capstone.interviewku.data.network.response.InterviewQuestionsResponse
import com.capstone.interviewku.data.network.response.InterviewResultResponse
import com.capstone.interviewku.util.JobFieldModel
import com.capstone.interviewku.util.Result
import com.capstone.interviewku.util.SingleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.scheduleAtFixedRate

@HiltViewModel
class InterviewTestViewModel @Inject constructor(
    private val interviewRepository: InterviewRepository,
    private val jobRepository: JobRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _currentDuration = MutableLiveData(0)
    val currentDuration: LiveData<Int>
        get() = _currentDuration

    private val _currentQuestion = MutableLiveData("")
    val currentQuestion: LiveData<String>
        get() = _currentQuestion

    private var currentQuestionOrder = 0

    val isEndOfInterview
        get() = currentQuestionOrder == interviewData?.questions?.size

    private val _isRecording = MutableLiveData(false)
    val isRecording: LiveData<Boolean>
        get() = _isRecording

    private val _endInterviewState = MutableLiveData<Result<InterviewResultResponse>>()
    val endInterviewState: LiveData<Result<InterviewResultResponse>>
        get() = _endInterviewState

    private val _prepareInterviewState = MutableLiveData<Result<JobFieldModel>>()
    val prepareInterviewState: LiveData<Result<JobFieldModel>>
        get() = _prepareInterviewState

    private val _startInterviewState = MutableLiveData<Result<InterviewQuestionsResponse>>()
    val startInterviewState: LiveData<Result<InterviewQuestionsResponse>>
        get() = _startInterviewState

    private val _submitInterviewState = MutableLiveData<Result<InterviewAnswerSubmitResponse>>()
    val submitInterviewState: LiveData<Result<InterviewAnswerSubmitResponse>>
        get() = _submitInterviewState

    private val interviewAnswers = mutableListOf<InterviewAnswer>()
    private var interviewData: InterviewQuestionsData? = null

    private var timer: Timer? = null

    private var jobFieldId: Int? = null

    fun endInterviewSession() = viewModelScope.launch {
        _endInterviewState.value = Result.Loading

        try {
            interviewData?.let {
                _endInterviewState.value = Result.Success(
                    interviewRepository.endInterviewSession(it.interviewId, it.token)
                )
            } ?: run {
                throw Exception()
            }
        } catch (e: Exception) {
            _endInterviewState.value = Result.Error(SingleEvent(e))
        }
    }

    fun moveToNextQuestion() {
        interviewData?.let { interviewQuestionsData ->
            currentQuestionOrder += 1
            _currentDuration.value = 0
            _currentQuestion.value =
                interviewQuestionsData.questions[currentQuestionOrder - 1].question

            interviewAnswers.add(
                InterviewAnswer(
                    question = interviewQuestionsData.questions[currentQuestionOrder - 1].question,
                    questionOrder = interviewQuestionsData.questions[currentQuestionOrder - 1].questionOrder,
                )
            )
        }
    }

    fun prepareInterview() = viewModelScope.launch {
        _prepareInterviewState.value = Result.Loading

        try {
            val userDetailResponse = userRepository.getUserIdentity()
            val jobFieldsResponse = jobRepository.getJobFields()

            jobFieldsResponse.data?.let { jobFieldsResponseData ->
                _prepareInterviewState.value = Result.Success(
                    JobFieldModel(
                        userDetailResponse.data?.jobFieldId ?: -1,
                        jobFieldsResponseData.jobFields.sortedBy { it.name }
                    )
                )
            } ?: run {
                throw Exception()
            }
        } catch (e: Exception) {
            _prepareInterviewState.value = Result.Error(SingleEvent(e))
        }
    }

    fun repeatQuestion() {
        _currentDuration.value = 0
        interviewAnswers[currentQuestionOrder - 1].audio = null
        interviewAnswers[currentQuestionOrder - 1].retryAttempt += 1
    }

    fun sendAnswer() = viewModelScope.launch {
        _submitInterviewState.value = Result.Loading

        try {
            interviewData?.let { interviewQuestionsData ->
                interviewAnswers[currentQuestionOrder - 1].apply {
                    audio?.let { audioFile ->
                        val response = interviewRepository.sendInterviewAnswer(
                            interviewQuestionsData.interviewId,
                            interviewQuestionsData.token,
                            audioFile,
                            retryAttempt,
                            question,
                            questionOrder
                        )

                        _submitInterviewState.value = Result.Success(response)
                    } ?: run {
                        throw Exception()
                    }
                }
            } ?: run {
                throw Exception()
            }
        } catch (e: Exception) {
            _submitInterviewState.value = Result.Error(SingleEvent(e))
        }
    }

    fun setAnswer(audio: File) {
        interviewAnswers[currentQuestionOrder - 1].audio = audio
    }

    fun setJobFieldId(jobFieldId: Int) {
        this.jobFieldId = jobFieldId
    }

    fun startInterviewSession() = viewModelScope.launch {
        _startInterviewState.value = Result.Loading

        try {
            jobFieldId?.let { id ->
                val response = interviewRepository.startInterviewTestSession(id)

                response.data?.let { interviewQuestionsData ->
                    interviewData = interviewQuestionsData.copy(
                        questions = interviewQuestionsData.questions.sortedBy { it.questionOrder }
                    )
                } ?: run {
                    throw Exception()
                }

                _startInterviewState.value = Result.Success(response)
            }
        } catch (e: Exception) {
            _startInterviewState.value = Result.Error(SingleEvent(e))
        }
    }

    fun startRecording() {
        _isRecording.value = true
        timer = Timer().apply {
            scheduleAtFixedRate(1000, 1000) {
                _currentDuration.postValue(_currentDuration.value?.plus(1))
            }
        }
    }

    fun stopRecording() {
        _isRecording.value = false
        timer?.cancel()
        timer = null
    }
}