package com.example.musicplayerapp.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.musicplayerapp.data.model.Track
import com.example.musicplayerapp.data.model.toTrack
import com.example.musicplayerapp.data.network.JamendoApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Date: 17 December, 2025
 * MadeBy: Deepshikha Ram
 * Purpose: To build MusicPlayerApp
 * Aim: Creating Music PlayerUsing Kotlin Multiplatform
 */


enum class SortMode {
    NAME,
    DURATION
}

class MusicRepository(
    private val context: Context,
    private val apiClient: JamendoApiClient
) {

    private var cachedTracks: List<Track> = emptyList()

    suspend fun getTracks(sortMode: SortMode): Result<List<Track>> =
        withContext(Dispatchers.IO) {

            if (cachedTracks.isNotEmpty()) {
                return@withContext Result.success(sortTracks(cachedTracks, sortMode))
            }

            if (!isNetworkAvailable()) {
                return@withContext Result.failure(
                    Exception("No internet connection")
                )
            }

            val apiResult = apiClient.fetchTracks()

            apiResult.fold(
                onSuccess = { dtoList ->
                    android.util.Log.d("Repository", "Received ${dtoList.size} tracks")
                    cachedTracks = dtoList.map { it.toTrack() }
                    Result.success(sortTracks(cachedTracks, sortMode))
                },
                onFailure = {
                    android.util.Log.e("Repository", "Fetch failed", it)
                    Result.failure(it)
                }
            )
        }

    private fun sortTracks(
        tracks: List<Track>,
        sortMode: SortMode
    ): List<Track> {
        return when (sortMode) {
            SortMode.NAME ->
                tracks.sortedBy { it.title.lowercase() }

            SortMode.DURATION ->
                tracks.sortedBy { it.duration }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }
}