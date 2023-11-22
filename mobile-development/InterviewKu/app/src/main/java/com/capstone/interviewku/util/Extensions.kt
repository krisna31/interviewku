package com.capstone.interviewku.util

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.interviewku.BuildConfig
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.BaseResponse
import com.capstone.interviewku.data.preferences.AppPreferences
import com.google.gson.Gson
import retrofit2.HttpException

object Extensions {
    val Context.dataStore by preferencesDataStore(AppPreferences.DATASTORE_NAME)

    fun Context.isPermissionGranted(permission: String) =
        checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    fun Context.isPermissionsGranted(vararg permissions: String) =
        permissions.all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }

    fun Exception.handleHttpException(context: Context) {
        if (BuildConfig.DEBUG) {
            printStackTrace()
        }

        if (this is HttpException) {
            val message = this.response()?.errorBody()?.string()?.let { errorBody ->
                try {
                    Gson().fromJson(errorBody, BaseResponse::class.java).message
                } catch (e: Exception) {
                    context.getString(R.string.unexpected_error)
                }
            } ?: run {
                context.getString(R.string.unexpected_error)
            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.unexpected_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}