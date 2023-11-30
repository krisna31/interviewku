package com.capstone.interviewku.data.network.request

import java.io.File

data class InterviewAnswer(
    val question: String,
    val questionOrder: Int,
    var audio: File? = null,
    var retryAttempt: Int = 0,
)
