package com.app.marier.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.marier.R
import com.app.marier.activities.HomeActivity
import com.app.marier.activities.SignupActivity
import com.app.marier.adapter.AddphotosRecyclerAdapter
import com.app.marier.databinding.FragmentAddphotosBinding
import com.app.marier.interfaces.PickiImagesAdapterInterface
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class AddphotosFragment : Fragment(), PickiImagesAdapterInterface, View.OnClickListener {
    private var binding:FragmentAddphotosBinding?=null
    private val pickImage = 100
    var uri: ArrayList<Uri> = ArrayList()
    private var addphotosRecyclerAdapter : AddphotosRecyclerAdapter?=null
    var multiParts: ArrayList<MultipartBody.Part> = ArrayList<MultipartBody.Part>()
    lateinit var viewModel:UserModuleviewModel
    val compressedBitmapList = ArrayList<Bitmap>()






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddphotosBinding.inflate(layoutInflater,container,false)
        listeners()
        initRecyler()

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               activity?.finishAffinity()
            }
        })



        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as SignupActivity).viewModel
    }
    private fun listeners(){
        binding?.tvcontinue?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)

    }
    private fun initRecyler() {
         addphotosRecyclerAdapter = AddphotosRecyclerAdapter(activity as FragmentActivity, this, uri)
        binding!!.addphotosrecylerview.layoutManager = GridLayoutManager(activity, 2)
        binding!!.addphotosrecylerview.adapter = addphotosRecyclerAdapter

    }
    override fun picimages(position: Int) {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        gallery.type = "image/*"
        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        
        startActivityForResult(gallery, pickImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            if (data?.clipData != null) {
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    uri.add(data.clipData!!.getItemAt(i).uri)
                }
                Toast.makeText(activity, uri.size.toString(), Toast.LENGTH_SHORT).show()
                initRecyler()
                addphotosRecyclerAdapter?.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, uri.size.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tvcontinue->{
                var userid = CSPreferences.readString(requireActivity(),Utils.USERID)
                viewModel.addmultiplephotos(multipartimages(),userid!!)
              bindobservers()
            }
            R.id.imgback->{
                activity?.finishAffinity()
            }
        }
    }



    fun multipartimages():ArrayList<MultipartBody.Part>{
        val bitmapList = ArrayList<Bitmap>()

        for (uri in uri) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                bitmapList.add(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        for (bitmap in bitmapList) {
            val compressedBitmap = compressBitmap(bitmap, 80) // Adjust the compression quality as needed (0-100)
            compressedBitmapList.add(compressedBitmap)
            val file = bitmapToFile(compressedBitmap, requireActivity())

            val requestFile = file.asRequestBody("image/jpeg".toMediaType())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            multiParts.add(body)
        }
        return multiParts
    }

    private fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }


    fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        val cacheDir = context.cacheDir

        val file = File.createTempFile("temp_image", null, cacheDir)

        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()

        return file
    }

    private fun bindobservers() {
        viewModel.addphotosliveData.value = null
        viewModel.addphotosliveData.removeObservers(viewLifecycleOwner)
        viewModel.addphotosliveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    CSPreferences.putString(requireActivity(),Utils.USERLOGIN,"true")
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(requireContext(),HomeActivity::class.java))
                    requireActivity().finishAffinity()
                }
                is Resource.Error -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    Utils.showDialogMethod(activity, activity?.fragmentManager)
                }
            }
        })
    }




}