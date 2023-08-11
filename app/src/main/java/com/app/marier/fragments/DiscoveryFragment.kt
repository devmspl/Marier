package com.app.marier.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.activities.HomeActivity
import com.app.marier.adapter.GenderRecyclerViewAdapter
import com.app.marier.adapter.GridViewAdapterDiscoverScreen
import com.app.marier.adapter.SexualRecylerAdapter
import com.app.marier.databinding.FragmentDiscoveryBinding
import com.app.marier.datamodel.getusermodel.Data
import com.app.marier.interfaces.DiscoverFragClickListener
import com.app.marier.interfaces.ItemClickListener
import com.app.marier.staticmodel.GenderModel
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel


class DiscoveryFragment : Fragment(), View.OnClickListener,DiscoverFragClickListener {
    private var binding: FragmentDiscoveryBinding? = null
    var itemClickListener: ItemClickListener? = null
    private var genderRecyclerViewAdapter: GenderRecyclerViewAdapter?=null
    private var sexualRecylerAdapter:SexualRecylerAdapter?=null
    private var userModuleviewModel:UserModuleviewModel?=null
    var token = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiscoveryBinding.inflate(layoutInflater, container, false)
        listeners()
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userModuleviewModel = (activity as HomeActivity).viewModel
         token = CSPreferences.readString(requireActivity(), Utils.TOKEN)!!
        userModuleviewModel?.getalluserFun(token!!, "30", "1")
        bindobservers()

    }

    private fun listeners() {
        binding?.linfilter?.setOnClickListener(this)
        binding?.tvdiscover?.setOnClickListener(this)
        binding?.imgretryicon?.setOnClickListener(this)
    }


    private fun initrecyclerview(data: List<Data>?) {
        val recommendationRecyclerAdapter = GridViewAdapterDiscoverScreen(requireActivity(),this,data)
        binding?.discovergridview?.adapter = recommendationRecyclerAdapter
    }

    private fun showFilterDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.filterdialog)
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        var genderrecylerview = dialog.findViewById<View>(R.id.genderrecylerview) as RecyclerView
        var sexualityrecylerview = dialog.findViewById<View>(R.id.sexualityrecylerview) as RecyclerView
        val radioclassic = dialog.findViewById<View>(R.id.radioclassic) as RadioButton
        val radiogallery = dialog.findViewById<View>(R.id.radiogallery) as RadioButton
        val radioGroup = dialog?.findViewById<RadioGroup>(R.id.radioGroup)
        val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView3)
        val imgback = dialog.findViewById<View>(R.id.imgback) as ImageView
        val tvsave = dialog.findViewById<View>(R.id.tvsave) as TextView
        val seek_bar = dialog.findViewById<View>(R.id.seek_bar) as SeekBar


        val tvdistance = dialog.findViewById<TextView>(R.id.tvdistance)

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                activity?.runOnUiThread {
                    tvdistance.text = progress.toString()
                    Toast.makeText(requireContext(), progress.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Handle the start of tracking touch event
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Handle the end of tracking touch event
            }
        })





        tvsave?.setOnClickListener{
            dialog?.dismiss()
        }


//radiobutton____________________________________________________________________________________________________________________________________________________________________________________________________________
        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<RadioButton>(checkedId)
            if (radiogallery.isChecked) {
                radiogallery.isChecked = true
                val handler = Handler()
                handler.postDelayed({
                    navController.setGraph(R.navigation.bottomnavscreengraph)
                    dialog.dismiss()
                }, 1000)
//
            } else {
                radiogallery.isChecked = false
                radioclassic.isChecked = true
                val handler = Handler()
                handler.postDelayed({
                    navController.setGraph(R.navigation.bottomnavigationgraph2)
                    dialog.dismiss()
                }, 1000)

            }
        }
//radiobutton____________________________________________________________________________________________________________________________________________________________________________________________________________


//recyclerview------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        itemClickListener = object : ItemClickListener {
            override fun onClick(s: String) {
                genderrecylerview?.post(Runnable { genderRecyclerViewAdapter?.notifyDataSetChanged() })
                sexualityrecylerview?.post(Runnable { sexualRecylerAdapter?.notifyDataSetChanged() })
            }
        }

        val genderRecyclerViewAdapter =
            GenderRecyclerViewAdapter(requireActivity(), getData(), itemClickListener)
        sexualRecylerAdapter = SexualRecylerAdapter(requireActivity(), sexualData())


        genderrecylerview?.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = genderRecyclerViewAdapter
        }
        sexualityrecylerview?.apply {
            layoutManager =LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter =sexualRecylerAdapter
        }
        //recyclerview------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        imgback.setOnClickListener {
            dialog?.dismiss()
        }

        dialog.show()
    }

    private fun getData(): List<GenderModel> {
        return listOf(
            GenderModel("Female"),
            GenderModel("Male"),
            GenderModel("Transgender Female"),
            GenderModel("Transgender Male"),
            GenderModel("Transgender"),
        )
    }
    private fun sexualData():List<String>{
        return listOf("Strainght","Gay","Lesbian","Bisexual","Panasexual")
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.linfilter -> showFilterDialog()
            R.id.tvdiscover->showDialog()
            R.id.imgretryicon->{
                userModuleviewModel?.getalluserFun(token!!, "30", "1")
                bindobservers()
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.matchdialog)
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)




        dialog.show()
    }

    override fun passclick() {
        val navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView3)
        navController.setGraph(R.navigation.bottomnavigationgraph2)
    }


    private fun bindobservers() {
        userModuleviewModel!!.getallusersliveData.value = null
        userModuleviewModel!!.getallusersliveData.removeObservers(viewLifecycleOwner)
        userModuleviewModel!!.getallusersliveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(
                        requireContext(),
                        response.data?.data?.size.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    initrecyclerview(response?.data?.data)


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