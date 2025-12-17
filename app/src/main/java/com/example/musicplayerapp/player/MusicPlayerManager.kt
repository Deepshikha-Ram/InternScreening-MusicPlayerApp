package com.example.musicplayerapp.player

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * Date: 17 December, 2025
 * MadeBy: Deepshikha Ram
 * Purpose: To build MusicPlayerApp
 * Aim: Creating Music PlayerUsing Kotlin Multiplatform
 */


class MusicPlayerManager {

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration

    fun play(url: String) {
        release()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener {
                _duration.value = it.duration
                it.start()
                _isPlaying.value = true
                startProgressUpdates()
            }
            setOnErrorListener { _, _, _ ->
                release()
                true
            }
            prepareAsync()
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
            }
        }
    }

    fun resume() {
        mediaPlayer?.start()
        _isPlaying.value = true
    }

    private fun startProgressUpdates() {
        progressJob?.cancel()
        progressJob = CoroutineScope(Dispatchers.Main).launch {
            while (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                _currentPosition.value = mediaPlayer!!.currentPosition
                delay(1000)
            }
        }
    }

    fun release() {
        progressJob?.cancel()
        progressJob = null

        mediaPlayer?.release()
        mediaPlayer = null

        _isPlaying.value = false
        _currentPosition.value = 0
        _duration.value = 0
    }
}