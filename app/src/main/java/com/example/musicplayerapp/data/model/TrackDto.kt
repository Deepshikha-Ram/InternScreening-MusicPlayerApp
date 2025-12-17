package com.example.musicplayerapp.data.model

import kotlinx.serialization.Serializable


/**
 * Date: 17 December, 2025
 * MadeBy: Deepshikha Ram
 * Purpose: To build MusicPlayerApp
 * Aim: Creating Music PlayerUsing Kotlin Multiplatform
 */

/**
 * Raw API response model
 */
@Serializable
data class TrackDto(
    val id: Int,
    val name: String,
    val duration: Int,
    val artist_name: String,
    val image: String? = null,
    val audio: String
)

/**
 * UI model used across the app
 */
data class Track(
    val id: Int,
    val title: String,
    val artist: String,
    val duration: Int,
    val thumbnail: String?,
    val audioUrl: String
)

/**
 * Mapper extension
 */
fun TrackDto.toTrack(): Track {
    return Track(
        id = id,
        title = name,
        artist = artist_name,
        duration = duration,
        thumbnail = image,
        audioUrl = audio
    )
}
