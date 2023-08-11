package com.app.marier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.datamodel.getallinterest.Data
import com.app.marier.interfaces.InterestRecyclerClickListener
import com.app.marier.interfaces.InterestRecyclerClicklistner

class InterestRecyclerAdapter(
    private val activity: FragmentActivity,
    private val data: List<Data>?,
    private val interestRecyclerClickListener: InterestRecyclerClickListener
) : RecyclerView.Adapter<InterestRecyclerAdapter.ViewHolder>() {
    private var selectedPositions: MutableSet<Int> = HashSet()
    private var arrayList:ArrayList<String> = ArrayList()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.interestitemdata, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return data?.size ?: 0
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = data?.get(position)

        holder.tvinterset.text = currentItem?.type
        holder.tvinterset.isSelected = selectedPositions.contains(position)

        holder.tvinterset.setOnClickListener {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position)
                arrayList.remove(data?.get(position)?._id) // Remove from the arrayList
                holder.tvinterset.isSelected = false
                holder.tvinterset.setBackgroundDrawable(activity.getDrawable(R.drawable.roundcornerstroke))
                holder.tvinterset.setTextColor(activity.getColor(R.color.black))
            } else {
                selectedPositions.add(position)
                arrayList.add(data?.get(position)?._id.toString()) // Add to the arrayList
                holder.tvinterset.isSelected = true
                holder.tvinterset.setBackgroundDrawable(activity.getDrawable(R.drawable.appcolorroundcorner))
                holder.tvinterset.setTextColor(activity.getColor(R.color.white))
            }

            interestRecyclerClickListener.onItemClick(position)
            interestRecyclerClickListener.passdata(arrayList)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linmain = itemView.findViewById<LinearLayout>(R.id.linmain)
        var tvinterset = itemView.findViewById<TextView>(R.id.tvinterset)
    }
}
