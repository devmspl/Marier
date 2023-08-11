package com.app.marier.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.interfaces.PickiImagesAdapterInterface

class AddphotosRecyclerAdapter (private val activity: FragmentActivity,
                                private val onlclick: PickiImagesAdapterInterface,
                                private val uri: ArrayList<Uri>
) : RecyclerView.Adapter<AddphotosRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddphotosRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.addphotositem, parent, false)
        return AddphotosRecyclerAdapter.ViewHolder(view)
    }


    override fun getItemCount(): Int {
        if (uri.size==0){
            return 4
        }else{
            return uri.size

        }

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imggallery.setOnClickListener{
            onlclick.picimages(position)
        }
        if (uri.size!=0){
            holder.imggallery.setImageURI(uri.get(position))
            holder.imgadd.visibility = View.VISIBLE

        }



    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var imggallery = itemView.findViewById<ImageView>(R.id.imggallery)
        var imgadd = itemView.findViewById<ImageView>(R.id.imgadd)


    }
}