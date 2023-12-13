package com.capstone.interviewku.util

open class SingleEvent<out T>(
    private val data: T
) {
    @Suppress("MemberVisibilityCanBePrivate")
    var isHandled = false
        private set

    fun getData(): T? {
        return if (isHandled) {
            null
        } else {
            isHandled = true
            data
        }
    }

    fun peekData(): T = data
}