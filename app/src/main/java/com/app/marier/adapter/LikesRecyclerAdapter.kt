package com.app.marier.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.activities.UserDeatilActivity

class LikesRecyclerAdapter (private val activity: FragmentActivity) : RecyclerView.Adapter<LikesRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LikesRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.likerecycleritem, parent, false)
        return LikesRecyclerAdapter.ViewHolder(view)
    }


    override fun getItemCount(): Int {

        return 20


    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.limain.setOnClickListener{
            var intent = Intent(activity, UserDeatilActivity::class.java)

            activity.startActivity(intent)
        }



    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val limain = itemView.findViewById<LinearLayout>(R.id.limain)


    }
}