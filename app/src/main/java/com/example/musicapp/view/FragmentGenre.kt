package com.example.musicapp.view

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.R
import com.example.musicapp.model.AppleMusicResponse
import com.example.musicapp.presenter.CustomAdapter
import com.example.musicapp.presenter.InterfacePlayer
import kotlinx.android.synthetic.main.layout_fragment_genre.*

class FragmentGenre: Fragment(), InterfacePlayer {

    val customAdapter = CustomAdapter(this)

    var mediaPlayer : MediaPlayer? = null

    // Static reference of object for parent class.
    companion object{
        fun newInstance(): FragmentGenre {
            return FragmentGenre()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.layout_fragment_genre, container, false)
        return view
    }

    fun sendAdapterData(dataSet: AppleMusicResponse, context: Context) {
        layout_recycler_view.layoutManager = LinearLayoutManager(context)
        layout_recycler_view.adapter = customAdapter

        customAdapter.dataSet = dataSet
        customAdapter.notifyDataSetChanged()
    }

    override fun playSong(url: String) {
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer().apply {
            this.setAudioStreamType(AudioManager.STREAM_MUSIC) //to send the object to the initialized state
            setDataSource(url) //to set media source and send the object to the initialized state
            prepare() //to send the object to the prepared state, this may take time for fetching and decoding
            start() //to start the music and send the object to started state

        }
    }
}