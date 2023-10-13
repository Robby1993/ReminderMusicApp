package com.robinson.reminderapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import com.robinson.reminderapp.R

@SuppressLint("StaticFieldLeak")
class MusicControl(private val mContext: Context) {
    private var mMediaPlayer: MediaPlayer? = null
    fun playMusic() {
        stopMusic()
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.smoke_beep)
        mMediaPlayer?.start()
    }

    fun stopMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.seekTo(0)
        }
    }

    companion object {
        private var sInstance: MusicControl? = null

        @JvmStatic
        fun getInstance(context: Context): MusicControl? {
            if (sInstance == null) {
                sInstance = MusicControl(context)
            }
            return sInstance
        }
    }
}