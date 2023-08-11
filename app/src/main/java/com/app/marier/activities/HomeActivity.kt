package com.app.marier.activities

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.app.marier.R
import com.app.marier.datamodel.chatuserlist.ChatUserlistModel
import com.app.marier.repository.UserModuleResponseRepository
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.factories.USerViewModelFactory
import com.app.marier.utils.media.Utils
import com.app.marier.viewmodel.UserModuleviewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONArray
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    var navController: NavController?=null
    lateinit var viewModel: UserModuleviewModel
    lateinit var mSocket: Socket
    var userid: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        FirebaseMessaging.getInstance().subscribeToTopic("topic_name")


        navController = Navigation.findNavController(this,R.id.fragmentContainerView3)
        HomeActivity.bottomNavigationView  = findViewById(R.id.bottomnav)
        val repository = UserModuleResponseRepository()

        NavigationUI.setupWithNavController(bottomNavigationView!!,navController!!)
        val viewModelProviderFactory =
            USerViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(UserModuleviewModel::class.java)
        userid = CSPreferences.readString(this, com.app.marier.utils.Utils.USERID)

        try {
            val opts = IO.Options()
            opts.transports = arrayOf<String>(WebSocket.NAME)
            mSocket = IO.socket("http://www.marier.one:9001", opts)
            mSocket.emit("join-chat", userid)


            mSocket.connect()
            mSocket.emit("get-room-list",userid!!)
            mSocket.on("receive-call", receiveCallListener);


            //Register all the listener and callbacks here.
            mSocket.on(Socket.EVENT_CONNECT, onConnect)
            mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectionError)





        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }



    }

    val receiveCallListener = Emitter.Listener { args ->
        // Handle the received data here
        val receivedData = args[0] as JSONObject

        val name = receivedData.getString("name")
        Log.d("checkingvalueforecieve","workinggggg")
        runOnUiThread {

            showDialog(name)
        }

    }




    var onConnect = Emitter.Listener {
        this!!.runOnUiThread {
            Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show()
        }

    }
    private val onConnectionError = Emitter.Listener {

        this!!.runOnUiThread {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }











    private fun showDialog(name: String) {
        val dialog = Dialog(this)
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
            startActivity(Intent(this,AgoraVideoActivity::class.java))
        }

        tvcallfrom.text = name

        tvcallfrom.setOnClickListener {


        }
        val btn_cancel = dialog.findViewById<View>(R.id.btn_cancel) as Button
        btn_cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }




    companion object{
        var bottomNavigationView: BottomNavigationView?=null
    }
}