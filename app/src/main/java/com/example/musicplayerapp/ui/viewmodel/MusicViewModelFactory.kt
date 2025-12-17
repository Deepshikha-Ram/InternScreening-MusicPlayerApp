package com.example.musicplayerapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayerapp.data.repository.MusicRepository

/**
 * Date: 17 December, 2025
 * MadeBy: Deepshikha Ram
 * Purpose: To build MusicPlayerApp
 * Aim: Creating Music PlayerUsing Kotlin Multiplatform
 */


class MusicViewModelFactory(
    private val repository: MusicRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MusicViewModel::class.java)) {
            return MusicViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}