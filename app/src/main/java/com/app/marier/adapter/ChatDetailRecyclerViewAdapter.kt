package com.app.marier.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.activities.ChatDetailActivity
import com.app.marier.datamodel.socket.Messages

class ChatDetailRecyclerViewAdapter(
    private val messageList: MutableList<Messages>,
    val chatDetailActivity: ChatDetailActivity

) :
    RecyclerView.Adapter<ChatDetailRecyclerViewAdapter.ViewHolder>() {
    private lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        var layout = -1
        when (viewType) {
            Messages.TYPE_MESSAGE -> layout = R.layout.item_message
            Messages.TYPE_MESSAGE_REMOTE -> layout = R.layout.itemmessageleft
            Messages.TYPE_ACTION -> layout = R.layout.item_action
//            Messages.TYPE_IMAGE -> layout = R.layout.item_image
        }
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]


        if (message.getmType() == Messages.TYPE_MESSAGE_REMOTE) {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            holder.rlMessage.layoutParams = params
            holder.setMessage(message.getmMessage())





        } else if (message.getmType() == Messages.TYPE_MESSAGE) {
            if (messageList.get(position).imageuri!=null){
                val widthInDp = 200f
                val heightInDp = 200f
                val widthInPixels = holder.dpToPxss(widthInDp)
                val heightInPixels = holder.dpToPxss(heightInDp)
                val params = RelativeLayout.LayoutParams(widthInPixels, heightInPixels)
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                holder.imgrecieve.layoutParams = params
                holder.imgrecieve.setImageURI(messageList.get(position).imageuri)
                holder.imgrecieve.visibility = View.VISIBLE
                holder.rlMessage.visibility = View.GONE
                holder.linseekbar.visibility = View.GONE
                holder.tvpdfname.visibility = View.GONE
                holder.imgrecieve.setOnLongClickListener{
                    holder.showPopupMenu(position)
                    true
                }

            }else if (messageList.get(position).recordaudiofile!=null){
                holder.linseekbar.visibility = View.VISIBLE
                holder.texttotalduration.setText(ChatDetailActivity.timerString)
                val widthInDp = 200f
                val heightInDp = 40f
                val widthInPixels = holder.dpToPxss(widthInDp)
                val heightInPixels = holder.dpToPxss(heightInDp)
                val params = RelativeLayout.LayoutParams(widthInPixels, heightInPixels)
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                holder.linseekbar.layoutParams = params
                holder.rlMessage.visibility = View.GONE
                holder.linpdf.visibility = View.GONE

                holder.linseekbar.setOnLongClickListener {
                    holder.showPopupMenu(position)
                    true
                }



            }else if (messageList.get(position).pdffile!=null){
                holder.linseekbar.visibility = View.VISIBLE
                val widthInDp = 200f
                val heightInDp = 40f
                val widthInPixels = holder.dpToPxss(widthInDp)
                val heightInPixels = holder.dpToPxss(heightInDp)
                val params = RelativeLayout.LayoutParams(widthInPixels, heightInPixels)
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                holder.linpdf.layoutParams = params
                holder.tvpdfname.text = messageList.get(position).pdffile
                holder.linpdf.visibility = View.VISIBLE
                holder.linseekbar.visibility = View.GONE
                holder.imgrecieve.visibility = View.GONE
                holder.rlMessage.visibility = View.GONE

            }

            else{
                holder.linseekbar.visibility = View.GONE
                holder.imgrecieve.visibility = View.GONE
                holder.linpdf.visibility = View.GONE
                holder.rlMessage.visibility = View.VISIBLE
                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                holder.rlMessage.layoutParams = params
                holder.setMessage(message.getmMessage())
               holder.rlMessage.setOnLongClickListener{
                   holder.showPopupMenu(position)
                   true
               }
            }



//            holder.rlMessage.setBackground(context.getResources().getDrawable(R.drawable.strokeedittextcorner))

        }

        holder.img_playpause.setOnClickListener{
            chatDetailActivity.prepareMediaPlayer(messageList.get(position).recordaudiofile,holder.playerseekbar)
        }


    }

    override fun getItemCount(): Int {

        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return messageList[position].getmType()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtmessage: TextView = itemView.findViewById(R.id.txtmessage)
        val rlMessage: RelativeLayout = itemView.findViewById(R.id.rlMessage)
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val imgrecieve: ImageView = itemView.findViewById(R.id.imgrecieve)
        val linseekbar: LinearLayout = itemView.findViewById(R.id.linseekbar)
        val img_playpause: ImageView = itemView.findViewById(R.id.img_playpause)
        val texttotalduration: TextView = itemView.findViewById(R.id.texttotalduration)
        val playerseekbar: SeekBar = itemView.findViewById(R.id.playerseekbar)
        val tvpdfname: TextView = itemView.findViewById(R.id.tvpdfname)
        val linpdf: LinearLayout = itemView.findViewById(R.id.linpdf)

        fun setTime(time: String) {
            if (txtTime == null) return
            txtTime.text = time
        }

        fun setMessage(message: String) {
            if (txtmessage == null) return
            txtmessage.text = message
        }

        fun dpToPxss(dp: Float): Int {
            val density = context.resources.displayMetrics.density
            return (dp * density + 0.5f).toInt()
        }

         fun showPopupMenu(position: Int) {
            val popupMenu = PopupMenu(context, linseekbar)
            popupMenu.inflate(R.menu.messagedeletemenu)
            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.deletemessage -> {
                        if (position >= 0 && position < messageList.size) {
                            messageList.removeAt(position)
                            notifyItemRemoved(position)
                        }
                        true
                    }

                    R.id.cancel -> {
                        popupMenu.dismiss()
                        true
                    }

                    else -> false
                }
            }
            popupMenu.show()
        }


    }
}