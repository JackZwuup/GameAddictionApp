package com.example.capstoneassignmentjack

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.capstoneassignmentjack.ui.screens.getSong


class MusicPlayerService : Service(), AudioManager.OnAudioFocusChangeListener {

    // MediaPlayer to play music.
    private lateinit var mediaPlayer: MediaPlayer

    // Variable to track music playback state.
    private var isMusicPlaying: Boolean = false

    // Binder for clients to interact with the service.
    private val binder = MusicBinder()

    // AudioManager for audio focus handling.
    private lateinit var audioManager: AudioManager

    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize MediaPlayer with the selected song.
        mediaPlayer = MediaPlayer.create(this, getSong())

        // Loop the music.
        mediaPlayer.isLooping = true

        // Initialize AudioManager.
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    override fun onDestroy() {
        super.onDestroy()

        // Stop and release the MediaPlayer.
        mediaPlayer.stop()
        mediaPlayer.release()

        // Abandon audio focus.
        audioManager.abandonAudioFocus(this)
    }
    // Function to handle audio focus changes.
    override fun onAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_LOSS -> {
                // Lost focus, stop playback.
                stopMusic()
                isMusicPlaying = false
            }

            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                // Lost focus temporarily, pause playback.
                mediaPlayer.pause()
                isMusicPlaying = false
            }

            AudioManager.AUDIOFOCUS_GAIN -> {
                // Gained focus, resume playback if it was playing.
                if (isMusicPlaying) {
                    mediaPlayer.start()
                    isMusicPlaying = true
                }
            }
        }
    }

    // Function to check if music is currently playing.
    fun isPlaying(): Boolean {
        val playing = if (::mediaPlayer.isInitialized) {
            mediaPlayer.isPlaying
        } else {
            false
        }
        return playing
    }

    // Function to start playing music
    fun startMusic() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            isMusicPlaying = true
        }
    }

    // Function to stop playing music
    fun stopMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isMusicPlaying = false
        }
    }

}
