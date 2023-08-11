package com.app.marier.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.databinding.FragmentTermsconBinding

class TermsconFragment : Fragment() {
private var binding:FragmentTermsconBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTermsconBinding.inflate(layoutInflater,container,false)
        binding?.imgback?.setOnClickListener{
            findNavController().popBackStack()
        }
        return binding!!.root
    }


}