package com.app.marier.interfaces

import java.util.ArrayList

interface InterestRecyclerClickListener {
    fun onItemClick(position: Int)
    fun passdata(arrayList: ArrayList<String>)
}