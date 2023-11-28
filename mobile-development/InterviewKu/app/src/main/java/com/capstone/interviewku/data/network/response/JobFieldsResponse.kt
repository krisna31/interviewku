package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobFieldsResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: JobFieldsResponseData?,
) : Parcelable

@Parcelize
data class JobFieldsResponseData(
    @field:SerializedName("jobFields")
    val jobFields: List<JobField>
) : Parcelable

@Parcelize
data class JobField(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String?,
) : Parcelable
