package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRegisterResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: UserRegisterData?,
) : Parcelable

@Parcelize
data class UserRegisterData(
    @SerializedName("userId")
    val userId: String,
) : Parcelable