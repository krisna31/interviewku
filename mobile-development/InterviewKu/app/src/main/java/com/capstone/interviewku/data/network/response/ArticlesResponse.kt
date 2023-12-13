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


@Parcelize
data class Article(
    @field:SerializedName("id")
    val id: String,

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