package com.geeklabs.spiffyshow.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class StringUtil @Inject constructor(val context: Context) {
    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}