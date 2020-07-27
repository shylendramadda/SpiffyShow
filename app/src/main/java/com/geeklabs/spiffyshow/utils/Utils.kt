package com.geeklabs.spiffyshow.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.geeklabs.spiffyshow.BuildConfig
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.extensions.inflate
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.extensions.windowManager
import com.geeklabs.spiffyshow.service.AppService
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

object Utils {

    fun getCurrentTime(): Long {
        return System.currentTimeMillis()
    }

    fun formatDateTime(dateString: String, sourceFormat: String, outputFormat: String): String {
        val date = getDateFromString(dateString, sourceFormat)
        return getDateAsString(date, outputFormat)
    }

    fun getDateFromString(dateString: String, sourceFormat: String): Date {
        return SimpleDateFormat(sourceFormat, Locale.getDefault()).parse(dateString) ?: Date()
    }

    fun getDateAsString(date: Date, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(date)
    }

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun getMinutesFromSeconds(seconds: Int): Int {
        return (seconds % 3600) / 60
    }

    fun isValidLatLang(latitude: Double?, longitude: Double?): Boolean {
        return latitude != null && longitude != null && latitude != 0.0 && longitude != 0.0 &&
                latitude.toInt() in -90 until 90 && longitude.toInt() in -180 until 180
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo
            .isConnected
    }

    fun showNoInternetWarning(
        view: View?,
        context: Context
    ) {
        if (!isNetworkAvailable(context)) {
            val snackBar = Snackbar.make(
                view!!,
                context.getString(R.string.no_internet),
                Snackbar.LENGTH_INDEFINITE
            )
            snackBar.setAction(
                context.getString(R.string.connect)
            ) {
                val intent = Intent(Settings.ACTION_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            snackBar.show()
        }
    }

    fun showProgressDialog(
        activity: Activity?,
        message: String?,
        isCancellable: Boolean
    ): AlertDialog? {
        val builder = AlertDialog.Builder(activity)
        builder.setView(R.layout.layout_progress)
        builder.setCancelable(isCancellable)
        if (message != null) {
            builder.setMessage(message)
        }
        builder.show()
        return builder.create()
    }

    fun showAlertDialog(
        context: Context,
        title: String?,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        isCancellable: Boolean,
        dialogActionListener: DialogActionListener?
    ) {
        val alertDialog = AlertDialog.Builder(context)
            .create()
        val dialogView = context.inflate(R.layout.layout_alert_dialog)
        alertDialog.setView(dialogView)
        alertDialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        alertDialog?.setCancelable(isCancellable)
        if (title != null) {
            dialogView?.findViewById<TextView>(R.id.titleTV)?.text = title
        }
        dialogView?.findViewById<TextView>(R.id.messageTV)?.text = message
        dialogView?.findViewById<Button>(R.id.positiveButton)?.text = positiveButtonText
        dialogView?.findViewById<Button>(R.id.negativeButton)?.text = negativeButtonText
        if (dialogActionListener != null) {
            dialogView?.findViewById<Button>(R.id.positiveButton)?.setOnClickListener {
                alertDialog.dismiss()
                dialogActionListener.onPositiveClick(title)
            }
            dialogView?.findViewById<Button>(R.id.negativeButton)?.setOnClickListener {
                alertDialog.dismiss()
            }
        }
        alertDialog?.show()
    }

    interface DialogActionListener {
        fun onPositiveClick(title: String?)
    }

    fun appIsInForeground(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val activePackages = getActivePackages(manager)
        if (activePackages.any()) {
            return activePackages[0] == BuildConfig.APPLICATION_ID
        }
        return false
    }

    private fun getActivePackages(manager: ActivityManager): Array<String> {
        val activePackages = HashSet<String>()
        val processInfos = manager.runningAppProcesses
        if (processInfos != null) {
            for (processInfo in processInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    activePackages.addAll(processInfo.pkgList)
                }
            }
        }
        return activePackages.toTypedArray()
    }

    fun isServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (AppService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun getJsonFromAssets(context: Context): String? {
        val json: String?
        try {
            val inputStream = context.assets.open("items.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun getGridCount(context: Context, displayMetrics: DisplayMetrics): Int {
        val defaultDisplay = context.windowManager.defaultDisplay
        defaultDisplay.getMetrics(displayMetrics)
        val density = displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / density
        return (dpWidth / 200).roundToInt()
    }

    fun showHideViews(isShow: Boolean, vararg views: View) {
        views.forEach {
            it.visible = isShow
        }
    }

    fun getTimeAgo(timeInMillis: Long): String {
        val format = SimpleDateFormat(Constants.DATE_TIME_FORMAT_WITH_SECONDS, Locale.getDefault())
        val past =
            format.parse(
                getDateAsString(
                    Date(timeInMillis),
                    Constants.DATE_TIME_FORMAT_WITH_SECONDS
                )
            ) ?: Date()
        val now = Date()
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
        val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
        return when {
            seconds < 10 -> "just now"
            seconds < 60 -> "$seconds seconds ago"
            minutes < 2 -> "$minutes minute ago"
            minutes < 60 -> "$minutes minutes ago"
            hours < 2 -> "$hours hour ago"
            hours < 24 -> "$hours hours ago"
            days < 2 -> "$days day ago"
            else -> "$days days ago"
        }
    }

    fun isValidURL(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    fun isValidYoutubeUrl(youTubeURl: String): Boolean {
        val success: Boolean
        val pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+".toRegex()
        success = youTubeURl.isNotEmpty() && youTubeURl.matches(pattern)
        return success
    }
}