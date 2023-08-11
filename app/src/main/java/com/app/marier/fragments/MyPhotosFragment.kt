package com.app.marier.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.activities.SettingActivity
import com.app.marier.adapter.MyPhotosGridAdapter
import com.app.marier.databinding.FragmentMyPhotosBinding
import com.app.marier.datamodel.getcurrentuserbyid.Gallery
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel


class MyPhotosFragment : Fragment(), View.OnClickListener {
    private var binding:FragmentMyPhotosBinding?=null
    private var viewModel: UserModuleviewModel? = null




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPhotosBinding.inflate(layoutInflater,container,false)
        listenrers()
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as SettingActivity).viewModel
        var token = CSPreferences.readString(requireActivity(),Utils.TOKEN)

        var userid = CSPreferences.readString(requireActivity(),Utils.USERID)
        viewModel?.getcurrentuserByid(token!!,userid!!)
        bindobservers()
    }
    private fun initgridview(gallery: List<Gallery>?) {
        Log.d("chekingloggallery",gallery.toString())
        val myPhotosGridAdapter = MyPhotosGridAdapter(requireActivity(),gallery)
        binding?.myphotosgridview?.apply {
            adapter = myPhotosGridAdapter
        }


    }

    private fun listenrers(){
        binding?.imgback?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.imgback->findNavController().popBackStack()
        }
    }

    private fun bindobservers() {
        viewModel?.getcurrentuserByidliveData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    initgridview(it?.data?.data?.gallery)
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




}