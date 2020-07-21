package com.geeklabs.spiffyshow.ui.common

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.geeklabs.spiffyshow.enums.AppStateEnum
import com.geeklabs.spiffyshow.enums.Navigation
import com.geeklabs.spiffyshow.extensions.addFragment
import com.geeklabs.spiffyshow.extensions.replaceFragment
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.models.MiscState
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import com.geeklabs.spiffyshow.ui.components.about.AboutFragment
import com.geeklabs.spiffyshow.ui.components.comment.CommentFragment
import com.geeklabs.spiffyshow.ui.components.feedback.FeedbackFragment
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import com.geeklabs.spiffyshow.ui.components.main.home.HomeFragment
import com.geeklabs.spiffyshow.ui.components.main.original.OriginalFragment
import com.geeklabs.spiffyshow.ui.components.main.search.SearchFragment
import com.geeklabs.spiffyshow.ui.components.main.settings.SettingsFragment
import com.geeklabs.spiffyshow.ui.components.notifications.NotificationFragment
import com.geeklabs.spiffyshow.ui.components.profile.ProfileFragment
import com.geeklabs.spiffyshow.ui.components.trim.TrimFragment

class NavigationHandler(
    private val activity: AppCompatActivity,
    private var containerId: Int,
    applicationState: ApplicationState
) {

    private val fragmentManager = activity.supportFragmentManager
    private var currentFragment: Fragment? = null
    private var currentNavigation: Navigation = Navigation.NONE
    private val miscState =
        applicationState.getAppState(AppStateEnum.MISC_STATE.value, MiscState()) as MiscState

    init {
        currentFragment = fragmentManager.primaryNavigationFragment
        currentNavigation = getCurrentDisplayingFragment(currentFragment)
    }

    private fun getCurrentDisplayingFragment(currentFragment: Fragment?): Navigation {
        return when (currentFragment) {
            is HomeFragment -> Navigation.HOME
            is OriginalFragment -> Navigation.ORIGINAL
            is TrimFragment -> Navigation.TRIM
            is SearchFragment -> Navigation.SEARCH
            is SettingsFragment -> Navigation.SETTINGS
            is NotificationFragment -> Navigation.NOTIFICATION
            is AboutFragment -> Navigation.ABOUT
            is FeedbackFragment -> Navigation.FEEDBACK
            is ProfileFragment -> Navigation.PROFILE
            is CommentFragment -> Navigation.COMMENT
            else -> Navigation.NONE
        }
    }

    private fun getFragment(navigation: Navigation) = when (navigation) {
        Navigation.HOME -> fragmentManager.findFragmentByTag(Navigation.HOME.tag) ?: HomeFragment()
        Navigation.ORIGINAL -> OriginalFragment()
        Navigation.TRIM -> TrimFragment()
        Navigation.SEARCH -> SearchFragment()
        Navigation.SETTINGS -> SettingsFragment()
        Navigation.NOTIFICATION -> NotificationFragment()
        Navigation.ABOUT -> AboutFragment()
        Navigation.FEEDBACK -> FeedbackFragment()
        Navigation.PROFILE -> ProfileFragment()
        Navigation.COMMENT -> CommentFragment()
        Navigation.NONE -> null
        else -> null
    }

    fun navigateTo(navigation: Navigation) {
        if (navigation == Navigation.HOME) {
            popAllStack()
        }
        val fragment = getFragment(navigation)
        val current = currentNavigation == navigation
        if (current) return
        currentNavigation = navigation
        currentFragment = fragment
        //If we want to Toggle fragment uncomment below 2 lines and comment above 2 lines
        /*currentNavigation = if (current) Navigation.HOME else navigation
        currentFragment = if (current) getFragment(Navigation.HOME) else fragment*/
        if (currentNavigation == Navigation.HOME) {
            activity.replaceFragment(currentFragment!!, containerId, currentNavigation.tag)
        } else {
            activity.addFragment(currentFragment!!, containerId, currentNavigation.tag)
        }
        addToStack(fragment?.tag)
    }

    fun navigateTo(fragment: Fragment, tag: String) {
        if (!activity.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            return
        }
        currentFragment = fragment
        val navigation = getCurrentDisplayingFragment(currentFragment)
        currentNavigation = navigation
        activity.addFragment(currentFragment!!, containerId, tag)
        addToStack(tag)
    }

    private fun popAllStack() {
        miscState.fragmentStack.clear()
        val count = fragmentManager.backStackEntryCount
        (1..count).forEach { _ -> fragmentManager.popBackStack() }
        if (currentFragment?.isAdded == true) {
            val childCount = currentFragment?.childFragmentManager?.backStackEntryCount ?: 0
            (1..childCount).forEach { _ -> currentFragment?.childFragmentManager?.popBackStack() }
        }
    }

    private fun addToStack(tag: String?) {
        if (miscState.fragmentStack.contains(tag)) return
        miscState.fragmentStack.add(tag)
    }

    fun onBackPressed(): Boolean {
        //Check if current fragment handles back-press
        val handled = (currentFragment as? BaseFragment<*, *>)?.onBackPressed() ?: false
        if (!handled && fragmentManager.backStackEntryCount >= 1) {
            if (currentFragment !is HomeFragment) {
                navigateTo(Navigation.HOME)
                (activity as MainActivity).setHomeAsSelected()
                return true
            }
            return false
        }

        val stack = miscState.fragmentStack
        if (!handled && stack.isNotEmpty()) {
            stack.pop()
        }
        miscState.fragmentStack = stack
        return handled
    }

}