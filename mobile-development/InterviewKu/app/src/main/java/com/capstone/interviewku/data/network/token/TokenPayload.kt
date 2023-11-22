package com.capstone.interviewku.data.network.token

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenPayload(
    @field:SerializedName("id")
    val userId: String?,

    @field:SerializedName("iat")
    val iat: Long?,
) : Parcelable
