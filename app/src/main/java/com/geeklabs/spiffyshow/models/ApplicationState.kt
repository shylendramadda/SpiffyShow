package com.geeklabs.spiffyshow.models

import com.geeklabs.spiffyshow.data.local.models.user.User
import java.util.*

class ApplicationState {
    private var appStateMap: HashMap<String, Any?> = hashMapOf()
    var user: User? = null
    var isAdmin: Boolean = false

    fun getAppState(appStateKey: String, default: Any): Any {
        if (appStateMap[appStateKey] == null) {
            appStateMap[appStateKey] = default
        }
        return appStateMap[appStateKey]!!
    }

    fun clear() {
        appStateMap.clear()
        user = null
        isAdmin = false
    }
}
