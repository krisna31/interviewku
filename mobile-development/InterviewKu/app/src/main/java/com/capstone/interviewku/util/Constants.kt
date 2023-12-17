package com.capstone.interviewku.util

object Constants {
    // audio recording format constants
    const val AUDIO_FORMAT_SUFFIX = ".amr"
    const val AUDIO_SAMPLE_RATE = 16000

    // data sources constants
    const val DATABASE_NAME = "db_interviewku"
    const val DATASTORE_NAME = "interviewku_preferences"

    // delay constants
    const val SPLASH_SCREEN_DELAY = 3000L

    // paging constants
    const val ARTICLE_PAGE_SIZE = 10
    const val CHAT_PAGE_SIZE = 10
    const val INTERVIEW_HISTORY_PAGE_SIZE = 10

    // text to speech constants
    const val GOOGLE_TTS_PACKAGE_NAME = "com.google.android.tts"
    const val TTS_EXTRA_VOICE_INDONESIA = "ind-idn"

    // other constants
    const val BIRTHDATE_FORMAT = "yyyy-MM-dd"
    const val DATE_FORMAT = "dd MMMM yyyy HH:mm"
    const val INDONESIA_LANGUAGE_TAG = "id-ID"
    const val INTERVIEW_MAX_DURATION_SECONDS = 2 * 60
    const val INTERVIEW_PERFORMANCE_COUNT = 7
    const val SWIPE_REFRESH_LAYOUT_TRIGGER_DISTANCE = 400
    const val TIPS_HOME_COUNT = 10
}