package com.app.marier.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.marier.R
import com.app.marier.adapter.LikesGridAdapter
import com.app.marier.adapter.RecentLikesGridAdapter
import com.app.marier.databinding.FragmentRecentPassesBinding


class RecentPassesFragment : Fragment() {
   private var binding:FragmentRecentPassesBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecentPassesBinding.inflate(layoutInflater,container,false)
        initrecyclerview()
        return binding!!.root
    }

    private fun initrecyclerview() {
        val likesRecyclerAdapter = RecentLikesGridAdapter(requireActivity())
        binding?.likesgridview?.apply {

            adapter = likesRecyclerAdapter
        }


    }

}