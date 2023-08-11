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
import com.app.marier.activities.UserDeatilActivity
import com.app.marier.datamodel.getusermodel.Data
import com.bumptech.glide.Glide
import com.yuyakaido.android.cardstackview.CardStackLayoutManager

class CardStackAdapter(private val activity: FragmentActivity,private val data: List<Data>?) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>(), View.OnClickListener {
    private lateinit var manager: CardStackLayoutManager


    private var viewClick: OnViewClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.relativelay.setOnClickListener {
            var intent = Intent(activity, UserDeatilActivity::class.java)
            intent.putExtra("userid",data?.get(position)?._id)
            activity.startActivity(intent)
        }
        Glide.with(activity)
            .load(data?.get(position)?.avatar).placeholder(R.drawable.ldki)
            .into(holder.img_user!!)

        holder.tvname.text = data!!.get(position).name
        holder.tvqualification.text = data!!.get(position).role
        holder.tvbio.text = data!!.get(position).bio


    }

    public interface OnViewClick {

        fun onitemclick(position: Int)
    }

    fun ViewClickListner(click: OnViewClick) {
        this.viewClick = click
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    fun getSpots():Int {
        return data!!.size

    }




    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var relativelay: LinearLayout = view.findViewById(R.id.relativelay)
        var img_user: ImageView = view.findViewById(R.id.img_user)
        var tvname: TextView = view.findViewById(R.id.tvname)
        var tvqualification: TextView = view.findViewById(R.id.tvqualification)
        var tvdistance: TextView = view.findViewById(R.id.tvdistance)
        var tvbio: TextView = view.findViewById(R.id.tvbio)


    }

    override fun onClick(v: View?) {


    }
}