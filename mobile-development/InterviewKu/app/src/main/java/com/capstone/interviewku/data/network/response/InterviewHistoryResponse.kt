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

    @field:SerializedName("meta")
    val meta: InterviewHistoryMeta?,

    @field:SerializedName("data")
    val data: List<InterviewHistoryData>?,
) : Parcelable

@Parcelize
data class InterviewHistoryMeta(
    @field:SerializedName("count")
    val count: Int,

    @field:SerializedName("currentPage")
    val currentPage: Int,

    @field:SerializedName("totalData")
    val totalData: Int,

    @field:SerializedName("nextUrl")
    val nextUrl: String?,

    @field:SerializedName("previousUrl")
    val previousUrl: String?,

    @field:SerializedName("firstPageUrl")
    val firstPageUrl: String,

    @field:SerializedName("lastPageUrl")
    val lastPageUrl: String,

    @field:SerializedName("limit")
    val limit: Int,
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
    val score: Int?,

    @field:SerializedName("totalDuration")
    val totalDuration: Double?,

    @field:SerializedName("jobFieldName")
    val jobFieldName: String,

    @field:SerializedName("feedback")
    val feedback: String?,

    @field:SerializedName("startedAt")
    val startedAt: String,
) : Parcelable
