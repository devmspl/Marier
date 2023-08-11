package com.app.marier.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.activities.SignupActivity
import com.app.marier.adapter.GenderRecyclerViewAdapter
import com.app.marier.adapter.SexualRecylerAdapter
import com.app.marier.databinding.FragmentPersonalinfoBinding
import com.app.marier.datamodel.adduserdatamodel.request.AdduserDataRequest
import com.app.marier.datamodel.adduserdatamodel.request.AgeRange
import com.app.marier.datamodel.adduserdatamodel.request.Location
import com.app.marier.datamodel.adduserdatamodel.request.Setting
import com.app.marier.interfaces.ItemClickListener
import com.app.marier.staticmodel.GenderModel
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.utils.factories.USerViewModelFactory
import com.app.marier.viewmodel.UserModuleviewModel
import java.util.Calendar


class PersonalinfoFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentPersonalinfoBinding? = null
    var itemClickListener: ItemClickListener? = null
    private var genderRecyclerViewAdapter: GenderRecyclerViewAdapter? = null
    private var sexualRecylerAdapter: SexualRecylerAdapter? = null
    lateinit var viewModel: UserModuleviewModel
    var gender:String?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPersonalinfoBinding.inflate(layoutInflater, container, false)
        val view: View = binding!!.root
        listeners()
        initRecyclerView()

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
            }
        })
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = (activity as SignupActivity).viewModel
        listeners()
    }

    private fun listeners() {
        binding?.tvproceed?.setOnClickListener(this)
        binding?.tvdob?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)

    }

    private fun initRecyclerView() {
        itemClickListener = object : ItemClickListener {
            override fun onClick(s: String) {
                gender = s
                binding?.genderrecylerview?.post(Runnable { genderRecyclerViewAdapter?.notifyDataSetChanged() })
                binding?.sexualityrecylerview?.post(Runnable { sexualRecylerAdapter?.notifyDataSetChanged() })
                Toast.makeText(requireContext(), s.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        genderRecyclerViewAdapter =
            GenderRecyclerViewAdapter(requireActivity(), getData(), itemClickListener)
        binding?.genderrecylerview?.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding?.genderrecylerview?.adapter = genderRecyclerViewAdapter
        sexualRecylerAdapter =
            SexualRecylerAdapter(requireActivity(), sexualData())
        binding?.sexualityrecylerview?.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = sexualRecylerAdapter
        }

    }

    private fun getData(): List<GenderModel> {
        return listOf(
            GenderModel("Female"),
            GenderModel("Male"),
            GenderModel("Transgender Female"),
            GenderModel("Transgender Male"),
        )
    }

    private fun sexualData(): List<String> {
        return listOf("Strainght", "Gay", "Lesbian", "Bisexual", "Panasexual")
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvproceed -> {
                var token = CSPreferences.readString(requireActivity(), Utils.TOKEN)
                var userid = CSPreferences.readString(requireActivity(), Utils.USERID)
                if (!(binding?.etname!!.text.toString().isEmpty()&&binding?.tvdob!!.text.toString().isEmpty())){
                    viewModel.addUserDataFun(token!!, userid!!, addData())
                    Toast.makeText(requireContext(), gender.toString(), Toast.LENGTH_SHORT).show()
                    bindobservers()
                }else{
                    Toast.makeText(requireContext(), "Please Enter All Details", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.tvdob -> pickdate()
            R.id.imgback -> activity?.finishAffinity()
        }
    }


    private fun pickdate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Set the minimum date to January 1, 1900 (an arbitrary date in the past)
        val minDate = Calendar.getInstance()
        minDate.set(1900, Calendar.JANUARY, 1)

        // Set the maximum date to December 31, 2005
        val maxDate = Calendar.getInstance()
        maxDate.set(2005, Calendar.DECEMBER, 31)

        val datePickerDialog = DatePickerDialog(
            activity as FragmentActivity,
            { view, year, monthOfYear, dayOfMonth ->
                binding?.tvdob?.text =
                    (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString())
            },
            year,
            month,
            day
        )

        // Set the minimum and maximum date for the DatePickerDialog
        datePickerDialog.datePicker.minDate = minDate.timeInMillis
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
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
        var sexualityrecylerview =
            dialog.findViewById<View>(R.id.sexualityrecylerview) as RecyclerView
        val imgback = dialog.findViewById<View>(R.id.imgback) as ImageView


//recyclerview------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        itemClickListener = object : ItemClickListener {
            override fun onClick(s: String) {
                genderrecylerview?.post(Runnable { genderRecyclerViewAdapter?.notifyDataSetChanged() })
                sexualityrecylerview?.post(Runnable { sexualRecylerAdapter?.notifyDataSetChanged() })
            }
        }

        val genderRecyclerViewAdapter =
            GenderRecyclerViewAdapter(requireActivity(), getData(), itemClickListener)

        genderrecylerview?.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = genderRecyclerViewAdapter
        }
        sexualRecylerAdapter =
            SexualRecylerAdapter(requireActivity(), sexualData())
        sexualityrecylerview?.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = sexualRecylerAdapter
        }
        //recyclerview------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


        imgback.setOnClickListener {
            dialog?.dismiss()
        }

        dialog.show()
    }

    private fun addData(): AdduserDataRequest {

        val name = binding?.etname?.text.toString()
        val dob = binding?.tvdob?.text.toString()
        val type = "Point"
        val coordinates = arrayListOf<Double>(30.7046, 76.7179)
        val location = Location(coordinates, type)
        val to = 0
        val from = 0
        val ageRange = AgeRange(to, from)
        val setting = Setting(ageRange, location)
        val smallcasegender = gender?.lowercase()


        return AdduserDataRequest(dob, name, setting = setting, smallcasegender!!,binding?.etbio?.text.toString())
    }

    private fun bindobservers() {
        viewModel.adduserlivedata.value = null
        viewModel.adduserlivedata.removeObservers(viewLifecycleOwner)
        viewModel.adduserlivedata.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_personalinfoFragment_to_interestFragment)

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
