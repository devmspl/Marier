package com.app.marier.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.activities.OnBoardSplashActivity
import com.app.marier.activities.SettingActivity
import com.app.marier.databinding.FragmentSettingBinding
import com.app.marier.datamodel.getcurrentuserbyid.Data
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel


class SettingFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentSettingBinding? = null
    private var viewModel: UserModuleviewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        listeners()
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as SettingActivity).viewModel
        var token = CSPreferences.readString(requireActivity(), Utils.TOKEN)
        var userid = CSPreferences.readString(requireActivity(), Utils.USERID)!!
        viewModel?.getcurrentuserByid(token!!,userid)
        bindobservers()
    }

    private fun listeners() {
        binding?.lineditprofile?.setOnClickListener(this)
        binding?.tvlocation?.setOnClickListener(this)
        binding?.tvmyphotos?.setOnClickListener(this)
        binding?.tvmanagenotification?.setOnClickListener(this)
        binding?.tvrecentpasses?.setOnClickListener(this)
        binding?.tvreferfriend?.setOnClickListener(this)
        binding?.tvprivacysetting?.setOnClickListener(this)
        binding?.tvcontactsupport?.setOnClickListener(this)
        binding?.tvfaqs?.setOnClickListener(this)
        binding?.tvlogout?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)
        binding?.tvblockscontacts?.setOnClickListener(this)
        binding?.linmarierplus?.setOnClickListener(this)
        binding?.linpremium?.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.lineditprofile -> findNavController().navigate(R.id.action_settingFragment_to_editProfileFragment)
            R.id.tvlocation -> findNavController().navigate(R.id.action_settingFragment_to_locationFragment)
            R.id.tvmyphotos -> findNavController().navigate(R.id.action_settingFragment_to_myPhotosFragment)
            R.id.tvprivacysetting -> findNavController().navigate(R.id.action_settingFragment_to_privacySettingFragment)
            R.id.tvmanagenotification -> findNavController().navigate(R.id.action_settingFragment_to_manageNotificationFragment)
            R.id.tvrecentpasses -> findNavController().navigate(R.id.action_settingFragment_to_recentPassesFragment)
            R.id.tvreferfriend -> findNavController().navigate(R.id.action_settingFragment_to_reeferFriendFragment)
            R.id.tvcontactsupport -> findNavController().navigate(R.id.action_settingFragment_to_helpSupportFragment)
            R.id.tvfaqs -> findNavController().navigate(R.id.action_settingFragment_to_faqsFragment)
            R.id.tvlogout -> showDialog()
            R.id.imgback -> requireActivity().finish()
            R.id.tvblockscontacts->findNavController().navigate(R.id.action_settingFragment_to_getBlockFragment)
            R.id.linmarierplus->findNavController().navigate(R.id.action_settingFragment_to_commingsoonFragment)
            R.id.linpremium->findNavController().navigate(R.id.action_settingFragment_to_commingsoonFragment)
        }
    }




    private fun bindobservers() {
        viewModel?.getcurrentuserByidliveData?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    setData(it?.data?.data)
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

    private fun setData(data: Data?) {
        binding?.tvname?.text = data?.name
        binding?.tvphonenum?.text = data?.phoneNumber.toString()

        val getdob = data?.dob.toString()
        val dob = getdob.take(10)
        binding?.tvdob?.text = dob

    }





    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.logoutdialog)
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
        val btn_signout = dialog.findViewById<View>(R.id.btn_signout) as Button
//        val gender: String = CSPreferences.readString(activity, Utils.GENDERSELECT)

        btn_signout.setOnClickListener {
            CSPreferences.putString(requireActivity()!!, Utils.USERLOGIN, "false")
            val intent1 = Intent(activity, OnBoardSplashActivity::class.java)
            startActivity(intent1)
            requireActivity().finishAffinity()

        }
        val btn_cancel = dialog.findViewById<View>(R.id.btn_cancel) as Button
        btn_cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


}