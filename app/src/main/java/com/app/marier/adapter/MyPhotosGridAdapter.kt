package com.app.marier.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.app.marier.R
import com.app.marier.datamodel.getcurrentuserbyid.Gallery
import com.bumptech.glide.Glide

class MyPhotosGridAdapter(private val context: Context,val  gallery: List<Gallery>?) : BaseAdapter() {

    // Return the number of items in the data set
    override fun getCount(): Int {
        return gallery!!.size
    }

    // Return an item at a specific position in the data set
    override fun getItem(position: Int): Any {
        return gallery!!.size
    }

    // Return the ID of an item at a specific position in the data set
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Create a new view to display an item in the data set
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.myphotositem, parent, false)
        }

        // Get the item at the current position
//        val item = getItem(position) as MyItem
//
//        // Set the text and image of the item view
//        val textView = view.findViewById<TextView>(R.id.my_text_view)
//        textView.text = item.text
        val userimages = view?.findViewById<ImageView>(R.id.userimages)
        Glide.with(context)
            .load(gallery?.get(position)?.image)
            .into(userimages!!)

        return view!!
    }
}