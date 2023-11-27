package com.capstone.interviewku.util

import com.capstone.interviewku.data.network.response.JobPosition

data class JobPositionModel(
    val selectedId: Int,
    val jobPositions: List<JobPosition>
)
