package com.capstone.interviewku.util

import android.util.Patterns
import java.util.regex.Pattern

object Helpers {
    fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordValid(password: String) = Pattern.matches("[a-zA-Z].*[0-9]", password)
}