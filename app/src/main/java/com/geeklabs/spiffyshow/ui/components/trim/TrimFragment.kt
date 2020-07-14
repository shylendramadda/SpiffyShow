package com.geeklabs.spiffyshow.ui.components.trim

import android.net.Uri
import android.widget.MediaController
import com.geeklabs.spiffyshow.R
import com.geeklabs.spiffyshow.enums.Navigation
import com.geeklabs.spiffyshow.extensions.toast
import com.geeklabs.spiffyshow.extensions.visible
import com.geeklabs.spiffyshow.models.FileMetaData
import com.geeklabs.spiffyshow.ui.base.BaseFragment
import com.geeklabs.spiffyshow.ui.common.Progress
import com.geeklabs.spiffyshow.ui.components.main.MainActivity
import kotlinx.android.synthetic.main.fragment_trim.*
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener
import javax.inject.Inject


class TrimFragment : BaseFragment<TrimContract.View, TrimContract.Presenter>(),
    TrimContract.View, OnTrimVideoListener, OnK4LVideoListener {

    @Inject
    lateinit var trimPresenter: TrimPresenter
    private var fileMetaData: FileMetaData? = null
    private var isTrim: Boolean = false
    private lateinit var progress: Progress

    override fun initUI() {
        if (fileMetaData == null) {
            showToast("File meta data is empty")
            return
        }
        saveButton.setOnClickListener {
            val title = titleET.text.toString().trim()
            val description = descriptionET.text.toString().trim()
            val category = categoryET.text.toString().trim()
            presenter?.onSaveClicked(title, description, category, isTrim)
        }
        progress = Progress(context, R.string.please_wait, false)
        presenter?.setFileMetaData(fileMetaData!!)
        setVideoView(fileMetaData!!.path)
    }

    override fun showProgress() {
        progress.show()
    }

    override fun hideProgress() {
        progress.dismiss()
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
    }

    override fun onTrimStarted() {
        presenter?.onVideoTrimStarted()
    }

    override fun getResult(uri: Uri?) {
        presenter?.onGetResult(uri.toString())
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

    override fun initPresenter() = trimPresenter

    override fun injectDependencies() = getApplicationComponent().inject(this)

    override fun getLayoutResId() = R.layout.fragment_trim

    companion object {
        fun newInstance(
            fileMetaData: FileMetaData,
            isTrim: Boolean
        ): TrimFragment {
            return TrimFragment().apply {
                this.fileMetaData = fileMetaData
                this.isTrim = isTrim
            }
        }
    }
}