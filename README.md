Android Developer Intern - Screening Assignment (This is the Project given to work on)
Music Player App using Kotlin Multiplatform (KMP) Goal: Build a simple music player app that demonstrates your Kotlin and Android fundamentals.

Requirements
Core Features (Must Have)
1. Fetch Audio List from API
○ Use this free API: Free Music Archive API OR Jamendo API
○ Alternative: AudioMack API, or if either doesn't work, feel free to use something else.
● Display list of tracks with: Title, Artist, Duration, Thumbnail (if available)
● Handle loading states (show progress indicator)
● Handle API errors gracefully
3. Sorting Functionality
○ Implement at least 2 sorting options:
■ Sort by name (A-Z)
■ Sort by duration (shortest to longest)
○ Add a way to toggle between sort modes (buttons/menu/chips)
4. Audio Playback
○ Use Android's MediaPlayer to stream selected audio
○ Implement basic controls:
■ Play/Pause
■ Show current playback position
■ Display total duration
○ Properly handle MediaPlayer lifecycle (release resources)
○ Handle network streaming errors
5. Networking
○ Use Ktor for API calls
○ Implement proper error handling for network failures
○ Parse JSON responses correctly
Code Quality Requirements
● Use Kotlin
● Follow MVVM or MVI pattern (ViewModel + LiveData/StateFlow)
● Clean code structure with separation of concerns
● Handle configuration changes (screen rotation)
● Use Coroutines for async operations

What is done in this app ?
Purpose of this app: 
MusicPlayerApp is a simple music streaming app that fetches tracks from Jamendo API.
Users can :
>See a list of trackswith title, artist, duration thumbnail
>Play/Pause Music
>Sort tracks by name and duration

Key Features:
>Kotlin Multiplatform ready code.
>Uses Jetpack COmponents: View Model, LiveData/ StateFlow
>Handles network requests asynchronously with Ktor + coroutines
>Smooth UI rendering with RecyclerView + Glide for images
>Media Playback handled by a custom MusicPlayerManager using Media Player API

Follows MVVM pattern:
Layer: Model
Responsibility: Data classes- TrackDto, Track. Handles API response mapping.

Layer: Repository
Responsibility: MusicRepository fetches data from Jamendo API, caches tracks, sorts tracks, checks network

Layer: ViewModel
Responsibility: MusicViewModel exposes UI state as a StateFlow<MusicUiState> to UI. Handles business logic like sorting tracks.

Layer: View
Responsibility: MainActivity observes uiState, renders RecyclerView, handles click events and, interacts with MusicPlayerManager.

Why MVVM?
>Decouples UI UI from data logic
>Easier to unit test ViewModel and Repository
>Makes UI reactive with StateFlow

Networking:
>Uses Ktor Client with ContentNegotiation + kotlinx.serialization to parse JSON.
>API Client: JamendoApiClient
	> Fetches top tracks
	> Maps TrackDto -> Track for UI
	> Handles exceptions with Result wrapper
>Caching:
	>cachedTracks prevents unnecessary API calls

RecyclerView Adapter:
> TrackAdapter shows a list of tracks:
	>Uses DiffUtil for efficient updates
	>Show thumbnail with Glide
	>Formats duration(mm:ss)
> Click listener triggers playback via MusicPlayerManager


Media Playback:
>MusicPlayerManager wraps Android MediaPlayer.
>Exposes StateFlow for:
	>isPlaying
	>currentPosition
	>duration
>MainActivity observes flows and updates UI:
	>Play/Pause Button
	>SeekBar (optional)
	>Current Time/ Total Time


Permissions:
>  <uses-permission android:name="android.permission.INTERNET" /> in AndroidManifest
> Needed for API requests and streaming audio

Handling Errors:
> Network unavailable -> shows error Toast
> API failure -> shows error Toast
> UI loading state -> progressbar displayed while fetching tracks


BuildConfig Usage:
>Stores Jamendo API Key securely in build.gradle.kts:   buildConfigField("String", "JAMENDO_API_KEY", "\"YOUR_API_KEY\"")
>Used in JamendoApiClient to avoid hardcoding in codebase.


MusicPlayerApp - Architecture Diagram (MVVM)

┌──────────────────────────────┐
│        MainActivity          │
│  (UI / View Layer)           │
│                              │
│ - RecyclerView               │
│ - Play / Pause Button        │
│ - Sort Button                │
│ - Progress Bar               │
│                              │
│ Observes StateFlow            │
└──────────────▲───────────────┘
               │
               │ uiState (StateFlow)
               │
┌──────────────┴───────────────┐
│        MusicViewModel        │
│   (Business Logic Layer)     │
│                              │
│ - Holds MusicUiState         │
│ - toggleSortMode()           │
│ - Calls repository           │
│                              │
│ No Android UI reference      │
└──────────────▲───────────────┘
               │
               │ getTracks()
               │
┌──────────────┴───────────────┐
│       MusicRepository        │
│     (Data Management)        │
│                              │
│ - Checks network             │
│ - Caches tracks              │
│ - Sorts data                 │
│ - Maps DTO → UI model        │
└──────────────▲───────────────┘
               │
               │ fetchTracks()
               │
┌──────────────┴───────────────┐
│      JamendoApiClient        │
│     (Networking Layer)       │
│                              │
│ - Ktor HTTP Client           │
│ - JSON Serialization         │
│ - Calls Jamendo API          │
└──────────────▲───────────────┘
               │
               │ JSON Response
               │
┌──────────────┴───────────────┐
│          Jamendo API         │
│   (Remote Music Server)      │
└──────────────────────────────┘


MusicPlayBackFlow
RecyclerView Item Click
        │
        ▼
TrackAdapter
        │
        ▼
MusicPlayerManager
        │
        ▼
Android MediaPlayer
        │
        ▼
Audio Stream (Jamendo)

