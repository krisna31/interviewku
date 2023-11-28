package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobPositionsResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: JobPositionsResponseData?,
) : Parcelable

@Parcelize
data class JobPositionsResponseData(
    @field:SerializedName("jobPositions")
    val jobPositions: List<JobPosition>
) : Parcelable

@Parcelize
data class JobPosition(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String?,
) : Parcelable
