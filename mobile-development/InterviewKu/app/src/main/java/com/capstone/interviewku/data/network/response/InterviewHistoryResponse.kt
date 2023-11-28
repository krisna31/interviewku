package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InterviewHistoryResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: InterviewHistoryData?,
) : Parcelable

@Parcelize
data class InterviewHistoryData(
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
) : Parcelable
