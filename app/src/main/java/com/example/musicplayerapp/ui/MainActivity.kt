package com.example.musicplayerapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayerapp.data.network.JamendoApiClient
import com.example.musicplayerapp.data.repository.MusicRepository
import com.example.musicplayerapp.databinding.ActivityMainBinding
import com.example.musicplayerapp.player.MusicPlayerManager
import com.example.musicplayerapp.ui.adapter.TrackAdapter
import com.example.musicplayerapp.ui.viewmodel.MusicUiState
import com.example.musicplayerapp.ui.viewmodel.MusicViewModel
import com.example.musicplayerapp.ui.viewmodel.MusicViewModelFactory
import com.example.musicplayerapp.BuildConfig
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


/**
 * Date: 17 December, 2025
 * MadeBy: Deepshikha Ram
 * Purpose: To build MusicPlayerApp
 * Aim: Creating Music PlayerUsing Kotlin Multiplatform
 */

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var playerManager: MusicPlayerManager
    private lateinit var adapter: TrackAdapter

    private val viewModel: MusicViewModel by viewModels {
        MusicViewModelFactory(
            MusicRepository(
                applicationContext,
                JamendoApiClient(BuildConfig.JAMENDO_API_KEY)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerManager = MusicPlayerManager()
        setupRecyclerView()
        observeUiState()
        setupControls()
    }

    private fun setupRecyclerView() {
        adapter = TrackAdapter { track ->
            playerManager.play(track.audioUrl)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is MusicUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    }

                    is MusicUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        adapter.submitList(state.tracks)
                    }

                    is MusicUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun setupControls() {

        binding.sortButton.setOnClickListener {
            viewModel.toggleSortMode()
        }

        binding.playPauseButton.setOnClickListener {
            if (playerManager.isPlaying.value) {
                playerManager.pause()
            } else {
                playerManager.resume()
            }
        }

        lifecycleScope.launch {
            playerManager.currentPosition.collectLatest { pos ->
                binding.currentTime.text = formatTime(pos)
            }
        }

        lifecycleScope.launch {
            playerManager.duration.collectLatest { dur ->
                binding.totalTime.text = formatTime(dur)
            }
        }
    }

    private fun formatTime(ms: Int): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.release()
    }
}