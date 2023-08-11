package com.app.marier.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import com.app.marier.R
import com.app.marier.activities.SettingActivity
import com.app.marier.adapter.BlockGridviewAdapter
import com.app.marier.databinding.FragmentGetBlockBinding
import com.app.marier.datamodel.block.getblockuser.Data
import com.app.marier.datamodel.unblock.unblockreq.UnblockRequest
import com.app.marier.interfaces.BlockClicklistener
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel


class GetBlockFragment : Fragment(), BlockClicklistener {
    private var binding:FragmentGetBlockBinding?=null
    lateinit var viewmodel:UserModuleviewModel
     var ouruserid =""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGetBlockBinding.inflate(layoutInflater,container,false)

        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel = (activity as SettingActivity).viewModel
    ouruserid  = CSPreferences.readString(requireActivity(),Utils.USERID)!!
        viewmodel.getallblockusers(ouruserid!!,"1","11")
        bindobserversgetallblockusers()
    }

    private fun initgridview(data: List<Data>) {
        var blockgridadapter = BlockGridviewAdapter(requireContext(),data,this)
        binding?.blockusergridview?.apply {
            adapter = blockgridadapter

        }
    }

    private fun bindobserversgetallblockusers() {
        viewmodel.getallblockuserslivedata.value = null
        viewmodel.getallblockuserslivedata.removeObservers(this)
        viewmodel.getallblockuserslivedata.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
                    initgridview(response.data!!.data)
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

    override fun passclcik(_id: String, imgmenu: ImageView) {
        showPopupMenu(_id,imgmenu)
    }


    private fun showPopupMenu(_id: String, imgmenu: ImageView) {
        val popupMenu = PopupMenu(requireContext(),imgmenu)
        popupMenu.inflate(R.menu.blockmenu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.unblock -> {
//                    showBlockDialog()
                    viewmodel.unblockuser(unblockreq(_id))
                    bindobserverunblock()
                    true
                }

                R.id.cancel -> {

                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }


    private fun unblockreq(_id: String):UnblockRequest{
        return UnblockRequest(ouruserid!!,_id)
    }

    private fun bindobserverunblock() {
        viewmodel.unblocklivedata.value = null
        viewmodel.unblocklivedata.removeObservers(this)
        viewmodel.unblocklivedata.observe(this, Observer { response ->
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




}