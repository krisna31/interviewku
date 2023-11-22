package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserIdentityModifyResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: UserIdentityModify?,
) : Parcelable

@Parcelize
data class UserIdentityModify(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("jobPositionId")
    val jobPositionId: Int?,

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
