package com.capstone.interviewku.data.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticlesResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("meta")
    val meta: ArticlesMeta?,

    @field:SerializedName("data")
    val data: List<Article>?,
) : Parcelable

@Parcelize
data class ArticlesMeta(
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


