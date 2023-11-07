package com.capstone.interviewku.util

import android.content.Context
import android.content.pm.PackageManager

object Extensions {
    fun Context.isPermissionGranted(permission: String) =
        checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    fun Context.isPermissionsGranted(vararg permissions: String) =
        permissions.all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
}