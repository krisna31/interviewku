package com.capstone.interviewku.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(AppPreferences.DATASTORE_NAME)

class AppPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val accessTokenPreferencesKey = stringPreferencesKey("access_token_key")
    private val refreshTokenPreferencesKey = stringPreferencesKey("refresh_token_key")

    fun getAccessToken() = dataStore.data.map {
        it[accessTokenPreferencesKey]
    }

    fun getRefreshToken() = dataStore.data.map {
        it[refreshTokenPreferencesKey]
    }

    suspend fun setAccessToken(accessToken: String) = dataStore.edit {
        it[accessTokenPreferencesKey] = accessToken
    }

    suspend fun setRefreshToken(refreshToken: String) = dataStore.edit {
        it[refreshTokenPreferencesKey] = refreshToken
    }

    companion object {
        const val DATASTORE_NAME = "interviewku_preferences"

        @Volatile
        private var instance: AppPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>) = instance ?: synchronized(this) {
            AppPreferences(dataStore).also {
                instance = it
            }
        }
    }
}