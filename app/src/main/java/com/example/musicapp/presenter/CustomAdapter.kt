package com.example.musicapp.presenter

import android.media.AudioManager
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.AppleMusicResponse
import com.squareup.picasso.Picasso

class CustomAdapter(val listener: InterfacePlayer) : RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            CustomViewHolder =
        CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.music_layout,
                parent,
                false
            )
        )

    var dataSet: AppleMusicResponse? = null

    override fun getItemCount() = dataSet?.results?.size ?: 0

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        dataSet?.let {
            holder.tvArtistName.text = it.results[position].artistName
            holder.tvCollectionName.text = it.results[position].collectionName
            holder.tvTrackName.text = it.results[position].trackName
            holder.tvPrice.text = "$ " + it.results[position].trackPrice
            Picasso.get().load(it.results[position].artworkUrl60).into(holder.ivAlbumArt)
            holder.viewGroup.setOnClickListener {
                var url = dataSet!!.results[position].previewUrl // your URL here
                listener.playSong(url)
                }

        }
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvCollectionName: TextView
        var tvArtistName: TextView
        var tvPrice: TextView
        var ivAlbumArt: ImageView
        var tvTrackName: TextView
        var viewGroup: ViewGroup

        init {
            tvCollectionName = itemView.findViewById(R.id.tv_collection_name)
            tvArtistName = itemView.findViewById(R.id.tv_artist_name)
            tvPrice = itemView.findViewById(R.id.tv_music_price)
            ivAlbumArt = itemView.findViewById(R.id.iv_music_image)
            tvTrackName = itemView.findViewById(R.id.tv_track_name)
            viewGroup = itemView.findViewById(R.id.music_holder)
        }
    }
}