package com.geeklabs.spiffyshow.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.work.WorkManager
import com.geeklabs.spiffyshow.data.DataRepository
import com.geeklabs.spiffyshow.data.local.LocalDataSource
import com.geeklabs.spiffyshow.data.remote.RemoteDataSource
import com.geeklabs.spiffyshow.di.scope.ApplicationScope
import com.geeklabs.spiffyshow.models.AppInfo
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.utils.RxBus
import com.google.android.gms.common.util.concurrent.NamedThreadFactory
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Named

@Module
class ApplicationModule(private val application: Application) {

    @ApplicationScope
    @Provides
    fun providesApplicationContext(): Context = application.applicationContext

    @ApplicationScope
    @Provides
    fun providesSharedPreferences(): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)

    @ApplicationScope
    @Provides
    fun providesRxBus(publisher: PublishSubject<Any>) = RxBus(publisher)

    @ApplicationScope
    @Provides
    fun providesDisplayMetrics() = application.resources.displayMetrics

    @ApplicationScope
    @Provides
    fun providesPublishSubject(): PublishSubject<Any> = PublishSubject.create()

    @ApplicationScope
    @Provides
    fun providesWorkManager() = WorkManager.getInstance()

    @ApplicationScope
    @Provides
    fun providesDataRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ) = DataRepository(
        localDataSource,
        remoteDataSource
    )

    @ApplicationScope
    @Provides
    fun providesAppInfo() = AppInfo()

    @ApplicationScope
    @Provides
    fun providesApplicationScope() = ApplicationState()

    @ApplicationScope
    @Provides
    @Named("SINGLE_THREAD_EXECUTOR")
    fun providesSingleThreadExecutorService(): ExecutorService = Executors.newSingleThreadExecutor()

    @ApplicationScope
    @Provides
    fun providesDefaultExecutorService(): ExecutorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE, NamedThreadFactory("App-Default"))

    companion object {
        const val DEFAULT_THREAD_POOL_SIZE = 10
    }
}
