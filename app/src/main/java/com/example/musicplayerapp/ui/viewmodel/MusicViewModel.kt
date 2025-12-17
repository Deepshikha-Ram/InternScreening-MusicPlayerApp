package com.example.musicplayerapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayerapp.data.model.Track
import com.example.musicplayerapp.data.repository.MusicRepository
import com.example.musicplayerapp.data.repository.SortMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * Date: 17 December, 2025
 * MadeBy: Deepshikha Ram
 * Purpose: To build MusicPlayerApp
 * Aim: Creating Music PlayerUsing Kotlin Multiplatform
 */


sealed class MusicUiState {
    object Loading : MusicUiState()
    data class Success(val tracks: List<Track>) : MusicUiState()
    data class Error(val message: String) : MusicUiState()
}

class MusicViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MusicUiState>(MusicUiState.Loading)
    val uiState: StateFlow<MusicUiState> = _uiState

    private var currentSortMode = SortMode.NAME

    init {
        loadTracks()
    }

    fun loadTracks() {
        _uiState.value = MusicUiState.Loading

        viewModelScope.launch {
            repository.getTracks(currentSortMode)
                .onSuccess { tracks ->
                    _uiState.value = MusicUiState.Success(tracks)
                }
                .onFailure { error ->
                    _uiState.value =
                        MusicUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    fun toggleSortMode() {
        currentSortMode =
            if (currentSortMode == SortMode.NAME)
                SortMode.DURATION
            else
                SortMode.NAME

        loadTracks()
    }

    fun getCurrentSortMode(): SortMode = currentSortMode
}