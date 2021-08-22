package com.setianjay.mybroadcastreceiver

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object PermissionManager {
    fun checkPermission(activity: Activity, permission: String, requestCode: Int){
        if (ActivityCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        }
    }
}