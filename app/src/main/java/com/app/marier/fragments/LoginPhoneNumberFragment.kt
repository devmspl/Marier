package com.app.marier.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.databinding.FragmentLoginBinding
import com.app.marier.databinding.FragmentLoginPhoneNumberBinding


class LoginPhoneNumberFragment : Fragment(), View.OnClickListener {
    private var binding:FragmentLoginPhoneNumberBinding?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginPhoneNumberBinding.inflate(layoutInflater)
        val view:View = binding!!.root
        listeners()
        return view
    }
    private fun listeners(){
        binding?.tvcontinue?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
       when(p0?.id){
           R.id.tvcontinue->findNavController().navigate(R.id.action_loginPhoneNumberFragment_to_otpFragment)
           R.id.imgback->findNavController().popBackStack()

       }
    }



}