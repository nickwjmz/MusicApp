package com.example.musicapp.model

data class PokoMusic(var music: List<PokoMusicInfo>)

data class PokoMusicInfo(var artistName: String,
                         var collectionName: String,
                         var artworkUrl60: String,
                         var trackPrice: String)