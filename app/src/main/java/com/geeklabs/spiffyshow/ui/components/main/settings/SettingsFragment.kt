package com.geeklabs.spiffyshow.ui.components.main.settings

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.user.User
import com.geeklabs.spiffyshow.enums.Navigation
import com.geeklabs.spiffyshow.extensions.shouldShow
import com.geeklabs.spiffyshow.extensions.toast
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.layout_content_settings.*
import permissions.dispatcher.*
import javax.inject.Inject

@RuntimePermissions
class SettingsFragment : BaseFragment<SettingsContract.View, SettingsContract.Presenter>(),
    SettingsContract.View {

    @Inject
    lateinit var settingsPresenter: SettingsPresenter
    private var isPermissionEnable = false

    override fun initUI() {
        editImageIV.setOnClickListener {
            presenter?.onEditImageClicked(isPermissionEnable)
        }

        saveButton.setOnClickListener {
            val name = nameET.text.toString().trim()
            val mobile = phoneNumberET.text.toString().trim()
            val email = emailET.text.toString().trim()
            val address = addressET.text.toString().trim()
            val interests = interestET.text.toString().trim()
            val bio = bioET.text.toString().trim()
            val user = User(
                name = name, phoneNumber = mobile, email = email,
                addressInfo = address, interests = interests, bio = bio
            )
            presenter?.saveUpdateUser(user)
        }

    }

    override fun showUserDetails(user: User) {
        nameET.setText(user.name)
        phoneNumberET.setText(user.phoneNumber)
        emailET.setText(user.email)
        addressET.setText(user.addressInfo)
        interestET.setText(user.interests)
        bioET.setText(user.bio)
        if (user.imageUrl?.isNotEmpty() == true) {
            updateProfileImage(user.imageUrl ?: "")
        } else if (user.name?.isNotEmpty() == true) {
            userImageText.text = user.name?.substring(0, 1)
            userImage.shouldShow = false
            userImageText.shouldShow = true
        }
    }

    override fun updateProfileImage(filePath: String) {
        Glide.with(activity!!).load(filePath).placeholder(R.drawable.ic_icon_user)
            .dontAnimate()
            .into(userImage)
        userImage.shouldShow = true
        userImageText.shouldShow = false
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
        if ((grantResults.isNotEmpty())) {
            val isGiven1 = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val isGiven2 = grantResults[1] == PackageManager.PERMISSION_GRANTED
            val isGiven3 = grantResults[2] == PackageManager.PERMISSION_GRANTED
            if ((isGiven1 && isGiven2 && isGiven3) || isPermissionEnable) {
                showUploadImageDialog()
            }
        }
    }

    override fun showUploadImageDialog() {
        val pictureDialog = AlertDialog.Builder(context!!)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera"
        )
        pictureDialog.setItems(
            pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> startGalleryIntent()
                1 -> startCameraIntent()
            }
        }
        pictureDialog.show()
    }

    private fun startGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = IMAGE_TYPE
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY)
    }

    private fun startCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || resultCode != Activity.RESULT_OK) {
            return
        }
        val fileUri = data.data.toString()
        presenter?.onSaveFilePath(fileUri)
        super.onActivityResult(requestCode, resultCode, data)
    }

    @NeedsPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    fun checkPermission() {
        isPermissionEnable = true
    }

    @OnShowRationale(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    fun showRationaleForLocation(request: PermissionRequest) {
        request.proceed()
    }

    @OnPermissionDenied(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    fun onPermissionDenied() {
        isPermissionEnable = false
        showToast(getString(R.string.permissionDenied))
    }

    @OnNeverAskAgain(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    fun onNeverAskAgain() {
        isPermissionEnable = false
        showToast(getString(R.string.neverAsk))
    }

    override fun navigateToHome() {
        (activity as MainActivity).navigateToScreen(Navigation.HOME)
    }

    override fun showToast(message: String) {
        activity?.toast(message)
    }

    override fun initPresenter() = settingsPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_settings

    companion object {
        private const val IMAGE_TYPE = "image/*"
        private const val CAMERA = 1
        private const val GALLERY = 2
    }
}