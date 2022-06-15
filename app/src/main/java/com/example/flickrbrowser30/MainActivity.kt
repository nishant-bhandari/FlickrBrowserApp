package com.example.flickrbrowser30

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*

// lec 158-160: MATERIAL DESIGNING :introduced in the android Lollipop
// lec 161 : Style Sheets
// lec 163 : Namespace
// lec 169 : Adding the Search Feature

class MainActivity : BaseActivity(), GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {


    private val TAG = "MainActivity"
    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: Called ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activateToolbar(false)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.addOnItemTouchListener(RecyclerItemClickListener(this,recycler_view,this))
        recycler_view.adapter = flickrRecyclerViewAdapter

        Log.d(TAG, "onCreate: Done ")
    }

    override fun onItemClick(view: View?, position: Int) {
        Log.d(TAG,"onItemClick:Starts")
        Toast.makeText(this,"normal tap at position : $position",Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View?, position: Int) {
        Log.d(TAG,"OnItemLongCLick Starts")
//        Toast.makeText(this,"long tap at position : $position",Toast.LENGTH_SHORT).show()
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if(photo != null) {
            val intent = Intent(this, PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER,photo)
            startActivity(intent)
        }
    }

    private fun createUri(
        baseURL: String,
        searchCriteria: String,
        lang: String,
        matchAll: Boolean
    ): String {
        Log.d(TAG, "createUri: Called")

        return Uri.parse(baseURL).buildUpon().appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang).appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1").build().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu: Called ")
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: Called ")
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete: Called")

            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            //download failed
            Log.d(TAG, "onDownloadComplete: Failed with status $status, error message : $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailable: Called")
        flickrRecyclerViewAdapter.loadNewData(data)
        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onError: Called with ${exception.message}")
    }

    override fun onResume() {
        Log.d(TAG,"onResume: Called")
        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY,"")

        if (queryResult != null) {
            if(queryResult.isNotEmpty()) {
                val url = createUri(
                    "https://www.flickr.com/services/feeds/photos_public.gne",
                    queryResult,
                    "en-us",
                    true
                )
                val getRawData = GetRawData(this)
                getRawData.execute(url)
            }
        }
        Log.d(TAG,"onResume: ENDS")
    }
}