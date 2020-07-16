package com.geeklabs.spiffyshow.ui.components.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.enums.Navigation
import com.geeklabs.spiffyshow.extensions.*
import com.geeklabs.spiffyshow.models.ApplicationState
import com.geeklabs.spiffyshow.service.AppService
import com.geeklabs.spiffyshow.ui.base.BaseActivity
import com.geeklabs.spiffyshow.ui.common.NavigationHandler
import com.geeklabs.spiffyshow.ui.common.Progress
import com.geeklabs.spiffyshow.ui.components.comment.CommentFragment
import com.geeklabs.spiffyshow.ui.components.login.LoginActivity
import com.geeklabs.spiffyshow.ui.components.main.drawer.DrawerFragment
import com.geeklabs.spiffyshow.ui.components.profile.ProfileFragment
import com.geeklabs.spiffyshow.ui.components.trim.TrimFragment
import com.geeklabs.spiffyshow.utils.Constants
import com.geeklabs.spiffyshow.utils.Utils
import com.geeklabs.spiffyshow.utils.Utils.showHideViews
import com.geeklabs.spiffyshow.worker.ItemsWorker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.internal.common.CommonUtils.hideKeyboard
import com.log4k.d
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*
import permissions.dispatcher.*
import javax.inject.Inject


@RuntimePermissions
class MainActivity : BaseActivity<MainContract.View, MainContract.Presenter>(), MainContract.View,
    Utils.DialogActionListener {

    @Inject
    lateinit var mainPresenter: MainPresenter

    @Inject
    lateinit var applicationState: ApplicationState

    @Inject
    lateinit var workManager: WorkManager

    private var isPermissionEnable = false
    private var progress: Progress? = null
    private lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationHandler = NavigationHandler(this, R.id.mainContainer, applicationState)
    }

    override fun navigateToHome() {
        navigateToScreen(Navigation.HOME)
    }

    override fun initUI() {
        checkPermissionWithPermissionCheck()
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        menuButton.setOnClickListener { presenter?.onMenuButtonClicked() }
        countFL.setOnClickListener {
            navigateToScreen(Navigation.NOTIFICATION)
        }
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun startWorkManager() {
        val firstPriorityList = mutableListOf(
            OneTimeWorkRequest.Builder(ItemsWorker::class.java).addTag(Constants.ITEMS_WORKER)
                .build()
        )
        workManager.beginWith(firstPriorityList).enqueue()
    }

    override fun onNewIntent(intent: Intent?) {
        val title = intent?.getStringExtra("title")
        d("Notification title: $title")
        // TODO when came from notification
        super.onNewIntent(intent)
    }

    // To hide drawer when user touch on the screen out side of drawer layout
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val viewRect = Rect()
        navDrawer.getGlobalVisibleRect(viewRect)
        if (!viewRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
            closeDrawer()
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setHomeAsSelected() {
        bottomNavigation.menu.findItem(R.id.navigation_home).isChecked = true
        setToolBarTitle(Navigation.HOME)
    }

    private var navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val navigation = when (item.itemId) {
                R.id.navigation_home -> Navigation.HOME
                R.id.navigation_original -> Navigation.ORIGINAL
                R.id.navigation_add -> Navigation.ADD
                R.id.navigation_search -> Navigation.SEARCH
                R.id.navigation_settings -> Navigation.SETTINGS
                else -> Navigation.HOME
            }
            navigateToScreen(navigation)
            true
        }

    private fun setToolBarTitle(navigation: Navigation) {
        var isShowBottomNav = true
        val title = when (navigation) {
            Navigation.HOME -> {
                bottomNavigation.menu.findItem(R.id.navigation_home).isChecked = true
                R.string.app_name
            }
            Navigation.ORIGINAL -> {
                bottomNavigation.menu.findItem(R.id.navigation_original).isChecked = true
                R.string.originals
            }
            Navigation.NOTIFICATION -> {
                isShowBottomNav = false
                R.string.notifications
            }
            Navigation.TRIM -> {
                isShowBottomNav = false
                R.string.add_video
            }
            Navigation.SETTINGS -> R.string.settings
            Navigation.SEARCH -> R.string.search_without_colon
            Navigation.ABOUT -> {
                isShowBottomNav = false
                R.string.about
            }
            Navigation.FEEDBACK -> {
                isShowBottomNav = false
                R.string.feedback_no_colon
            }
            Navigation.PROFILE -> {
                isShowBottomNav = false
                R.string.profile
            }
            Navigation.COMMENT -> {
                isShowBottomNav = false
                R.string.comment
            }
            else -> {
                R.string.app_name
            }
        }
        showHideViews(isShowBottomNav, bottomNavigation)
        menuButton.visible = isShowBottomNav
        backButton.visible = !isShowBottomNav
        titleToolbar.text = getString(title)
    }

    override fun showDrawer() {
        currentFocus?.hideKeyboard(this)
        drawerLayout.openDrawer(GravityCompat.START)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN)
        replaceFragment(DrawerFragment(), R.id.drawerContainer)
    }

    fun navigateToScreen(navigation: Navigation) {
        closeDrawer()
        mainContainer.hideKeyboard(this)
        if (navigation == Navigation.ADD) {
            presenter?.onAddButtonClicked(isPermissionEnable)
        } else {
            navigationHandler.navigateTo(navigation)
        }
        setToolBarTitle(navigation)
    }

    fun closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun finishView() {
        finishAffinity()
    }

    override fun startVideoIntent() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = VIDEO_TYPE
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Video"), GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null || resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == GALLERY) {
            val fileUri = data.data.toString()
            presenter?.onSaveFilePath(fileUri)
        }
    }

    override fun askPermissions() {
        checkPermissionWithPermissionCheck()
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.isNotEmpty())) {
            val isGiven1 = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val isGiven2 = grantResults[1] == PackageManager.PERMISSION_GRANTED
            val isGiven3 = grantResults[2] == PackageManager.PERMISSION_GRANTED
            val isGiven4 = grantResults[3] == PackageManager.PERMISSION_GRANTED
            val isGiven5 = grantResults[4] == PackageManager.PERMISSION_GRANTED
            if ((isGiven1 && isGiven2 && isGiven3) || isPermissionEnable) {
                startService()
            }
            if ((isGiven4 && isGiven5) || isPermissionEnable) {
                presenter?.onAddButtonClicked(isPermissionEnable)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @NeedsPermission(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun checkPermission() {
        isPermissionEnable = true
        startService()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OnShowRationale(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun showRationaleForLocation(request: PermissionRequest) {
        request.proceed()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OnPermissionDenied(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun onPermissionDenied() {
        isPermissionEnable = false
        toast(getString(R.string.permissionDenied))
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OnNeverAskAgain(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun onNeverAskAgain() {
        isPermissionEnable = false
        toast(getString(R.string.neverAsk))
    }

    override fun navigateToLogin() {
        launchActivity<LoginActivity> { }
        finishView()
    }

    override fun navigateToTrim(
        obj: Any,
        isTrim: Boolean
    ) {
        val fragment = TrimFragment.newInstance(obj, isTrim)
        navigationHandler.navigateTo(fragment, "TrimFragment")
        setToolBarTitle(Navigation.TRIM)
    }

    fun navigateToUserProfile(user: User) {
        val fragment = ProfileFragment.newInstance(user)
        navigationHandler.navigateTo(fragment, "ProfileFragment")
        setToolBarTitle(Navigation.PROFILE)
    }

    fun navigateToComment(obj: Any) {
        val fragment = CommentFragment.newInstance(obj)
        navigationHandler.navigateTo(fragment, "CommentFragment")
        setToolBarTitle(Navigation.COMMENT)
    }

    override fun onBackPressed() {
        hideKeyboard(this, toolBar)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.currentFocus?.setKeyboardVisibility(false)
            closeDrawer()
        } else if (!navigationHandler.onBackPressed()) {
            Utils.showAlertDialog(
                this, getString(R.string.app_name), "Are you sure want to exit from the app?",
                "Yes", "No", true, this
            )
        }
    }

    override fun onPositiveClick(title: String?) {
        finishView()
    }

    private fun startService() {
        AppService.start(this)
    }

    override fun stopService() {
        AppService.stop(this)
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun showAlert(message: String) {
        alert("File Error", message) {
            positiveButton(getString(R.string.okay))
            negativeButton("")
        }.show()
    }

    override fun showHideProgress(isShow: Boolean) {
        if (isShow) progress = Progress(this, R.string.please_wait, cancelable = false)
        progress?.apply { if (isShow) show() else dismiss() }
    }

    override fun initPresenter() = mainPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.activity_main

    companion object {
        // private const val CAMERA = 101
        private const val GALLERY = 102
        private const val VIDEO_TYPE = "video/*"
    }
}
