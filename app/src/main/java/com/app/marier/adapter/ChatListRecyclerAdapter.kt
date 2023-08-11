package com.app.marier.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.activities.ChatDetailActivity
import com.app.marier.datamodel.chatuserlist.ChatUserlistModel
import com.bumptech.glide.Glide

class ChatListRecyclerAdapter(
    private val activity: FragmentActivity,
   private val arrayList: ArrayList<ChatUserlistModel>
) : RecyclerView.Adapter<ChatListRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatListRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chatlistitem, parent, false)
        return ChatListRecyclerAdapter.ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvname.text  = arrayList.get(position).memberName
        holder.linmain.setOnClickListener{
            var intent = Intent(activity,ChatDetailActivity::class.java)
            intent.putExtra("userid",arrayList.get(position).userid)
            intent.putExtra("username",arrayList.get(position).memberName)
            intent.putExtra("userimage",arrayList.get(position).memberAvatar)
            activity.startActivity(intent)
        }
        Glide.with(activity)
            .load(arrayList?.get(position)?.memberAvatar).placeholder(R.drawable.ldki)
            .into(holder.imguser!!)


    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var linmain = itemView.findViewById<LinearLayout>(R.id.linmain)
        var tvname = itemView.findViewById<TextView>(R.id.tvname)
        var imguser = itemView.findViewById<ImageView>(R.id.imguser)


    }
}