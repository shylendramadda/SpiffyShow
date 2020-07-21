package com.geeklabs.spiffyshow.utils

class Constants {

    companion object {
        const val SMS_TIME_OUT: Long = 120 // 1 minute max
        const val DELAY_REFRESH: Long = 1500 // 1.5 seconds
        const val DATE_TIME_FORMAT_WITH_SECONDS = "dd/MM/yyyy hh:mm:ss a"
        const val DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm a"
        const val CHAT_TIME_FORMAT = "dd-MMM-YYYY hh:mm a"
        const val FIREBASE_TOKEN = "FireBaseToken"
        const val DATABASE_NAME = "SpiffyShow.db"
        const val MAX_FILE_SIZE = 1024

        const val TOKEN_KEY = "spiffy"
        const val USER_NAME = "spiffy"
        const val PASSWORD = "spiffy321"


        // Shared Preference keys
        const val IS_WELCOME_SEEN = "IS_WELCOME_SEEN"
        const val IS_LOGIN = "IS_LOGIN"
        const val USER_ID = "USER_ID"
        const val ADMIN = "ADMIN"

        const val ITEMS_WORKER = "ITEMS_WORKER"

        const val IMAGE_DIRECTORY = "/SpiffyShow/Images/"
        const val VIDEO_DIRECTORY = "/SpiffyShow/Videos/"
    }
}