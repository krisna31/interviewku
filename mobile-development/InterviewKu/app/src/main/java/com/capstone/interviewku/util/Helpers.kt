package com.capstone.interviewku.util

import android.content.Context
import android.text.format.DateUtils
import android.util.Patterns
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.Article
import com.capstone.interviewku.data.network.response.ArticleItem
import com.capstone.interviewku.data.room.entity.ArticleEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Helpers {
    fun articleEntityToArticleResponse(articleEntity: ArticleEntity) = Article(
        id = articleEntity.id,
        title = articleEntity.title,
        author = articleEntity.author,
        subtitle = articleEntity.subtitle,
        content = articleEntity.content,
        coverImgUrl = articleEntity.coverImgUrl,
        createdAt = articleEntity.createdAt,
        updatedAt = articleEntity.updatedAt
    )

    fun articleResponseToArticleEntity(article: ArticleItem) = ArticleEntity(
        id = article.id,
        title = article.title,
        author = article.author,
        subtitle = article.subtitle,
        content = article.content,
        coverImgUrl = article.coverImgUrl,
        createdAt = article.createdAt,
        updatedAt = article.updatedAt
    )

    fun getGenders(context: Context): MutableList<SpinnerModel> {
        val labelArray = context.resources.getStringArray(R.array.gender_label)
        val valueArray = context.resources.getStringArray(R.array.gender)
        val result = mutableListOf<SpinnerModel>()

        labelArray.forEachIndexed { index, s ->
            result.add(SpinnerModel(valueArray[index], s))
        }

        return result
    }

    fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordValid(password: String) =
        password.length >= 8
                && password.contains(Regex("[a-z]"))
                && password.contains(Regex("[A-Z]"))
                && password.contains(Regex("[0-9]"))

    fun secondsToString(seconds: Int): String {
        return DateUtils.formatElapsedTime(seconds.toLong())
    }

    fun tzTimeStringToDate(dateString: String): Date? {
        return try {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT).run {
                timeZone = TimeZone.getTimeZone("GMT")
                parse(dateString)
            }
        } catch (e: Exception) {
            null
        }
    }
}