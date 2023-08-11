package com.app.marier.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.marier.R
import com.app.marier.activities.HomeActivity
import com.app.marier.adapter.InteresetGridRecyclerAdapter
import com.app.marier.databinding.FragmentProfileBinding
import com.app.marier.datamodel.getcurrentuserbyid.Data
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random


class ProfileFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentProfileBinding? = null
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var bitmap: Bitmap? = null
    private val REQUEST_IMAGE_CAPTURE = 1 // Request code for camera Intent
    var bottomSheetDialog: BottomSheetDialog? = null
    private val CAMERA_PERMISSION_REQUEST_CODE = 101
    var imagePart: MultipartBody.Part? = null
    lateinit var viewmodel: UserModuleviewModel
    var userid = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        listeners()

        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel = (activity as HomeActivity).viewModel
        var token = CSPreferences.readString(requireActivity(), Utils.TOKEN)
        userid = CSPreferences.readString(requireActivity(), Utils.USERID)!!

        Log.d("tokenchecking",token!!)
        Log.d("tokenchecking",userid!!)


        viewmodel.getcurrentuserByid(token!!, userid!!)
        bindobservers()
        viewmodel.getinterestByUSerID(userid)
        getinterestBYidbindobservers()

    }

    private fun listeners() {
        binding?.imgselectimage?.setOnClickListener(this)
        binding?.imgsetting?.setOnClickListener(this)

    }

    private fun initgridrecyclerview(data: List<com.app.marier.datamodel.getinteresetByUserId.Data>?) {
        var gridRecyclerAdapter = InteresetGridRecyclerAdapter(requireActivity(),data)

        binding!!.interestgridrecylerview.apply {
            layoutManager = GridLayoutManager(requireActivity(), 3)
            adapter = gridRecyclerAdapter
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.imgsetting -> findNavController().navigate(R.id.action_profileFragment_to_settingActivity)

            R.id.imgselectimage -> showBottomSheetDialog(view)

        }
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



    private fun showBottomSheetDialog(view: View?) {
        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.SheetDialog)
        bottomSheetDialog?.setContentView(R.layout.chooseimagegalleryorcameraitem)

        var tvcamera = bottomSheetDialog?.findViewById<TextView>(R.id.tvcamera)
        var tvfromgallery = bottomSheetDialog?.findViewById<TextView>(R.id.tvfromgallery)
        var tvcancel = bottomSheetDialog?.findViewById<TextView>(R.id.tvcancel)
        tvcamera?.setOnClickListener {
            captureImageFromCamera(requireContext())

        }
        tvfromgallery?.setOnClickListener {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        tvcancel?.setOnClickListener {
            bottomSheetDialog?.dismiss()
        }
        bottomSheetDialog?.show()


    }

    private fun bindobservers() {
        viewmodel.getcurrentuserByidliveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    setData(it?.data?.data)
                }

                is Resource.Error -> {
                    Utils.hideDialog()
                }

                is Resource.Loading -> {
                    Utils.showDialogMethod(requireActivity(), activity?.fragmentManager)
                }
            }
        })
    }

    private fun setData(data: Data?) {
        binding?.tvusername?.text = data?.name
        binding?.tvuserage?.text = data?.dob
        binding?.tvgender?.text = data?.sex
        binding?.tvbio?.text = data?.bio

        val dob = calculateAge(data!!.dob)
        CSPreferences.putString(requireActivity(), Utils.AGE, dob.toString())
        binding?.tvuserage?.text = "," + dob.toString()
        if (data.avatar.length!=0){
            Glide.with(this)
                .load(data.avatar)
                .into(binding!!.imguserimage)
        }

        Log.d("checkingavatar",data.avatar.toString())

//        binding?.imguserimage?.let { Glide.with(requireActivity()).load(data.avatar).into(it) }
    }

    fun calculateAge(birthDateString: String): Int {
        val birthDate =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(birthDateString)
        val today = Calendar.getInstance().time
        val diffInMs = today.time - birthDate.time
        val diffInYears = (diffInMs / (1000 * 60 * 60 * 24 * 365.25)).toInt()
        return diffInYears
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
//            img_profile?.setImageURI(imageUri)
            try {
                bitmap =
                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri)
                val file = bitmapToFile(bitmap!!, requireActivity())
                val requestFile = file.asRequestBody("image/jpeg".toMediaType())

                val random = Random()
                imagePart = MultipartBody.Part.createFormData(
                    "image",
                    "abc" + random.nextInt(1000000) + ".jpg",
                    requestFile
                )
                viewmodel.uploadUserprofileimage(imagePart!!, userid)
                uploadimagebindobservers()


            } catch (e: IOException) {
                e.printStackTrace()
            }
            bottomSheetDialog?.dismiss()

            binding!!.imguserimage?.setImageURI(imageUri)
        } else if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            if (data != null) {
                val extras = data.getExtras()
                val bitmap = extras?.get("data") as? Bitmap
                binding!!.imguserimage?.setImageBitmap(bitmap)

                val file = bitmapToFile(bitmap!!, requireActivity())
                val requestFile = file.asRequestBody("image/jpeg".toMediaType())

                val random = Random()
                imagePart = MultipartBody.Part.createFormData(
                    "image",
                    "abc" + random.nextInt(1000000) + ".jpg",
                    requestFile
                )
                viewmodel.uploadUserprofileimage(imagePart!!, userid)
                uploadimagebindobservers()
                bottomSheetDialog?.dismiss()

            }
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
            captureImageFromCamera(requireContext())
        } else {
            // Camera permission denied, show an error message or handle accordingly
        }
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

    private fun uploadimagebindobservers() {
        viewmodel.uploadimageliveData.value = null
        viewmodel.uploadimageliveData.removeObservers(viewLifecycleOwner)
        viewmodel.uploadimageliveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
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


    private fun getinterestBYidbindobservers() {
        viewmodel.getinterestByuseridliveData.value = null
        viewmodel.getinterestByuseridliveData.removeObservers(viewLifecycleOwner)
        viewmodel.getinterestByuseridliveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    initgridrecyclerview(response?.data?.data)
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
                }
                is Resource.Error -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
//                    Utils.showDialogMethod(activity, activity?.fragmentManager)
                }
            }
        })
    }



}