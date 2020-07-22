package com.geeklabs.spiffyshow.ui.components.trim

import android.net.Uri
import android.widget.MediaController
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.data.local.models.item.Original
import com.geeklabs.spiffyshow.data.local.models.item.Trim
import com.geeklabs.spiffyshow.enums.Navigation
import com.geeklabs.spiffyshow.extensions.alert
import com.geeklabs.spiffyshow.extensions.input
import com.geeklabs.spiffyshow.extensions.toast
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import com.geeklabs.spiffyshow.ui.common.Progress
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import kotlinx.android.synthetic.main.fragment_trim.*
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener
import java.io.File
import javax.inject.Inject


class TrimFragment : BaseFragment<TrimContract.View, TrimContract.Presenter>(),
    TrimContract.View, OnTrimVideoListener, OnK4LVideoListener {

    @Inject
    lateinit var trimPresenter: TrimPresenter
    private var obj: Any? = null
    private var fileMetaData: FileMetaData? = null
    private var isTrim: Boolean = false
    private var progress: Progress? = null

    override fun initUI() {
        if (obj == null) {
            showToast("File meta data is empty")
            return
        }
        if (obj is Trim) {
            val trim = obj as Trim
            fileMetaData = trim.fileMetaData
            titleET.setText(trim.title)
            descriptionET.setText(trim.description)
            categoryET.setText(trim.category)
            originalUrlET.setText(trim.originalUrl)
        } else {
            val original = obj as Original
            fileMetaData = original.fileMetaData
            titleET.setText(original.title)
            descriptionET.setText(original.description)
            originalUrlET.setText(original.originalUrl)
            categoryET.setText(original.category)
        }
        saveButton.setOnClickListener {
            val externalUri = urlET.input().trim()
            val title = titleET.input().trim()
            val description = descriptionET.input().trim()
            val category = categoryET.input().trim()
            val originalUrl = originalUrlET.input().trim()
            presenter?.onSaveClicked(externalUri, title, description, category, originalUrl, isTrim)
        }
        setFileMetaData()
    }

    private fun setFileMetaData() {
        if (fileMetaData == null) {
            urlET.visible = true
            videoLL.visible = false
        } else {
            presenter?.setItem(obj!!)
            when {
                fileMetaData!!.path.isNotEmpty() && fileMetaData!!.size.isEmpty() -> {
                    isTrim = false
                    youtubePlayer.setYoutubeView(fileMetaData!!.path)
                    urlET.visible = true
                    videoLL.visible = true
                    urlET.setText(fileMetaData?.path ?: "")
                    youtubePlayer.visible = true
                    videoTrimmer.visible = false
                    videoView.visible = false
                }
                File(fileMetaData!!.path).exists() -> {
                    urlET.visible = false
                    setVideoView(fileMetaData!!.path)
                }
                else -> {
                    alert(
                        getString(R.string.file_not_exists),
                        getString(R.string.file_not_exists_error)
                    ) {
                        positiveButton(getString(R.string.okay)) {
                            navigateToOriginals()
                        }
                        negativeButton("")
                    }.show()
                }
            }
        }
    }

    override fun showHideProgress(isShow: Boolean) {
        if (isShow) progress = Progress(context, R.string.please_wait, cancelable = false)
        progress?.apply { if (isShow) show() else dismiss() }
    }

    private fun setVideoView(filePath: String) {
        val uri = Uri.parse(filePath)
        if (isTrim) {
            videoTrimmer.setMaxDuration(120) // 2 min
            videoTrimmer.setVideoURI(uri)
            videoTrimmer.setOnTrimVideoListener(this)
            videoTrimmer.setOnK4LVideoListener(this)
            videoTrimmer.setVideoInformationVisibility(true)
        } else {
            val mediaController = MediaController(context)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(uri)
            videoView.seekTo(1)
        }
        videoTrimmer.visible = isTrim
        videoView.visible = !isTrim
        youtubePlayer.visible = false
    }

    override fun onTrimStarted() {
        presenter?.onVideoTrimStarted()
    }

    override fun getResult(uri: Uri?) {
        activity?.runOnUiThread {
            presenter?.onGetResult(uri.toString())
        }
    }

    override fun onVideoPrepared() {
        presenter?.onVideoPrepared()
    }

    override fun onError(message: String?) {
        showToast(message ?: "Error")
    }

    override fun showToast(title: String) {
        activity?.toast(title)
    }

    override fun cancelAction() {
        presenter?.onCancelClick()
    }

    override fun navigateToHome() {
        (activity as MainActivity).navigateToScreen(Navigation.HOME)
    }

    override fun navigateToOriginals() {
        (activity as MainActivity).navigateToScreen(Navigation.ORIGINAL)
    }

    override fun onBackPressed(): Boolean {
        if (fragmentManager?.backStackEntryCount ?: 0 > 0) {
            (activity as MainActivity).setToolBarTitle(Navigation.ORIGINAL)
            fragmentManager?.popBackStack()
            return true
        }
        return false
    }

    override fun initPresenter() = trimPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_trim

    companion object {
        fun newInstance(
            obj: Any,
            isTrim: Boolean
        ): TrimFragment {
            return TrimFragment().apply {
                this.obj = obj
                this.isTrim = isTrim
            }
        }
    }
}