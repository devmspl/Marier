package com.app.marier.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.app.marier.R
import com.app.marier.activities.SignupActivity
import com.app.marier.adapter.InterestRecyclerAdapter
import com.app.marier.databinding.FragmentInterestBinding
import com.app.marier.datamodel.addinterest.AddinterestRequest
import com.app.marier.datamodel.getallinterest.Data
import com.app.marier.interfaces.InterestRecyclerClickListener
import com.app.marier.interfaces.InterestRecyclerClicklistner
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel
import java.util.ArrayList


class InterestFragment : Fragment(), View.OnClickListener, InterestRecyclerClickListener {
   private var binding:FragmentInterestBinding?=null
    lateinit var viewmodel:UserModuleviewModel
    private var arraylist: ArrayList<String> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInterestBinding.inflate(layoutInflater,container,false)
        listeners()


        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
            }
        })
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel = (activity as SignupActivity).viewModel
        var token = CSPreferences.readString(requireActivity(),Utils.TOKEN)
        viewmodel.getallInterestFun(token!!)
        bindobservers()
    }
    private fun listeners(){
        binding?.tvsubmit?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)

    }

    private fun initRecyclerview(data: List<Data>?) {
        val interestRecyclerAdapter = InterestRecyclerAdapter(requireActivity(),data,this)
        val layoutManager = GridLayoutManager(activity, 6)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (position) {
                    0, 1, 2 -> return 2
                    3, 4 -> return 3
                    5 -> return 3
                    10->return 2
                    else -> return 2
                }
            }
        }
        binding?.interserrccyler?.layoutManager = layoutManager
        binding?.interserrccyler?.adapter = interestRecyclerAdapter
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tvsubmit->{
                var token = CSPreferences.readString(requireActivity(),Utils.TOKEN)
                var id = CSPreferences.readString(requireActivity(),Utils.USERID)
                if (arraylist.size!=0){
                    viewmodel.addinterstFun(token!!,id!!,addinterestRequest())
                    bindobserversAddinterst()
                }else{
                    Toast.makeText(requireContext(), "Please select interests", Toast.LENGTH_SHORT).show()
                }


            }
            R.id.imgback->activity?.finishAffinity()
        }
    }
    private fun bindobserversAddinterst() {
        viewmodel.addinterestliveData.value = null
        viewmodel.addinterestliveData.removeObservers(viewLifecycleOwner)
        viewmodel.addinterestliveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    findNavController().navigate(R.id.action_interestFragment_to_addphotosFragment)
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

    private fun bindobservers() {
        viewmodel.getallinterestlivedata.value = null
        viewmodel.getallinterestlivedata.removeObservers(viewLifecycleOwner)
        viewmodel.getallinterestlivedata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    initRecyclerview(response?.data?.data)
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

    override fun passdata(arrayList: ArrayList<String>) {

        this.arraylist = arrayList
    }

    private fun addinterestRequest():AddinterestRequest{

        val interests = arraylist

        return AddinterestRequest(interests)

    }

    override fun onItemClick(position: Int) {

    }


}