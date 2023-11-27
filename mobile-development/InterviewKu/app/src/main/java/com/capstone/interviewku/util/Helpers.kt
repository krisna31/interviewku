package com.capstone.interviewku.util

import android.content.Context
import android.util.Patterns
import com.capstone.interviewku.R
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

    fun isPasswordValid(password: String) = Pattern.matches("[a-zA-Z].*[0-9]", password)
}