package com.capstone.interviewku.util

import android.content.Context
import android.text.format.DateUtils
import android.util.Patterns
import com.capstone.interviewku.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern

object Helpers {
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

    fun isPasswordValid(password: String) = Pattern.matches("[a-zA-Z0-9].*", password)

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