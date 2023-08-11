package com.app.marier.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.databinding.FragmentManageNotificationBinding


class ManageNotificationFragment : Fragment(), View.OnClickListener {
    private var binding:FragmentManageNotificationBinding?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentManageNotificationBinding.inflate(layoutInflater,container,false)
        listenres()
        return binding?.root
    }

    private fun listenres(){
        binding?.imgback?.setOnClickListener(this)
        binding?.tvsave?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.imgback->findNavController().popBackStack()
            R.id.tvsave->findNavController().popBackStack()
        }
    }

}