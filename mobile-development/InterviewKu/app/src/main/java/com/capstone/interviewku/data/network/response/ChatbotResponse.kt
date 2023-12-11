package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatbotResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("meta")
    val meta: ChatsMeta,

    @field:SerializedName("data")
    val data: List<Chat>?,
) : Parcelable

@Parcelize
data class ChatsMeta(
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
data class Chat(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("question")
    val question: String,

    @field:SerializedName("answer")
    val answer: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String?,
) : Parcelable