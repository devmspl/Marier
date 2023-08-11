package com.app.marier.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.app.marier.R
import com.app.marier.datamodel.getusermodel.Data
import com.app.marier.interfaces.DiscoverFragClickListener
import com.bumptech.glide.Glide

class GridViewAdapterDiscoverScreen(
    private val context: Context,
    private val discoverFragClickListener: DiscoverFragClickListener,
    private val data: List<Data>?
) : BaseAdapter() {

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
            view = LayoutInflater.from(context).inflate(R.layout.discoverecycleritem, parent, false)
        }



//
        val limain = view?.findViewById<LinearLayout>(R.id.limain)
        val tvname = view?.findViewById<TextView>(R.id.tvname)
        val imguser = view?.findViewById<ImageView>(R.id.imguser)

        Glide.with(context)
            .load(data?.get(position)?.avatar).placeholder(R.drawable.baseline_person_24)
            .into(imguser!!)

        limain?.setOnClickListener{
            discoverFragClickListener.passclick()

        }
        tvname?.text =data?.get(position)?.name


        return view!!
    }
}