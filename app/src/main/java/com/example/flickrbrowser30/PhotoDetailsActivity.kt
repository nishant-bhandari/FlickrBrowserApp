package com.example.flickrbrowser30

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_details.*

class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)

        activateToolbar(true)

        // for Serializable:
//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo

        // for Parcelable :
        val photo = intent.getParcelableExtra<Photo>(PHOTO_TRANSFER) as Photo

//        photo_title.text = "title: " + photo.title
        photo_title.text = resources.getString(R.string.photo_title_text,photo.title)
//        photo_tags.text = "tags: " +photo.tags
        photo_tags.text = resources.getString(R.string.photo_tags_text,photo.tags)

        photo_author.text = photo.author
//        photo_author.text = resources.getString(R.string.photo_author_text, "my","red","car")

        Picasso.get().load(photo.link)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(photo_image)

    }
}