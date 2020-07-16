package com.geeklabs.spiffyshow.ui.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.geeklabs.spiffyshow.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import kotlinx.android.synthetic.main.layout_youtube_view.view.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class CustomYoutubePlayer(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context!!, attrs) {

    init {
        View.inflate(context, R.layout.layout_youtube_view, this)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loadYoutubeView(path: String) {
        val youtubeVideoId = extractYoutubeId(path)
        if (youtubeVideoId?.isEmpty() == true) {
            showToast("Invalid youtube ID")
            return
        }
        customYoutubePlayer.addYouTubePlayerListener(object : YouTubePlayerListener {
            override fun onApiChange(youTubePlayer: YouTubePlayer) {

            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                showToast("Youtube error: $error")
            }

            override fun onPlaybackQualityChange(
                youTubePlayer: YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {
            }

            override fun onPlaybackRateChange(
                youTubePlayer: YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {
            }

            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(youtubeVideoId!!, 0f)
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
            }

            override fun onVideoLoadedFraction(
                youTubePlayer: YouTubePlayer,
                loadedFraction: Float
            ) {
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun extractYoutubeId(youtubeUrl: String): String? {
        var videoId: String? = null
        val pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher: Matcher = pattern.matcher(youtubeUrl)
        if (matcher.matches()) {
            videoId = matcher.group(1)
        }
        return videoId
    }
}