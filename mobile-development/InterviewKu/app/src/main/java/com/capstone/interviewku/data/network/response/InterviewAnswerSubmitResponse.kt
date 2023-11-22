package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InterviewAnswerSubmitResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: InterviewAnswerSubmitData?,
) : Parcelable

@Parcelize
data class InterviewAnswerSubmitData(
    @field:SerializedName("testHistoryId")
    val testHistoryId: String
) : Parcelable
