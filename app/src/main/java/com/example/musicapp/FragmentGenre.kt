package com.example.musicapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.layout_fragment_genre.*

class FragmentGenre: Fragment() {

    val customAdapter = CustomAdapter()

    // Static reference of object for parent class.
    companion object{
        fun newInstance(): FragmentGenre{
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
}