package com.app.marier.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupMenu
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.marier.R
import com.app.marier.adapter.ChatDetailRecyclerViewAdapter
import com.app.marier.databinding.ActivityChatDetailBinding
import com.app.marier.datamodel.block.blockreq.BlockRequest
import com.app.marier.datamodel.socket.Messages
import com.app.marier.repository.UserModuleResponseRepository
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.utils.audio.AudioRecorder
import com.app.marier.utils.factories.USerViewModelFactory
import com.app.marier.viewmodel.UserModuleviewModel
import com.bumptech.glide.Glide
import com.devlomi.record_view.OnBasketAnimationEnd
import com.devlomi.record_view.OnRecordClickListener
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordPermissionHandler
import com.kroegerama.imgpicker.BottomSheetImagePicker
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.UUID
import java.util.concurrent.TimeUnit


class ChatDetailActivity : AppCompatActivity(), View.OnClickListener,
    BottomSheetImagePicker.OnImagesSelectedListener {
    private var seekbar: SeekBar?=null
    private var binding: ActivityChatDetailBinding? = null
    var userid: String? = null
    lateinit var mSocket: Socket
     var roomId: String?=null
    private var messageList: ArrayList<Messages> = ArrayList()
    private var chatadapter: ChatDetailRecyclerViewAdapter? = null
    lateinit var viewModel: UserModuleviewModel
    var otheruserid: String? = null
    var username: String? = null
    var userimage: String? = null
    var agoratoken: String? = null
    var arrayList: ArrayList<Uri> = ArrayList()
    private var audioRecorder: AudioRecorder? = null
    private var recordFile: File? = null
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler()
    private val CAMERA_PERMISSION_REQUEST_CODE = 101

    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_FILE_REQUEST_CODE = 2
    private val PICK_PDF_FILE_REQUEST_CODE = 3




    companion object{
        var timerString = ""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userid = CSPreferences.readString(this, Utils.USERID)
        binding = ActivityChatDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        otheruserid = intent.getStringExtra("userid")
        Log.d("useridforanother", otheruserid!!)
        username = intent.getStringExtra("username")
        userimage = intent.getStringExtra("userimage")
        binding?.tvname?.text = username
        Glide.with(this)
            .load(userimage).placeholder(R.drawable.ldki)
            .into(binding?.userimage!!)
        listeners()
        initchatrecylerview()
        val repository = UserModuleResponseRepository()

        val viewModelProviderFactory =
            USerViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(UserModuleviewModel::class.java)

        viewModel.createagoratoken("hilooosss")
        bindobserverCreateagoratoken()
        audioRecorder = AudioRecorder()
        mediaPlayer = MediaPlayer()




        try {
            val opts = IO.Options()
            opts.transports = arrayOf<String>(WebSocket.NAME)
            mSocket = IO.socket("http://www.marier.one:9001", opts)
            Log.d("checkingid", userid!!)
            mSocket.emit("join-chat", userid)
            mSocket.emit(
                "set-room", JSONObject()
                    .put("senderId", userid)
                    .put("receiverId", otheruserid)
            )

            mSocket.on("set-room", onGettingRoomId)






            mSocket.on("receive-message", onNewMessage)


        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("fail", "Failed to connect")
        }

        mSocket.connect()
        //Register all the listener and callbacks here.
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectionError)



        binding?.imgback?.setOnClickListener {
            finish()
        }

        binding?.recordButton?.setRecordView(binding?.recordView)

        //ListenForRecord must be false ,otherwise onClick will not be called
        binding?.recordButton?.setOnRecordClickListener(OnRecordClickListener {
            Toast.makeText(this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show()
            Log.d("RecordButton", "RECORD BUTTON CLICKED")
        })


        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8


        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        binding?.recordView?.setCancelBounds(8f)


        binding?.recordView?.setSmallMicColor(Color.parseColor("#c2185b"))

        //prevent recording under one Second

        //prevent recording under one Second
        binding?.recordView?.setLessThanSecondAllowed(false)


        binding?.recordView?.setSlideToCancelText("")


        binding?.recordView?.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0)


        binding?.recordView?.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                recordFile = File(filesDir, UUID.randomUUID().toString() + ".3gp")
                try {
                    audioRecorder!!.start(recordFile?.getPath())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Log.d("RecordView", "onStart")
                Toast.makeText(this@ChatDetailActivity, "OnStartRecord", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                stopRecording(true)
                Toast.makeText(this@ChatDetailActivity, "onCancel", Toast.LENGTH_SHORT).show()
                Log.d("RecordView", "onCancel")
            }

            override fun onFinish(recordTime: Long, limitReached: Boolean) {
                stopRecording(false)
                val time: String? = getHumanTimeText(recordTime)
                Log.d("checkingforrecorvideo", recordFile.toString())
                addMessage("", "", Messages.TYPE_MESSAGE, uri = null, recordFile.toString())


                var numpath: Int = recordTime!!.toInt()
                timerString = ""

                var secondString = ""
                val hours = (numpath / (1000 * 60 * 60)).toInt()
                val minutes = (numpath % (1000 * 60 * 60) / (1000 * 60)).toInt()
                val seconds = (numpath % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
                if (hours > 0) {
                    timerString = "0$seconds"
                }
                secondString = if (seconds < 10) {
                    "0$seconds"
                } else {
                    "" + seconds
                }
                timerString = "$timerString$minutes:$secondString"
                Log.d("checkingvideo",timerString.toString())






            }

            override fun onLessThanSecond() {
                stopRecording(true)
                Toast.makeText(this@ChatDetailActivity, "OnLessThanSecond", Toast.LENGTH_SHORT)
                    .show()
                Log.d("RecordView", "onLessThanSecond")
            }

            override fun onLock() {
                Toast.makeText(this@ChatDetailActivity, "onLock", Toast.LENGTH_SHORT).show()
                Log.d("RecordView", "onLock")
            }
        })


        binding?.recordView?.setOnBasketAnimationEndListener(OnBasketAnimationEnd {
            Log.d(
                "RecordView",
                "Basket Animation Finished"
            )
        })

        binding?.recordView?.setRecordPermissionHandler(RecordPermissionHandler {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return@RecordPermissionHandler true
            }
            val recordPermissionAvailable = ContextCompat.checkSelfPermission(
                this@ChatDetailActivity,
                Manifest.permission.RECORD_AUDIO
            ) == PermissionChecker.PERMISSION_GRANTED
            if (recordPermissionAvailable) {
                return@RecordPermissionHandler true
            }
            ActivityCompat.requestPermissions(
                this@ChatDetailActivity, arrayOf<String>(Manifest.permission.RECORD_AUDIO),
                0
            )
            false
        })


    }


    private fun stopRecording(deleteFile: Boolean) {
        audioRecorder?.stop()
        if (recordFile != null && deleteFile) {
            recordFile?.delete()
        }
    }


    private fun getHumanTimeText(milliseconds: Long): String? {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(milliseconds),
            TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        )
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
    private val onGettingRoomId = Emitter.Listener {
        this!!.runOnUiThread {
            var roomIdjson = it[0].toString();

            try {
                val jsonObject = JSONObject(roomIdjson)
                roomId = jsonObject.getString("roomId")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }


    private fun listeners() {
        binding?.imgsend?.setOnClickListener(this)
        binding?.imgmenu?.setOnClickListener(this)
        binding?.videocall?.setOnClickListener(this)
        binding?.imggallery?.setOnClickListener(this)
        binding?.imgcamera?.setOnClickListener(this)
        binding?.imgadd?.setOnClickListener(this)
        binding?.imgphone?.setOnClickListener(this)
    }

    private fun initchatrecylerview() {
        chatadapter = ChatDetailRecyclerViewAdapter(messageList, this)
        binding!!.chatrecyclerview.apply {
            layoutManager =
                LinearLayoutManager(this@ChatDetailActivity, LinearLayoutManager.VERTICAL, false)
            adapter = chatadapter
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgsend -> {

                if (binding?.etmsg?.text!!.isNotEmpty()) {
                    val message: String = binding!!.etmsg?.getText().toString()
                    addMessage("strDate", message, Messages.TYPE_MESSAGE)

                    mSocket.emit(
                        "send-message", JSONObject()
                            .put("text_data", binding?.etmsg?.text.toString())
                            .put("senderId", userid)
                            .put("roomId", roomId)
                    );
                    binding?.etmsg?.text?.clear()
                }
            }

            R.id.imgmenu -> showPopupMenu()
            R.id.videocall -> {
                val data = JSONObject()
                data.put("senderId", userid)
                data.put("receiverId", otheruserid)
                data.put("token", agoratoken)
                data.put("channelName", "hilooosss")
                mSocket.emit("send-call", data);
                startActivity(Intent(this, AgoraVideoActivity::class.java))
            }

            R.id.imggallery -> {
                BottomSheetImagePicker.Builder(getString(R.string.file_provider))
                    .multiSelect(1, 6)                  //user has to select 3 to 6 images
                    .multiSelectTitles(
                        R.plurals.pick_multi,           //"you have selected <count> images
                        R.plurals.pick_multi_more,      //"You have to select <min-count> more images"
                        R.string.pick_multi_limit       //"You cannot select more than <max> images"
                    )
                    .peekHeight(R.dimen.peekHeight)     //peek height of the bottom sheet
                    .columnSize(R.dimen.columnSize)     //size of the columns (will be changed a little to fit)
                    .requestTag("multi")                //tag can be used if multiple pickers are used
                    .show(supportFragmentManager)
            }

            R.id.imgcamera->{
                captureImageFromCamera(this)
            }
            R.id.imgadd->{
                launchFilePicker()
            }
            R.id.imgphone->{
                val data = JSONObject()
                data.put("senderId", userid)
                data.put("receiverId", otheruserid)
                data.put("token", agoratoken)
                data.put("channelName", "hilooosss")
                mSocket.emit("send-call", data);
                var intent = Intent(this,AgoraVideoActivity::class.java)
                intent.putExtra("value","audio")
                startActivity(intent)
            }
        }

    }


    private fun addMessage(
        time: String,
        message: String,
        type: Int,
        uri: Uri? = null,
        recordfile: String? = null,
        pdffile:String?=null
    ) {
        val builder = Messages.Builder(type)
            .time(time)
            .message(message)

        uri?.let {
            builder.uri(it)
        }
        recordfile?.let {
            builder.mrecordaudo(it)
        }
        pdffile?.let {
            builder.pdffile(it)
        }

        messageList.add(builder.build())
        chatadapter?.notifyItemInserted(messageList.size)
        // scrollToBottom()
    }


    private val onNewMessage =
        Emitter.Listener { args ->
            if (this != null) {
                this!!.runOnUiThread(Runnable {
                    binding?.chatrecyclerview!!.post {
                        binding?.chatrecyclerview!!.scrollToPosition(chatadapter!!.itemCount - 1)
                        // Here adapteeeeer.getItemCount()== child count
                    }

                    val data = args[0] as JSONObject
                    val remotesender = data.optString("sender")
                    val message = data.optString("text_data")
                    Toast.makeText(this, remotesender.toString(), Toast.LENGTH_SHORT).show()
                    if (remotesender != userid) {
                        addMessage(
                            "strDate",
                            message,
                            Messages.TYPE_MESSAGE_REMOTE
                        )
                    }


                })
            }
        }


    private fun showPopupMenu() {
        val popupMenu = PopupMenu(this, binding?.imgmenu)
        popupMenu.inflate(R.menu.blockdetailsmenu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.blocknreport -> {
                    showBlockDialog()
                    true
                }

                R.id.viewprofile -> {
                    val intent = Intent(this, UserDeatilActivity::class.java)
                    intent.putExtra("userid", otheruserid)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }


    private fun showBlockDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.blockdialog)
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
        val btnblock = dialog.findViewById<View>(R.id.btnblock) as Button
//        val gender: String = CSPreferences.readString(activity, Utils.GENDERSELECT)

        btnblock.setOnClickListener {
            viewModel.createblock(createblockReq())
            bindobserverscreateblock()
            dialog?.dismiss()
//            finish()

        }
        val btn_cancel = dialog.findViewById<View>(R.id.btn_cancel) as Button
        btn_cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun createblockReq(): BlockRequest {
        var ouruserid = CSPreferences.readString(this, Utils.USERID)
        var anotheruserid = "6468715f8e1a8b34c66ef4e0"
        return BlockRequest(ouruserid!!, anotheruserid)
    }


    private fun bindobserverscreateblock() {
        viewModel.createblocklivedata.value = null
        viewModel.createblocklivedata.removeObservers(this)
        viewModel.createblocklivedata.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(this, response.data?.message, Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Error -> {
                    Utils.hideDialog()
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
//                    Utils.showDialogMethod(activity, activity?.fragmentManager)
                }
            }
        })
    }

    private fun bindobserverCreateagoratoken() {
        viewModel.createtokenlivedata.value = null
        viewModel.createtokenlivedata.removeObservers(this)
        viewModel.createtokenlivedata.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    agoratoken = response?.data?.data.toString()
                    Log.d("checkingforlog", "200")

                }

                is Resource.Error -> {
                    Utils.hideDialog()
                    Log.d("checkingforlog", "400")

                }

                is Resource.Loading -> {
                    Utils.showDialogMethod(this, fragmentManager)
                }
            }
        })
    }

    override fun onImagesSelected(uris: List<Uri>, tag: String?) {
        if (uris.isNotEmpty()) {
            for (i in 0 until uris.size) {
                addMessage("", uris[0].toString(), Messages.TYPE_MESSAGE, uris[i])
            }

            Log.d("checkinglogs", uris.toString())
        }
        Toast.makeText(this, uris.size.toString(), Toast.LENGTH_SHORT).show()
    }

    fun prepareMediaPlayer(paths: String, playerseekbar: SeekBar) {
        this.seekbar = playerseekbar
        Log.d("pathupdate", paths)
        try {
            if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
                mediaPlayer!!.reset()
            }

            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setDataSource(paths)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
            startSeekBarUpdater() // Start updating the SeekBar
        } catch (exception: Exception) {
            Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()

        }
    }


    private val updater: Runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer?.isPlaying == true) {
                seekbar?.progress = ((mediaPlayer!!.currentPosition.toFloat() / mediaPlayer!!.duration) * 100).toInt()
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun startSeekBarUpdater() {
        handler.removeCallbacks(updater)
        handler.postDelayed(updater, 1000)
    }

    private fun captureImageFromCamera(context: Context) {
        try {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } else {
                // Request the CAMERA permission
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle any exceptions that occur
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                val extras = data.extras
                val bitmap = extras?.get("data") as? Bitmap

                if (bitmap != null) {
                    val imageUri = bitmapToUri(this, bitmap)
                    addMessage("", "", Messages.TYPE_MESSAGE, imageUri)

                    // Use the imageUri as needed (e.g., display it in an ImageView, save it to a database, etc.)

                } else {
                    Toast.makeText(this, "Bitmap is null", Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == PICK_FILE_REQUEST_CODE && data != null && data.data != null) {
                val selectedFileUri: Uri = data.data!!
                val selectedFileName: String? = getFileNameFromUri(selectedFileUri)
                addMessage("","",Messages.TYPE_MESSAGE,uri = null,recordfile = null,selectedFileName)

            } else if (requestCode == PICK_PDF_FILE_REQUEST_CODE && data != null && data.data != null) {
                val selectedFileUri: Uri = data.data!!
                val selectedFilePath: String? = selectedFileUri.path
                val selectedFileName: String? = selectedFilePath?.let { filePath ->
                    val file = File(filePath)
                    file.name
                }
                Toast.makeText(this, "Selected PDF file: $selectedFileName", Toast.LENGTH_SHORT).show()

                // Handle the selected PDF file URI here
                // You can use this URI to access and process the selected PDF file
                // For example, you can display the file name or read its contents
            }

        }
    }


    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_${timeStamp}.jpg"

        // Get the external storage directory
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Create the file to save the bitmap
        val imageFile = File(storageDir, fileName)

        // Save the bitmap to the file
        saveBitmapToFile(bitmap, imageFile)

        // Notify the media scanner about the new image file
        scanMediaFile(context, imageFile)

        // Get the content URI for the saved image file
        return FileProvider.getUriForFile(context, "com.kroegerama.imgpicker.demo.fileprovider", imageFile)

    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        var outputStream: OutputStream? = null
        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            outputStream?.close()
        }
    }

    private fun scanMediaFile(context: Context, file: File) {
        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null) { path, uri ->
            // Media scan completed
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Camera permission granted, capture image from camera
            captureImageFromCamera(this)
        } else {
            // Camera permission denied, show an error message or handle accordingly
        }
    }

    private fun launchFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }


    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        val scheme = uri.scheme

        if (scheme == "file") {
            fileName = uri.lastPathSegment
        } else if (scheme == "content") {
            val contentResolver = contentResolver
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }

        return fileName
    }






}