package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InterviewQuestionsResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: InterviewQuestionsData?,
) : Parcelable

@Parcelize
data class InterviewQuestionsData(
    @field:SerializedName("interviewId")
    val interviewId: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("questions")
    val questions: List<InterviewQuestion>
) : Parcelable

@Parcelize
data class InterviewQuestion(
    @field:SerializedName("question")
    val question: String,

    @field:SerializedName("jobFieldName")
    val jobFieldName: String,

    @field:SerializedName("questionOrder")
    val questionOrder: Int,
) : Parcelable
