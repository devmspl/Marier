package com.app.marier.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.activities.SettingActivity
import com.app.marier.databinding.FragmentEditProfileBinding
import com.app.marier.datamodel.getcurrentuserbyid.GetCurrentUserByIdExample
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel
import java.util.Calendar


class EditProfileFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentEditProfileBinding? = null
    lateinit var viewmodel: UserModuleviewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        listeners()
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel = (activity as SettingActivity).viewModel
        var token = CSPreferences.readString(requireActivity(), Utils.TOKEN)
        var userid = CSPreferences.readString(requireActivity(), Utils.USERID)
        viewmodel.getcurrentuserByid(token!!, userid!!)
        bindobserver()

    }

    private fun listeners() {
        binding?.tvdone?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)
        binding?.tvdob?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvdone -> findNavController().navigateUp()
            R.id.imgback -> findNavController().navigateUp()
            R.id.tvdob -> pickdate()
        }
    }

    private fun bindobserver() {
        viewmodel.getcurrentuserByidliveData.value = null
        viewmodel.getcurrentuserByidliveData.removeObservers(viewLifecycleOwner)
        viewmodel.getcurrentuserByidliveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(
                        requireContext(),
                        it.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    setData(it.data)

                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        it.data?.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                }

                is Resource.Loading -> {
                    Utils.showDialogMethod(requireActivity(), activity?.fragmentManager)

                }
            }
        })
    }

    private fun setData(data: GetCurrentUserByIdExample?) {
        binding?.tvaboutname?.text  = "About "+data?.data?.name
        binding?.tvphonenum?.text  = data?.data?.phoneNumber.toString()
        val getdob = data?.data?.dob
        val dob = getdob?.take(10)
        binding?.tvdob?.text =   dob
        binding?.etgender?.setText(data?.data!!.sex.toString())
    }

    private fun pickdate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, 1)
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            activity as FragmentActivity,
            { view, year, monthOfYear, dayOfMonth ->
                binding?.tvdob?.text =
                    (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
                binding?.tvdob?.setTextColor(Color.BLACK)
//                            (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show()
    }



}