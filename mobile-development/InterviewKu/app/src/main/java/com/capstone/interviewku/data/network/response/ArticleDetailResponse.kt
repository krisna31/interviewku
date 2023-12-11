package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleDetailResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: Article?,
) : Parcelable

@Parcelize
data class Article(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("subtitle")
    val subtitle: String,

    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("source")
    val source: String,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("coverImgUrl")
    val coverImgUrl: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String?,
) : Parcelable
