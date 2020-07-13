package com.geeklabs.spiffyshow.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.geeklabs.spiffyshow.App
import com.geeklabs.spiffyshow.BuildConfig
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.location.DeviceLocation
import com.geeklabs.spiffyshow.domain.local.location.SaveDeviceLocationUseCase
import com.geeklabs.spiffyshow.events.LocationUpdateEvent
import com.geeklabs.spiffyshow.extensions.notificationManager
import com.geeklabs.spiffyshow.service.MyFirebaseMessageService.Companion.FIREBASE_CHANNEL_ID
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import com.geeklabs.spiffyshow.utils.RxBus
import com.geeklabs.spiffyshow.utils.Utils
import com.google.android.gms.location.*
import com.log4k.d
import com.log4k.e
import com.log4k.w
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class AppService : Service() {

    @Inject
    lateinit var rxBus: RxBus

    @Inject
    lateinit var saveDeviceLocationUseCase: SaveDeviceLocationUseCase

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var disposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        (application as App).applicationComponent.inject(this)
        initialization()
        createNotificationChannel()
        createNotificationChannelForFirebase()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_START_FOREGROUND_SERVICE -> startForegroundService()
            ACTION_STOP_FOREGROUND_SERVICE -> stopForegroundService()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return LocalBinder()
    }

    private fun initialization() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.smallestDisplacement = SMALLEST_DISPLACEMENT
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        onNewLocation(location)
                        d("Location: $location")
                    }
                }
        } else {
            d("ACCESS_FINE_LOCATION is turned off, this needs to be turned on for proper functioning of the application")
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)
                if (result?.lastLocation != null) {
                    onNewLocation(result.lastLocation)
                    d("onLocationChange: ${result.lastLocation}")
                }
            }
        }
        requestLocationUpdates()
    }

    private fun createNotificationChannel() {
        // Android O requires a Notification Channel.
        @RequiresApi(Build.VERSION_CODES.O)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the channel for the notification
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            )
            channel.lightColor = Color.BLUE
            channel.description = getString(R.string.notification_for_status)
            channel.enableLights(true)
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannelForFirebase() {
        @RequiresApi(Build.VERSION_CODES.O)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the channel for the notification
            val channel = NotificationChannel(
                FIREBASE_CHANNEL_ID, getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.lightColor = Color.BLUE
            channel.description = getString(R.string.notification)
            channel.enableLights(true)
            channel.setShowBadge(true)
            channel.importance = NotificationManager.IMPORTANCE_HIGH
            notificationManager.createNotificationChannel(channel)
        }
    }

    inner class LocalBinder : Binder() {
        internal val service: AppService
            get() = this@AppService
    }

    private fun requestLocationUpdates() {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()
            )
        } catch (ex: SecurityException) {
            e("Lost location permission. Could not request updates. ${ex.message}")
        }
    }

    private fun removeLocationUpdates() {
        try {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        } catch (ex: SecurityException) {
            ex.printStackTrace()
            e("Lost location permission. Could not remove updates")
        }
    }

    private fun onNewLocation(location: Location) {
        try {
            if (!BuildConfig.DEBUG && location.isFromMockProvider) {
                w("Used mock location provider: $location")
                return
            }
            d("NewLocation:  ${location.latitude},${location.longitude} - ${location.accuracy} - ${location.bearing}")
            if (Utils.isNetworkAvailable(this)) {
                fetchAddressFromLocation(location)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fetchAddressFromLocation(location: Location) {
        disposables.add(Observable.fromCallable {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )
            addresses
        }.observeOn(Schedulers.newThread())
            .subscribe {
                // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                if (it.isNotEmpty()) {
                    val addressOne = it[0]
                    val city = addressOne.locality
                    val state = addressOne.adminArea
                    val country = addressOne.countryName
                    val postalCode = addressOne.postalCode
                    val addressLine = addressOne.getAddressLine(0)
                    val deviceLocation = DeviceLocation(
                        city = city,
                        state = state,
                        country = country,
                        pinCode = postalCode,
                        addressLine = addressLine,
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    saveLocationInfo(deviceLocation, location)
                }
            })
    }

    @WorkerThread
    private fun saveLocationInfo(
        deviceLocation: DeviceLocation,
        location: Location
    ) {
        disposables += Single.just(saveDeviceLocationUseCase.execute(deviceLocation))
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                rxBus.publish(LocationUpdateEvent(location = location))
            }, {
                e("saveLocationInfo: ${it.message}")
            })
    }

    private fun startForegroundService() {
        // Create notification default intent.
        val intent = Intent(this, MainActivity::class.java)// need to mention the activity
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(applicationInfo.icon)
            .setOngoing(true)
            .setLargeIcon(
                (ContextCompat.getDrawable(
                    this,
                    R.drawable.app_logo
                ) as BitmapDrawable).bitmap
            )
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_for_status))
            .setContentIntent(pendingIntent).build()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopForegroundService() {
        stopForeground(true)
        removeLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        d("App service is stopped")
        stopForegroundService()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    companion object {
        const val CHANNEL_ID = "SpiffyShowService"
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        const val NOTIFICATION_ID = 123
        const val UPDATE_INTERVAL_IN_MILLISECONDS = 60 * 1000L// 60 Seconds
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
        const val SMALLEST_DISPLACEMENT = 0f

        fun start(context: Context) {
            val intent = Intent(context, AppService::class.java)
            intent.action = ACTION_START_FOREGROUND_SERVICE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, AppService::class.java)
            intent.action = ACTION_STOP_FOREGROUND_SERVICE
            context.stopService(intent)
        }
    }
}