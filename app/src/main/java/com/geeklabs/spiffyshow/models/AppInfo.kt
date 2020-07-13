package com.geeklabs.spiffyshow.models

import android.os.Build
import com.geeklabs.spiffyshow.BuildConfig

data class AppInfo(
    val version: String = BuildConfig.VERSION_NAME,
    val deviceModel: String = Build.MODEL,
    var machineName: String = "",
    val platformVersion: String = Build.VERSION.RELEASE
)