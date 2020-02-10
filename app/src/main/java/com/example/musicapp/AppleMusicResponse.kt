package com.example.musicapp

data class AppleMusicResponse(var results: List<MusicItem>)

data class MusicItem(var trackName: String,
                     var artistName: String,
                     var collectionName: String,
                     var artworkUrl60: String,
                     var trackPrice: String,
                     var previewUrl: String)