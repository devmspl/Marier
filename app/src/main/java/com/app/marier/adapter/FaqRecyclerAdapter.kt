package com.app.marier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.datamodel.getfaq.Data

class FaqRecyclerAdapter(private val activity: FragmentActivity, val data: List<Data>?) : RecyclerView.Adapter<FaqRecyclerAdapter.ViewHolder>() {
var value:Boolean = true
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.faqsamleitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var sampleFaqRecyclerAdapter2 = SampleFaqRecyclerAdapter2(activity,data)
        holder.samplefaqrecyclerview2?.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            adapter = sampleFaqRecyclerAdapter2
        }
        holder.linexpandable.setOnClickListener{
            if (value){
                holder.samplefaqrecyclerview2.visibility  =View.VISIBLE
                holder.imgadd.setImageDrawable(activity.resources.getDrawable(R.drawable.baseline_minimize_24))

            }else{
                holder.samplefaqrecyclerview2.visibility  =View.GONE
                holder.imgadd.setImageDrawable(activity.resources.getDrawable(R.drawable.baseline_add_24))

            }
            value =! value

        }

        holder.tvfaqs.text = data!!.get(position).question

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val samplefaqrecyclerview2: RecyclerView = itemView.findViewById(R.id.samplefaqrecyclerview2)
        val linexpandable: LinearLayout = itemView.findViewById(R.id.linexpandable)
        val imgadd: ImageView = itemView.findViewById(R.id.imgadd)
        val tvfaqs: TextView = itemView.findViewById(R.id.tvfaqs)
    }

}