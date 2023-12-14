package com.capstone.interviewku.util

import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.interviewku.BuildConfig
import com.capstone.interviewku.R
import com.capstone.interviewku.data.network.response.BaseResponse
import com.google.gson.Gson
import retrofit2.HttpException

object Extensions {
    val Context.dataStore by preferencesDataStore(Constants.DATASTORE_NAME)

    fun Context.hideKeyboard(view: View) =
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(view.windowToken, 0)
        }

    fun Context.isPermissionGranted(permission: String) =
        checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    fun Exception.handleHttpException(context: Context): String {
        if (BuildConfig.DEBUG) {
            printStackTrace()
        }

        return if (this is HttpException) {
            val message = this.response()?.errorBody()?.string()?.let { errorBody ->
                try {
                    Gson().fromJson(errorBody, BaseResponse::class.java).message
                } catch (e: Exception) {
                    context.getString(R.string.unexpected_error)
                }
            } ?: run {
                context.getString(R.string.unexpected_error)
            }

            message
        } else {
            context.getString(R.string.unexpected_error)
        }
    }
}