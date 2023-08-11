package com.app.marier.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.marier.R
import com.app.marier.activities.HomeActivity
import com.app.marier.adapter.GetsuperlikeGridAdapter
import com.app.marier.adapter.LikesGridAdapter
import com.app.marier.databinding.FragmentLikeBinding
import com.app.marier.datamodel.getlikeotheruser.Data
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class LikeFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentLikeBinding? = null
    val scope = CoroutineScope(Dispatchers.IO)
    lateinit var viewModel: UserModuleviewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLikeBinding.inflate(layoutInflater, container, false)
        listeners()
        return binding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel
        var token = CSPreferences.readString(requireActivity(),Utils.TOKEN)
        var userid = CSPreferences.readString(requireActivity(),Utils.USERID)
        viewModel?.getallcreatelikes(token!!,userid!!)
        Log.d("checkuserid",userid+"")
        bindobservers()
    }

    private fun listeners() {
        binding?.tvlike?.setOnClickListener(this)
        binding?.tvsuperlike?.setOnClickListener(this)

    }

    private fun initrecyclerview(data: List<Data>?) {
        val likesRecyclerAdapter = LikesGridAdapter(requireActivity(),data)
        binding?.likesrecyclerview?.apply {
            adapter = likesRecyclerAdapter
        }

    }

    private fun getsuperlikeGridview(data: List<com.app.marier.datamodel.Getsuperlikemodel.Data>?) {
        val gridAdapter = GetsuperlikeGridAdapter(requireActivity(), data)
        binding?.superlikesrecyclerview?.apply {
            adapter = gridAdapter
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tvlike -> {
                binding?.tvlike?.setBackgroundDrawable(resources.getDrawable(R.drawable.onesidedrawableround))
                binding?.tvsuperlike?.setBackgroundDrawable(resources.getDrawable(R.drawable.strokerightcornerdrawable))

                binding?.likesrecyclerview?.visibility = View.VISIBLE
                binding?.superlikesrecyclerview?.visibility = View.GONE
                binding?.tvlike?.setTextColor(Color.WHITE)
                binding?.tvsuperlike?.setTextColor(Color.BLACK)
            }

            R.id.tvsuperlike -> {
                binding?.tvlike?.setBackgroundDrawable(resources.getDrawable(R.drawable.strokeleftsidedrawable))
                binding?.tvlike?.setTextColor(Color.BLACK)
                binding?.tvsuperlike?.setTextColor(Color.WHITE)

                binding?.tvsuperlike?.setBackgroundDrawable(resources.getDrawable(R.drawable.onesideroundrightdrawable))

                binding?.likesrecyclerview?.visibility = View.GONE
                binding?.superlikesrecyclerview?.visibility = View.VISIBLE

            }


        }
    }
    private fun bindobservers() {
        viewModel.getalllikeliveData.value = null
        viewModel.getalllikeliveData.removeObservers(viewLifecycleOwner)
        viewModel.getalllikeliveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
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
    private fun bindobserversgetsuperlike() {
        viewModel.getsuperlikeliveData.value = null
        viewModel.getsuperlikeliveData.removeObservers(viewLifecycleOwner)
        viewModel.getsuperlikeliveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(requireContext(), response.data?.message, Toast.LENGTH_SHORT)
                        .show()
                    getsuperlikeGridview(response?.data?.data)

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