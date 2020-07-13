package com.geeklabs.spiffyshow.enums

import com.geeklabs.spiffyshow.R

enum class StringEnum(val resId: Int) {
    WELCOME(R.string.welcome),
    UPDATE_SUCCESS(R.string.update_success),
    NO_INTERNET(R.string.no_network),
    DELETE_SUCCESS(R.string.delete_success),
    SOMETHING_WENT_WRONG(R.string.something_went_wrong),
    SOMETHING_WENT_WRONG_SERVER(R.string.something_went_wrong_server),
    INVALID_CODE(R.string.invalid_code),
    FILE_PATH_ERROR(R.string.file_path_error),
    USER_LOCATION_NOT_UPDATED(R.string.user_location_not_updated),
}