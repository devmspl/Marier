package com.app.marier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.interfaces.ItemClickListener

class SexualRecylerAdapter(
    private val activity: FragmentActivity,
    private val arrayList: List<String>,

) : RecyclerView.Adapter<SexualRecylerAdapter.ViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.genderrecyleritem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayList[position]
        holder.tvgender.text = currentItem
        holder.radioButton.isChecked = position == selectedPosition

// set listener on radio button
        holder.radioButton.setOnCheckedChangeListener { compoundButton, b ->
// check condition
            if (b) {
// When checked
// update selected position
                selectedPosition = holder.adapterPosition
// Call listener

            }
        }


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvgender: TextView = itemView.findViewById(R.id.tvgender)
        val radioButton: RadioButton = itemView.findViewById(R.id.radiobutton)
    }
}