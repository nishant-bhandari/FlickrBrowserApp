package com.example.flickrbrowser30

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrImageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.title)
}

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>): RecyclerView.Adapter<FlickrImageViewHolder>() {
    //tag supports only maximum 23 characters
    private val TAG = "FlickrRecyclerViewAdapt"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        //called by the layout manager when it needs a new view
        Log.d(TAG,"onCreateViewHolder: new view Requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickrImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        //Called by the layout manager when it wants new data in an existing view
        if(photoList.isEmpty()) {
            holder.thumbnail.setImageResource(R.drawable.placeholder)
            holder.title.setText(R.string.empty_photo)
        }
        else {
            val photoItem = photoList[position]
//        Log.d(TAG,"onBindViewHolder: Called ${photoItem.title} --> $position")
            Picasso.get().load(photoItem.image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail)
            holder.title.text = photoItem.title
        }
    }

    override fun getItemCount(): Int {
//        Log.d(TAG,"getItemCount: Called")
        return if(photoList.isNotEmpty()) photoList.size else 1
    }
    fun loadNewData(newPhotos: List<Photo>) {
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if(photoList.isNotEmpty()) photoList[position] else null
    }
}