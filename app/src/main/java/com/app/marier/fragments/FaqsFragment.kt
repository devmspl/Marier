package com.app.marier.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.marier.R
import com.app.marier.activities.SettingActivity
import com.app.marier.adapter.FaqRecyclerAdapter
import com.app.marier.databinding.FragmentFaqsBinding
import com.app.marier.datamodel.getfaq.Data
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel


class FaqsFragment : Fragment(), View.OnClickListener {
    private var binding:FragmentFaqsBinding?=null
    private var viewModel: UserModuleviewModel? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFaqsBinding.inflate(layoutInflater,container,false)
        listeners()
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as SettingActivity).viewModel
        viewModel?.getfaqs()
        bindobservers()

    }
    private fun listeners(){
        binding?.imgback?.setOnClickListener(this)
    }

    private fun initRecyclerview(data: List<Data>?) {
        val faqRecyclerAdapter = FaqRecyclerAdapter(requireActivity(),data)
        binding?.samlequestionrecyclerview?.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            adapter = faqRecyclerAdapter

        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.imgback->{
               findNavController().navigateUp()

            }
        }
    }

    private fun bindobservers() {
        viewModel?.getfaqlivedata?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    initRecyclerview(it?.data?.data)
                    Toast.makeText(requireContext(), it?.data?.message, Toast.LENGTH_SHORT).show()

                }

                is Resource.Error -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), it?.data?.message, Toast.LENGTH_SHORT).show()

                }

                is Resource.Loading -> {
                    Utils.showDialogMethod(requireActivity(), activity?.fragmentManager)

                }
            }
        })
    }



}