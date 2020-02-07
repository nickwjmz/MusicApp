package com.example.musicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
// https://itunes.apple.com/search?term=rock&amp;media=music&amp;entity=song&amp;limit=50
// https://itunes.apple.com/search?term=classick&amp;media=music&amp;entity=song&amp;limit=50
// https://itunes.apple.com/search?term=pop&amp;media=music&amp;entity=song&amp;limit=50


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }



/*
 * Probably need to put this in onCreate(). Use for API 29+.
 *  if (ContextCompat.checkSelfPermission(thisActivity,
 *          Manifest.permission.INTERNET && Manifest.permission.ACCESS_NETWORK_STATE)
 *          != PackageManager.PERMISSION_GRANTED) {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                Manifest.permission.INTERNET)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(thisActivity,
                arrayOf(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE),
                MY_PERMISSIONS_REQUEST_INTERNET)

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    } else {
        // Permission has already been granted
    }
  */

}
