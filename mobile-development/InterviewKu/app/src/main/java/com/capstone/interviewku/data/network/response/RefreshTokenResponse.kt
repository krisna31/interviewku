package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RefreshTokenResponse(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: RefreshTokenData?,
) : Parcelable

@Parcelize
data class RefreshTokenData(
    @SerializedName("accessToken")
    val accessToken: String,
) : Parcelable