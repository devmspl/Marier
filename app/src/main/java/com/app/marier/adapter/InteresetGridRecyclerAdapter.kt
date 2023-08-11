package com.app.marier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.datamodel.getinteresetByUserId.Data

class InteresetGridRecyclerAdapter(
    private val activity: FragmentActivity,
    val data: List<Data>?
) : RecyclerView.Adapter<InteresetGridRecyclerAdapter.ViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InteresetGridRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.interestitemdata2, parent, false)
        return InteresetGridRecyclerAdapter.ViewHolder(view)
    }


    override fun getItemCount(): Int {
        if (data!!.size>6)return 6 else  return data!!.size

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvinterset.text = data?.get(position)?.type


    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvinterset = itemView.findViewById<TextView>(R.id.tvinterset)


    }
}