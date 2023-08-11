package com.app.marier.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.databinding.FragmentHelpSupportBinding


class HelpSupportFragment : Fragment(), View.OnClickListener {
private var binding:FragmentHelpSupportBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHelpSupportBinding.inflate(layoutInflater,container,false)
        this.binding = binding
        listeners()
        return binding!!.root
    }

    private fun listeners(){
        binding?.imgback?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){

            R.id.imgback->{
                findNavController().navigateUp()
            }
        }
    }


}