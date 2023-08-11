package com.app.marier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.datamodel.getcurrentuserbyid.Gallery
import com.bumptech.glide.Glide

class OtherPhotsRecyclerAdapter(private val activity: FragmentActivity,val  gallery: List<Gallery>?) : RecyclerView.Adapter<OtherPhotsRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OtherPhotsRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.otherrecycleritem, parent, false)
        return OtherPhotsRecyclerAdapter.ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return gallery!!.size


    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(activity).load(gallery?.get(position)?.image).into(holder.imguuser)


    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var imguuser = itemView.findViewById<ImageView>(R.id.imguuser)


    }
}