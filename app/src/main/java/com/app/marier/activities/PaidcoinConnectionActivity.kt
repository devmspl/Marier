package com.app.marier.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.adapter.PlansRecyclerAdapter
import com.app.marier.databinding.ActivityPaidcoinConnectionBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class PaidcoinConnectionActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityPaidcoinConnectionBinding? = null
    var bottomSheetDialog: BottomSheetDialog? = null
    var spinner:Spinner?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaidcoinConnectionBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        listeners()
    }

    private fun listeners() {
        binding?.tvwhereuse?.setOnClickListener(this)
        binding?.tvbuy?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)
        binding?.linbottom?.setOnClickListener(this)
        binding?.lintop?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvwhereuse -> showBottomSheetDialog(p0)
            R.id.tvbuy -> showBottomSheetBuyDialog(p0)
            R.id.imgback -> finish()
            R.id.linbottom -> showBottomSheetBuyDialog(p0)
            R.id.lintop -> showBottomSheetBuyDialog(p0)

        }
    }

    private fun showBottomSheetDialog(view: View?) {
        bottomSheetDialog = BottomSheetDialog(this, R.style.SheetDialog)
        bottomSheetDialog?.setContentView(R.layout.tvwhereusebottomsheetdialog)

//        btn_apllyfilter = bottomSheetDialog?.findViewById<Button>(R.id.btn_apllyfilter)
        bottomSheetDialog?.show()


    }

    private fun showBottomSheetBuyDialog(view: View?) {
        bottomSheetDialog = BottomSheetDialog(this, R.style.SheetDialog)
        bottomSheetDialog?.setContentView(R.layout.buybottomsheetdialog)
        spinner = bottomSheetDialog?.findViewById(R.id.spinner)

        spinner()
        bottomSheetDialog?.show()


    }
    private fun spinner(){
        val languages = resources.getStringArray(R.array.plans)
        val adapter = ArrayAdapter(this,
            R.layout.plansrecycleritem,R.id.tvplan, languages)
       spinner?.adapter = adapter
       spinner?.setPrompt("Select your favorite Planet!");
        spinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }

        }

    }


}