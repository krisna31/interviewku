package com.capstone.interviewku.util

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: SingleEvent<Exception>) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}
