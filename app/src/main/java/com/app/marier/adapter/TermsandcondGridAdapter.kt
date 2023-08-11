package com.app.marier.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.staticmodel.TermsandcondModel

class TermsandcondGridAdapter(
    private val activity: FragmentActivity,
 private val   arrayList: ArrayList<TermsandcondModel>
) : RecyclerView.Adapter<TermsandcondGridAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TermsandcondGridAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.termsandcondgriditem, parent, false)
        return TermsandcondGridAdapter.ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return arrayList.size


    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvtext1.text = arrayList.get(position).textView
        holder.tvtext2.text = arrayList.get(position).textView1



    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvtext1 = itemView.findViewById<TextView>(R.id.tvtext1)
        val tvtext2 = itemView.findViewById<TextView>(R.id.tvtext2)


    }
}