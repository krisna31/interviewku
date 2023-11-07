package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BaseResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,
) : Parcelable