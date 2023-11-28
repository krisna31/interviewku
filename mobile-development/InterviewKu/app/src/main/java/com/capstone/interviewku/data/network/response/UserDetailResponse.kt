package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: User?,
) : Parcelable

@Parcelize
data class User(
    @SerializedName("id")
    val id: String,

    @SerializedName("firstName")
    val firstname: String,

    @SerializedName("lastName")
    val lastname: String?,

    @SerializedName("email")
    val email: String,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("updatedAt")
    val updatedAt: String?,
) : Parcelable

