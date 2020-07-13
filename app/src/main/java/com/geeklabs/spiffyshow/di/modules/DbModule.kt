package com.geeklabs.spiffyshow.di.modules

import android.app.Application
import com.geeklabs.spiffyshow.data.local.AppDatabaseWrapper
import com.geeklabs.spiffyshow.di.scope.ApplicationScope
import com.geeklabs.spiffyshow.utils.PrefManager
import dagger.Module
import dagger.Provides

@Module
class DbModule(private val application: Application) {

    @ApplicationScope
    @Provides
    fun providesAppDatabaseWrapper(prefManager: PrefManager) =
        AppDatabaseWrapper(application.applicationContext, prefManager)
}
