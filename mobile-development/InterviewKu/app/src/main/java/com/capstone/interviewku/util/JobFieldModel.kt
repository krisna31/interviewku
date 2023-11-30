package com.capstone.interviewku.util

import com.capstone.interviewku.data.network.response.JobField

data class JobFieldModel(
    val selectedId: Int,
    val jobFields: List<JobField>
)
