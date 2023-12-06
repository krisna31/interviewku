package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InterviewResultResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: InterviewResultData?,
) : Parcelable

@Parcelize
data class InterviewResultData(
    @field:SerializedName("interviewId")
    val interviewId: String,

    @field:SerializedName("mode")
    val mode: String,

    @field:SerializedName("totalQuestions")
    val totalQuestions: Int,

    @field:SerializedName("completed")
    val completed: Boolean,

    @field:SerializedName("score")
    val score: Double?,

    @field:SerializedName("totalDuration")
    val totalDuration: Double?,

    @field:SerializedName("feedback")
    val feedback: String,

    @field:SerializedName("jobFieldName")
    val jobFieldName: String,

    @field:SerializedName("startedAt")
    val startedAt: String,

    @field:SerializedName("answers")
    val answers: List<InterviewResultItem>,
) : Parcelable

@Parcelize
data class InterviewResultItem(
    @field:SerializedName("question")
    val question: String,

    @field:SerializedName("jobFieldName")
    val jobFieldName: String,

    @field:SerializedName("questionOrder")
    val questionOrder: Int,

    @field:SerializedName("userAnswer")
    val userAnswer: String?,

    @field:SerializedName("audioUrl")
    val audioUrl: String?,

    @field:SerializedName("score")
    val score: Double?,

    @field:SerializedName("duration")
    val duration: Double?,

    @field:SerializedName("retryAttempt")
    val retryAttempt: Int?,

    @field:SerializedName("feedback")
    val feedback: String,
) : Parcelable
