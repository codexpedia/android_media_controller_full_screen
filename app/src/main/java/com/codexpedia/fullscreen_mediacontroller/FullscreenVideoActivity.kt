package com.codexpedia.fullscreen_mediacontroller

import android.app.Activity
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_fullscreen_video.*

import java.io.IOException

class FullscreenVideoActivity : Activity(), SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {
    private val TAG = "FullscreenVideoActivity"
    private var mediaPlayer: MediaPlayer? = null
    private var controller: VideoControllerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_video)
        val videoHolder = videoSurface.holder
        videoHolder.addCallback(this)
        val videoPath = "android.resource://" + packageName + "/" + R.raw.sample_video
        mediaPlayer = MediaPlayer()
        controller = VideoControllerView(this)
        try {
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer!!.setDataSource(this, Uri.parse(videoPath))
            mediaPlayer!!.setOnPreparedListener(this)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    //Show the media controller
    override fun onTouchEvent(event: MotionEvent): Boolean {
        controller!!.show()
        return false
    }

    // Implement SurfaceHolder.Callback
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mediaPlayer!!.setDisplay(holder)
        mediaPlayer!!.prepareAsync()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {}
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    override fun onPrepared(mp: MediaPlayer) {
        controller!!.setMediaPlayer(this)
        controller!!.setAnchorView(findViewById<View>(R.id.videoSurfaceContainer) as FrameLayout)
        mediaPlayer!!.start()
    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    override fun getBufferPercentage(): Int {
        return 0
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer!!.currentPosition
    }

    override fun getDuration(): Int {
        return mediaPlayer!!.duration
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer!!.isPlaying
    }

    override fun pause() {
        mediaPlayer!!.pause()
    }

    override fun seekTo(i: Int) {
        mediaPlayer!!.seekTo(i)
    }

    override fun start() {
        mediaPlayer!!.start()
    }

    override fun isFullScreen(): Boolean {
        val isFullScreen = resources.configuration.orientation == 2
        Log.d(TAG, "isFullScreen: " + isFullScreen)
        return isFullScreen
    }

    override fun toggleFullScreen() {
        Log.d(TAG, "toggleFullScreen")
        mediaPlayer!!.pause()
        if (resources.configuration.orientation == 1) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
