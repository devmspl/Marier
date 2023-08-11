package com.app.marier.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.marier.R
import com.app.marier.adapter.TermsandcondGridAdapter
import com.app.marier.databinding.FragmentTermsandcondtionBinding
import com.app.marier.staticmodel.TermsandcondModel


class TermsandcondtionFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentTermsandcondtionBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTermsandcondtionBinding.inflate(layoutInflater, container, false)
        val view: View = binding!!.root
        listeners()
        initrecyclerview()
        return view
    }

    private fun initrecyclerview() {
        val arrayList:ArrayList<TermsandcondModel> = ArrayList()
        arrayList.add(TermsandcondModel("Be yourself.","Upload only own photos,age and bio"))
        arrayList.add(TermsandcondModel("Be cool.","Stay chill and treat others with respect and dignity"))
        arrayList.add(TermsandcondModel("Be safe.","Don't give out personal info too quickly.Guage,analyse and date safely"))
        arrayList.add(TermsandcondModel("Be active.","Report others’ rude or bad behaviour actively so we can keep it safe"))
        val termsandcondGridAdapter = TermsandcondGridAdapter(requireActivity(),arrayList)
        binding?.termsandconrecyclerview?.layoutManager = GridLayoutManager(activity,2)
        binding?.termsandconrecyclerview?.adapter = termsandcondGridAdapter
    }

    private fun listeners() {
        binding?.tvunderstand?.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tvunderstand->findNavController().navigate(R.id.action_termsandcondtionFragment_to_personalinfoFragment)

        }

    }

}