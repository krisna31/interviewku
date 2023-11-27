package com.capstone.interviewku.util

data class SpinnerModel(
    val value: String,
    val label: String,
) {
    override fun toString(): String {
        return label
    }
}