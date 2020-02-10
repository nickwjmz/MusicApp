package com.example.musicapp.view

import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.musicapp.model.AppleMusicResponse
import com.example.musicapp.model.MusicItem
import com.example.musicapp.R
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
// https://itunes.apple.com/search?term=rock&amp;media=music&amp;entity=song&amp;limit=50
// https://itunes.apple.com/search?term=classick&amp;media=music&amp;entity=song&amp;limit=50
// https://itunes.apple.com/search?term=pop&amp;media=music&amp;entity=song&amp;limit=50

    val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout: TabLayout = findViewById(R.id.tabs)
        val tab1: TabItem? = tab_rock_music
        val tab2: TabItem? = tab_classic_music
        val tab3: TabItem? = tab_pop_music

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                val uri = buildURLRequest(
                    // This sets QUERY_PARAM equal to the text value specified in each tab in XML
                    p0!!.text.toString()
                )
                MyAsynctask().execute(uri)
            }

        })

        tabLayout.getTabAt(1)!!.select()
    }

    fun buildURLRequest(genre: String): Uri {
        val url =
            "https://itunes.apple.com/search?term=rock&amp;media=music&amp;entity=song&amp;limit=50"
        val BASE_URL = "https://itunes.apple.com/search?"
        val REST = "&amp;media=music&amp;entity=song&amp;limit=50"
        val QUERY_PARAM = "term"
        return Uri.parse(
            BASE_URL
        ).buildUpon()
            .appendQueryParameter(QUERY_PARAM, genre)
            .appendPath(REST)
            .build()
    }

    fun checkNetworkConnectivity(): Boolean {
        val connectivityManager =
            getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    /*  override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers))
            )
            Response.success(
                gson.fromJson(json, clazz),
                HttpHeaderParser.parseCacheHeaders(response)
            )
        }
        // handle errors*/
// ...

    @Throws(IOException::class)
    fun getConnection(uriPath: Uri): HttpURLConnection {
        val url = URL(uriPath.toString())
        val connection =
            url.openConnection() as HttpURLConnection
        connection.readTimeout = 10000
        connection.connectTimeout = 15000
        connection.requestMethod = "GET"
        connection.doInput = true
        return connection
    }

    @Throws(java.lang.Exception::class)
    fun getMusic(connection: HttpURLConnection): String? {
        connection.connect()
        Log.d(TAG, "getMusic: " + connection.responseCode)
        val inputStream = connection.inputStream
        val input = parseIS(inputStream)
        Log.d(TAG, "getMusic: $input")
        return input
    }

    @Throws(IOException::class)
    fun parseIS(inputStream: InputStream): String? {
        val stringBuilder = StringBuilder()
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = bufferedReader.readLine()

        while (line != null) {
            stringBuilder.append(line + "\n")
            line = bufferedReader.readLine()
        }
        //Log.d(TAG, "parseIS= "+stringBuilder.toString())
        //return if (stringBuilder.isEmpty()) null else stringBuilder.toString()
        return stringBuilder.toString()
    }

    fun convertToDataClass(data: String): AppleMusicResponse {
        val JSONObject = JSONObject(data)
        val dataMusicResponse: AppleMusicResponse
        val listOfMusic = mutableListOf<MusicItem>()
        var musicItem: MusicItem
        val jsonArray = JSONObject.getJSONArray("results")

        for (result in 0 until jsonArray.length()) {
            var jsonMusic = jsonArray.getJSONObject(result)
            var trackName = try {
                jsonMusic.getString("trackName")
            } catch (exception: JSONException) {
                "Track title not specified"
            }
            var collectionName = try {
                jsonMusic.getString("collectionName")
            } catch (exception: JSONException) {
                "Collection not specified"
            }
            var artistName = try {
                jsonMusic.getString("artistName")
            } catch (exception: JSONException) {
                "Artist Not Specified"
            }
            var trackPrice = try {
                jsonMusic.getString("trackPrice")
            } catch (exception: JSONException) {
                "None"
            }
            var previewUrl = try {
                jsonMusic.getString("previewUrl")
            }catch (exception: JSONException) {
                "Song preview not available"
            }
            musicItem = MusicItem(
                trackName,
                collectionName,
                artistName,
                jsonMusic.getString("artworkUrl60"),
                trackPrice,
                jsonMusic.getString("previewUrl")
            )
            listOfMusic.add(musicItem)
        }
        dataMusicResponse =
            AppleMusicResponse(listOfMusic)
        return dataMusicResponse
    }

    /*
     * First parameter defines params for doInBackground().
     * 2nd param defines parameters for onProgressUpdate()
     * 3rd parameter defines the return type for doInBackground() which must match the parameter
     * of onPostExecute(). Use void as parameter if not implementing one of the 3 methods.
     */
    internal inner class MyAsynctask : AsyncTask<Uri, Int, String?>() {
        // Post back progress for a progress bar.
        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            // progressBar.setValue(values[0])
        }

        // Post data back to the main thread
        override fun onPostExecute(value: String?) {
            super.onPostExecute(value)
            //todo put it in an Adapter for the RecyclerView
            val dataSet = convertToDataClass(value!!)
            val fragmentManager = supportFragmentManager
            val fragmentGenre = FragmentGenre.newInstance()
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentGenre).commitNow()
            fragmentGenre.sendAdapterData(dataSet, this@MainActivity)
            Log.d(TAG, "onPostExecute: ${value.let { it }}")
        }

        // Create a worker thread (Separate thread).
        override fun doInBackground(vararg uris: Uri): String? {
            // TODO("not implemented") To change body of created functions use File | Settings | File Templates.
            var connection: HttpURLConnection?
            var value: String? = null
            try {
                Log.d(TAG, "doInBackground: " + uris[0])
                connection = getConnection(uris[0])
                value = getMusic(connection)
                Log.d(TAG, "doInBackground: $value")
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return value
        }
    }
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


