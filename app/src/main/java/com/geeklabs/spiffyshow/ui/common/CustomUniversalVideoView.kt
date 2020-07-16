package com.geeklabs.spiffyshow.ui.common

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.geeklabs.spiffyshow.R
import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.layout_universal_video_view.view.*

class CustomUniversalVideoView(context: Context?, attrs: AttributeSet?) :
    RelativeLayout(context!!, attrs) {

    init {
        View.inflate(context, R.layout.layout_universal_video_view, this)
    }

    fun playVideo(path: String) {
        val uri = Uri.parse(path)
        videoViewUniversal?.setMediaController(mediaControllerUniversal)
        videoViewUniversal?.setVideoURI(uri)
        videoViewUniversal?.seekTo(1)
        videoViewUniversal?.setVideoViewCallback(object :
            UniversalVideoView.VideoViewCallback {
            override fun onBufferingStart(mediaPlayer: MediaPlayer?) {
            }

            override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {
            }

            override fun onPause(mediaPlayer: MediaPlayer?) {
                mediaPlayer?.pause()
            }

            override fun onScaleChange(isFullscreen: Boolean) {
            }

            override fun onStart(mediaPlayer: MediaPlayer?) {
                mediaPlayer?.start()
            }
        })
    }
}