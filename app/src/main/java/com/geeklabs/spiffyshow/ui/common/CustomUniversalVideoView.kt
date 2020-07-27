package com.geeklabs.spiffyshow.ui.common

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.geeklabs.spiffyshow.R
import com.log4k.d
import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.layout_universal_video_view.view.*

class CustomUniversalVideoView(context: Context?, attrs: AttributeSet?) :
    RelativeLayout(context!!, attrs) {

    init {
        View.inflate(context, R.layout.layout_universal_video_view, this)
        videoViewUniversal?.setMediaController(mediaControllerUniversal)

        videoViewUniversal?.setVideoViewCallback(object :
            UniversalVideoView.VideoViewCallback {
            override fun onBufferingStart(mediaPlayer: MediaPlayer?) {
                d("onBufferingStart UniversalVideoView callback")
            }

            override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {
                d("onBufferingEnd UniversalVideoView callback")
            }

            override fun onPause(mediaPlayer: MediaPlayer?) {
                d("onPause UniversalVideoView callback")
                mediaPlayer?.pause()
            }

            override fun onScaleChange(isFullscreen: Boolean) {
                d("onScaleChange UniversalVideoView callback")
            }

            override fun onStart(mediaPlayer: MediaPlayer?) {
                d("onStart UniversalVideoView callback")
                mediaPlayer?.start()
            }
        })
    }

    fun setVideoView(path: String, title: String) {
        val uri = Uri.parse(path)
        mediaControllerUniversal?.setTitle(title)
        videoViewUniversal?.setVideoURI(uri)
        videoViewUniversal?.seekTo(1000)
    }
}