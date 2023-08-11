package com.app.marier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.datamodel.getfaq.Data

class SampleFaqRecyclerAdapter2(private val activity: FragmentActivity,val  answer: List<Data>?) : RecyclerView.Adapter<SampleFaqRecyclerAdapter2.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.samplerecycleritem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = answer!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvans.text = answer?.get(position)?.answer
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvans: TextView = itemView.findViewById(R.id.tvans)
    }
}