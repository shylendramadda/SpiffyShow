package com.geeklabs.spiffyshow.di.components

import com.geeklabs.spiffyshow.di.modules.ApplicationModule
import com.geeklabs.spiffyshow.di.modules.DbModule
import com.geeklabs.spiffyshow.di.modules.NetworkModule
import com.geeklabs.spiffyshow.di.scope.ApplicationScope
import com.geeklabs.spiffyshow.service.AppService
import com.geeklabs.spiffyshow.service.MyFirebaseMessageService
import com.geeklabs.spiffyshow.ui.components.about.AboutFragment
import com.geeklabs.spiffyshow.ui.components.comment.CommentFragment
import com.geeklabs.spiffyshow.ui.components.feedback.FeedbackFragment
import com.geeklabs.spiffyshow.ui.components.login.LoginActivity
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import com.geeklabs.spiffyshow.ui.components.main.drawer.DrawerFragment
import com.geeklabs.spiffyshow.ui.components.main.home.HomeFragment
import com.geeklabs.spiffyshow.ui.components.main.original.OriginalFragment
import com.geeklabs.spiffyshow.ui.components.main.search.SearchFragment
import com.geeklabs.spiffyshow.ui.components.main.settings.SettingsFragment
import com.geeklabs.spiffyshow.ui.components.trim.TrimFragment
import com.geeklabs.spiffyshow.ui.components.notifications.NotificationFragment
import com.geeklabs.spiffyshow.ui.components.profile.ProfileFragment
import com.geeklabs.spiffyshow.ui.components.register.RegisterActivity
import com.geeklabs.spiffyshow.ui.components.splash.SplashActivity
import com.geeklabs.spiffyshow.ui.components.onboard.OnboardActivity
import com.geeklabs.spiffyshow.worker.ItemsWorker
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class, DbModule::class, NetworkModule::class])
interface ApplicationComponent {

    fun inject(myFirebaseMessageService: MyFirebaseMessageService)
    fun inject(appService: AppService)

    fun inject(activity: SplashActivity)
    fun inject(onboardActivity: OnboardActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(registerActivity: RegisterActivity)
    fun inject(activity: MainActivity)

    fun inject(itemsWorker: ItemsWorker)

    fun inject(homeFragment: HomeFragment)
    fun inject(drawerFragment: DrawerFragment)
    fun inject(notificationFragment: NotificationFragment)
    fun inject(settingsFragment: SettingsFragment)
    fun inject(aboutFragment: AboutFragment)
    fun inject(feedbackFragment: FeedbackFragment)
    fun inject(originalFragment: OriginalFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(trimFragment: TrimFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(commentFragment: CommentFragment)
}