package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserIdentityResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: UserIdentity?,
) : Parcelable

@Parcelize
data class UserIdentity(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("jobPositionId")
    val jobPositionId: Int?,

    @field:SerializedName("jobPositionName")
    val jobPositionName: String?,

    @field:SerializedName("gender")
    val gender: String?,

    @field:SerializedName("currentCity")
    val currentCity: String?,

    @field:SerializedName("dateBirth")
    val dateBirth: String?,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String?,
) : Parcelable
