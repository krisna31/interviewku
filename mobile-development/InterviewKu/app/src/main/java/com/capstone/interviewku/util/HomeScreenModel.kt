package com.capstone.interviewku.util

import com.capstone.interviewku.data.network.response.Article
import com.capstone.interviewku.data.network.response.InterviewHistoryData
import com.capstone.interviewku.data.network.response.JobField
import com.capstone.interviewku.data.network.response.UserIdentity

data class HomeScreenModel(
    val userIdentity: UserIdentity,
    val jobFields: List<JobField>,
    val interviewPerformance: List<InterviewHistoryData>,
    val tipsArticles: List<Article>,
)
