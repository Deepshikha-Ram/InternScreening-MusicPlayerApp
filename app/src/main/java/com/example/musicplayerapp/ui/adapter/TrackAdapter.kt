package com.example.musicplayerapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayerapp.data.model.Track
import com.example.musicplayerapp.databinding.ItemTrackBinding


/**
 * Date: 17 December, 2025
 * MadeBy: Deepshikha Ram
 * Purpose: To build MusicPlayerApp
 * Aim: Creating Music PlayerUsing Kotlin Multiplatform
 */

class TrackAdapter(
    private val onItemClick: (Track) -> Unit
) : ListAdapter<Track, TrackAdapter.TrackViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TrackViewHolder(
        private val binding: ItemTrackBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.title.text = track.title
            binding.artist.text = track.artist
            binding.duration.text = formatDuration(track.duration)

            Glide.with(binding.root.context)
                .load(track.thumbnail)
                .centerCrop()
                .into(binding.thumbnail)

            binding.root.setOnClickListener {
                onItemClick(track)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Track>() {
            override fun areItemsTheSame(old: Track, new: Track): Boolean =
                old.id == new.id

            override fun areContentsTheSame(old: Track, new: Track): Boolean =
                old == new
        }
    }

    private fun formatDuration(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}