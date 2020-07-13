package com.geeklabs.spiffyshow.data.local

import android.content.Context
import androidx.room.Room
import com.geeklabs.spiffyshow.utils.Constants.Companion.DATABASE_NAME
import com.geeklabs.spiffyshow.utils.PrefManager

class AppDatabaseWrapper(private val context: Context, private val prefManager: PrefManager) {

    private var appDatabaseInstance: AppDatabase? = null

    @Synchronized
    fun getAppDatabase(): AppDatabase {

        if (appDatabaseInstance == null) {
            val builder = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            /*if (BuildConfig.sqliteEncryptionEnabled) {
                val apiToken = prefManager.getString(PrefConfig.REGISTRATION_TOKEN_KEY)
                if (apiToken.isNullOrEmpty()) {
                    throw UnsupportedOperationException("Empty or null api token. Local DB can't be created or opened")
                }
                builder.openHelperFactory(SafeHelperFactory(apiToken.toCharArray()))
            }*/
            appDatabaseInstance = builder.fallbackToDestructiveMigration().build()
        }
        return appDatabaseInstance!!

    }
}