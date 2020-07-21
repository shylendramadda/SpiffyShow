package com.geeklabs.spiffyshow.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.geeklabs.spiffyshow.R
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager
import com.volokh.danylo.video_player_manager.meta.MetaData
import com.volokh.danylo.video_player_manager.ui.MediaPlayerWrapper
import kotlinx.android.synthetic.main.layout_video_player_view.view.*


class CustomVideoPlayer(context: Context?, attrs: AttributeSet?) :
    RelativeLayout(context!!, attrs) {

    private var videoPlayerManager: VideoPlayerManager<MetaData>? = null

    init {
        View.inflate(context, R.layout.layout_video_player_view, this)
        videoPlayerManager = SingleVideoPlayerManager(PlayerItemChangeListener {
        })
    }

    fun setVideoView(path: String) {
        videoPlayerView.addMediaPlayerListener(object :
            MediaPlayerWrapper.MainThreadMediaPlayerListener {
            override fun onBufferingUpdateMainThread(percent: Int) {
            }

            override fun onVideoCompletionMainThread() {
            }

            override fun onVideoStoppedMainThread() {
            }

            override fun onVideoPreparedMainThread() {
                videoPlayerManager?.playNewVideo(null, videoPlayerView, path)
            }

            override fun onErrorMainThread(what: Int, extra: Int) {
            }

            override fun onVideoSizeChangedMainThread(width: Int, height: Int) {
            }
        })
    }

}