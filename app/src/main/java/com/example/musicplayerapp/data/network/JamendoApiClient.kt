package com.example.musicplayerapp.data.network


import com.example.musicplayerapp.data.model.TrackDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath

import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


/**
 * Date: 17 December, 2025
 * MadeBy: Deepshikha Ram
 * Purpose: To build MusicPlayerApp
 * Aim: Creating Music PlayerUsing Kotlin Multiplatform
 */

/**
 * API response wrapper
 */
@Serializable
data class JamendoResponse(
    val results: List<TrackDto>
)

/**
 * Ktor API Client
 */
class JamendoApiClient(
    private val apiKey: String
) {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun fetchTracks(): Result<List<TrackDto>> {
        return try {
            val response: JamendoResponse = client.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.jamendo.com"
                    encodedPath = "/v3.0/tracks"
                    parameters.append("client_id", apiKey)
                    parameters.append("format", "json")
                    parameters.append("limit", "20")
                    parameters.append("include", "musicinfo")
                    parameters.append("audioformat", "mp31")
                    parameters.append("order", "popularity_week")
                }
            }.body()
            android.util.Log.d("Jamendo", "Fetched ${response.results.size} tracks")

            Result.success(response.results)

        } catch (e: Exception) {
            android.util.Log.e("Jamendo", "API Error", e)
            Result.failure(e)
        }
    }
}