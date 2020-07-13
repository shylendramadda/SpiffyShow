package com.geeklabs.spiffyshow

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.geeklabs.spiffyshow.di.components.ApplicationComponent
import com.geeklabs.spiffyshow.di.components.DaggerApplicationComponent
import com.geeklabs.spiffyshow.di.modules.ApplicationModule
import com.geeklabs.spiffyshow.di.modules.DbModule
import com.geeklabs.spiffyshow.di.modules.NetworkModule
import com.geeklabs.spiffyshow.extensions.notificationManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.log4k.Level
import com.log4k.Log4k
import com.log4k.android.AndroidAppender
import com.log4k.i

class App : Application(), LifecycleObserver {

    val applicationComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .dbModule(DbModule(this))
            .networkModule(NetworkModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        if (BuildConfig.DEBUG) {
            Log4k.add(Level.Verbose, ".*", AndroidAppender())
        }
    }

    private fun setGlobalRxErrorHandler(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        i("App is in background")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        i("App is in foreground")
        notificationManager.cancelAll()
    }
}