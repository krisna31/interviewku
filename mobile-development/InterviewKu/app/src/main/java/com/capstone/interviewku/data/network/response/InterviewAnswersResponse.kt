package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InterviewAnswersResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: InterviewAnswersData?,
) : Parcelable

@Parcelize
data class InterviewAnswersData(
    @field:SerializedName("question")
    val question: String,

    @field:SerializedName("answers")
    val answers: List<InterviewAnswer>
) : Parcelable

@Parcelize
data class InterviewAnswer(
    @field:SerializedName("answer")
    val answer: String
) : Parcelable
