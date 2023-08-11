package com.app.marier.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.app.marier.R
import com.app.marier.datamodel.getlikeotheruser.Data

class LikesGridAdapter(private val context: Context,val  data: List<Data>?) : BaseAdapter() {

    // Return the number of items in the data set
    override fun getCount(): Int {
        return data!!.size
    }

    // Return an item at a specific position in the data set
    override fun getItem(position: Int): Any {
        return data!!.size
    }

    // Return the ID of an item at a specific position in the data set
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // Create a new view to display an item in the data set
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.likerecycleritem, parent, false)
        }

        // Get the item at the current position
//        val item = getItem(position) as MyItem
//
//        // Set the text and image of the item view

        val tvname = view?.findViewById<TextView>(R.id.tvname)
        tvname?.text =data?.get(position)?.likeBy?.name

//        textView.text = item.text
//        val imageView = view.findViewById<ImageView>(R.id.my_image_view)
//        imageView.setImageResource(item.imageResId)

        return view!!
    }
}
