package com.app.marier.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.marier.R
import com.app.marier.activities.AgoraVideoActivity
import com.app.marier.adapter.ChatListRecyclerAdapter
import com.app.marier.databinding.FragmentChatBinding
import com.app.marier.datamodel.chatuserlist.ChatUserlistModel
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Utils
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONArray
import org.json.JSONObject


class ChatFragment : Fragment() {
    private var binding: FragmentChatBinding? = null
    lateinit var mSocket: Socket
    var userid: String? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        return binding!!.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userid = CSPreferences.readString(requireActivity(), Utils.USERID)
        Utils.showDialogMethod(requireActivity(),requireActivity().fragmentManager)

        try {
            val opts = IO.Options()
            opts.transports = arrayOf<String>(WebSocket.NAME)
            mSocket = IO.socket("http://www.marier.one:9001", opts)
            mSocket.emit("join-chat", userid)


            mSocket.connect()
            mSocket.emit("get-room-list",userid!!)
            mSocket.on("receive-call", receiveCallListener);

            mSocket.on("get-room-list") { args ->
                val userList = args[0] as JSONArray

                Log.d("checklist",userList.toString())



                var arrayList:ArrayList<ChatUserlistModel> = ArrayList()
                for (i in 0 until userList.length()) {
                    val jsonObject = userList.getJSONObject(i)

                    val membersArray = jsonObject.getJSONArray("members")

                    for (j in 0 until membersArray.length()) {
                        val memberObject = membersArray.getJSONObject(j)
                        val memberId = memberObject.getString("_id")
                        val memberName = memberObject.getString("name")
                        val memberAvatar = memberObject.getString("avatar")

                        arrayList.add(ChatUserlistModel(memberName
                            ,memberAvatar,memberId))

                        // Process the member data here
                        // ...

                    }
                }
//                Toast.makeText(requireContext(), arrayList.size.toString(), Toast.LENGTH_SHORT).show()

                Log.d("checklistsize",arrayList.size.toString())
                requireActivity().runOnUiThread {
                    initRecyclerview(arrayList)
                    Utils.hideDialog()
                }



            }

            //Register all the listener and callbacks here.
            mSocket.on(Socket.EVENT_CONNECT, onConnect)
            mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectionError)




        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }
    }

    private fun initRecyclerview(arrayList: ArrayList<ChatUserlistModel>) {
        val chatListRecyclerAdapter = ChatListRecyclerAdapter(activity as FragmentActivity,arrayList)
        binding!!.chatlistrecyclerview?.apply {
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = chatListRecyclerAdapter
        }


    }
    var onConnect = Emitter.Listener {
        requireActivity()!!.runOnUiThread {
            Toast.makeText(requireActivity(), "connected", Toast.LENGTH_SHORT).show()
        }


    }


    private val onConnectionError = Emitter.Listener {
        requireActivity()!!.runOnUiThread {
            Toast.makeText(requireActivity()!!, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    val receiveCallListener = Emitter.Listener { args ->
        // Handle the received data here
        val receivedData = args[0] as JSONObject
        // Process the received data as needed
        // Example: Extract values from the received data
        val senderId = receivedData.getString("callFrom")
//        val receiverId = receivedData.getString("receiverId")
        val name = receivedData.getString("name")
//        val channelName = receivedData.getString("channelName")
        Log.d("checkingvalueforecieve",senderId)

        requireActivity().runOnUiThread{
            showDialog(name)
        }


//        com.app.marier.utils.Utils.showDialogMethod(requireActivity(),requireActivity().fragmentManager)
    }


    private fun showDialog(name: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pickupcall)
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val tvcallfrom = dialog.findViewById<View>(R.id.tvcallfrom) as TextView
        val btnanswer = dialog.findViewById<View>(R.id.btnanswer) as Button
//        val gender: String = CSPreferences.readString(activity, Utils.GENDERSELECT)

        btnanswer.setOnClickListener{
            startActivity(Intent(requireActivity(),AgoraVideoActivity::class.java))
        }

        tvcallfrom.text = name

        tvcallfrom.setOnClickListener {


        }
        val btn_cancel = dialog.findViewById<View>(R.id.btn_cancel) as Button
        btn_cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


}