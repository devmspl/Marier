package com.app.marier.fragments

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.marier.R
import com.app.marier.activities.HomeActivity
import com.app.marier.adapter.CardStackAdapter
import com.app.marier.adapter.GenderRecyclerViewAdapter
import com.app.marier.adapter.SexualRecylerAdapter
import com.app.marier.databinding.FragmentDiscovery2Binding
import com.app.marier.datamodel.CreateSuperlike.createsuperlikeRequest.CreateSuperlikeRequest
import com.app.marier.datamodel.createlike.createlikerequest.CreateLikeReq
import com.app.marier.datamodel.getusermodel.Data
import com.app.marier.interfaces.ItemClickListener
import com.app.marier.staticmodel.GenderModel
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.viewmodel.UserModuleviewModel
import com.yuyakaido.android.cardstackview.*


class DiscoveryFragment2 : Fragment(), CardStackListener, View.OnClickListener {
    private var datalist: List<Data>? = ArrayList()
    private var binding: FragmentDiscovery2Binding? = null
    private var manager: CardStackLayoutManager? = null
    var itemClickListener: ItemClickListener? = null
    private var genderRecyclerViewAdapter: GenderRecyclerViewAdapter? = null
    private var sexualRecylerAdapter: SexualRecylerAdapter? = null
    lateinit var viewmodel: UserModuleviewModel
 var  token = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiscovery2Binding.inflate(layoutInflater, container, false)
        manager = CardStackLayoutManager(context, this)
        setupCardStackView()
        listners()

        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewmodel = (activity as HomeActivity).viewModel
         token = CSPreferences.readString(requireActivity(), Utils.TOKEN)!!
        viewmodel.getalluserFun(token!!, "30", "1")
        bindobservers()
    }


    private fun listners() {
        binding?.linfilter?.setOnClickListener(this)
        binding?.imgrefresh?.setOnClickListener(this)
        binding?.linsuperlike?.setOnClickListener(this)
        binding?.imgexit?.setOnClickListener(this)
        binding?.imgtrends?.setOnClickListener(this)
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun bindobservers() {
        viewmodel.getallusersliveData.value = null
        viewmodel.getallusersliveData.removeObservers(viewLifecycleOwner)
        viewmodel.getallusersliveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Utils.hideDialog()

                    initofcardstack(response?.data?.data)
                    this.datalist = response?.data?.data

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

    private fun initofcardstack(data: List<Data>?) {
        val adapter = CardStackAdapter(requireActivity(), data)
        binding?.cardStackView?.layoutManager = manager

        binding?.cardStackView?.adapter = adapter
        binding?.cardStackView?.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }

    }


    private fun initialize() {
        manager?.setStackFrom(StackFrom.None)
        manager?.setVisibleCount(5)
        manager?.setTranslationInterval(8.0f)
        manager?.setScaleInterval(0.95f)
        manager?.setSwipeThreshold(0.1f)
        manager?.setMaxDegree(20.0f)
        manager?.setDirections(Direction.FREEDOM)
        manager?.setCanScrollHorizontal(true)
        manager?.setCanScrollVertical(true)
        manager?.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)

    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        if (direction == Direction.Right) {
            binding?.imglike?.visibility = View.VISIBLE
            binding?.linmain?.visibility = View.GONE
            binding?.imgquit?.visibility = View.GONE
            binding?.imgsuperlike?.visibility = View.GONE


        } else if (direction == Direction.Left) {
            binding?.imglike?.visibility = View.GONE
            binding?.imgquit?.visibility = View.VISIBLE
            binding?.linmain?.visibility = View.GONE
            binding?.imgsuperlike?.visibility = View.GONE

        } else if (direction == Direction.Top) {
            binding?.imgsuperlike?.visibility = View.VISIBLE
            binding?.imglike?.visibility = View.GONE
            binding?.linmain?.visibility = View.GONE
            binding?.imgquit?.visibility = View.GONE
        } else if (direction == Direction.Bottom) {
            binding?.imgsuperlike?.visibility = View.GONE
            binding?.imglike?.visibility = View.GONE
            binding?.linmain?.visibility = View.GONE
            binding?.imgquit?.visibility = View.GONE
        }else if (direction ==Direction.FREEDOM){
            binding?.imgsuperlike?.visibility = View.GONE
            binding?.imglike?.visibility = View.GONE
            binding?.linmain?.visibility = View.GONE
            binding?.imgquit?.visibility = View.GONE
        }


    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> {
                viewmodel.createlikeMethod(retrunrequestData(false))
                bindobserverscreatelike()
            }

            Direction.Top -> {
                viewmodel.createsuperlike(returncreatesuperlike())
                bindobserverscreatesuperlike()
            }
            Direction.Left->{
                val ouruserid = CSPreferences.readString(requireActivity(), Utils.USERID)
                val anotheruserid = datalist!!.get(manager!!.topPosition)._id
                viewmodel.createdislike(ouruserid!!,anotheruserid!!)
                bindobserverscreatedislike()
            }

            else -> {}
        }

//        if (direction==Direction.Top){
//            homePresenter?.likeuser(data.get(manager!!.topPosition-1)._id,"true")
//        }else if (direction == Direction.Right){
//            binding?.imglike?.visibility = View.VISIBLE
//            homePresenter?.likeuser(data.get(manager!!.topPosition-1)._id,"false")
//            binding?.linmain?.visibility = View.VISIBLE
//
//
//        }
//
//        if (data.size==manager?.topPosition){
//            reload()
//            homePresenter?.getrandomuser()
//        }
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
        binding?.imglike?.visibility = View.GONE
        binding?.imgquit?.visibility = View.GONE
        binding?.imgsuperlike?.visibility = View.GONE
        binding?.linmain?.visibility = View.VISIBLE


    }

    override fun onCardAppeared(view: View?, position: Int) {
        binding?.imglike?.visibility = View.GONE
        binding?.imgsuperlike?.visibility = View.GONE
        binding?.imgquit?.visibility = View.GONE
        binding?.linmain?.visibility = View.VISIBLE


    }

    override fun onCardDisappeared(view: View?, position: Int) {
        binding?.imglike?.visibility = View.GONE
        binding?.imgquit?.visibility = View.GONE


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
        val radioclassic = dialog.findViewById<View>(R.id.radioclassic) as RadioButton
        val radiogallery = dialog.findViewById<View>(R.id.radiogallery) as RadioButton
        val tvfilter = dialog.findViewById<View>(R.id.tvfilter) as TextView
        val radioGroup = dialog?.findViewById<RadioGroup>(R.id.radioGroup)
        val navController =
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView3)
        val imgback = dialog.findViewById<View>(R.id.imgback) as ImageView
        val tvsave = dialog.findViewById<View>(R.id.tvsave) as TextView

        tvsave?.setOnClickListener {
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


    private fun getData(): List<GenderModel> {
        return listOf(
            GenderModel("Female"),
            GenderModel("Male"),
            GenderModel("Transgender Female"),
            GenderModel("Transgender Male"),
            GenderModel("Transgender"),
        )
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.linfilter -> showFilterDialog()
            R.id.linsuperlike ->{
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Top)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager?.setSwipeAnimationSetting(setting)
                binding?.cardStackView?.swipe()
            }

            R.id.imgexit ->{
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager?.setSwipeAnimationSetting(setting)
                binding?.cardStackView?.swipe()
            }
            R.id.imgtrends->{
                val setting = SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Slow.duration)
                    .setInterpolator(AccelerateInterpolator())
                    .build()
                manager?.setSwipeAnimationSetting(setting)
                binding?.cardStackView?.swipe()
            }

            R.id.imgrefresh -> {
                viewmodel.getalluserFun(token!!, "30", "1")
        bindobservers()
            }

        }
    }
//    private fun reload() {
//        val adapter = CardStackAdapter(requireActivity())
//
//        val old = adapter.getSpots()
//        val new = createSpots()
//        val callback = SpotDiffCallback(old, new)
//        val result = DiffUtil.calculateDiff(callback)
//        adapter.setSpots(new)
//        result.dispatchUpdatesTo(adapter)
//    }

    private fun sexualData(): List<String> {
        return listOf("Strainght", "Gay", "Lesbian", "Bisexual", "Panasexual")
    }
//    private fun createSpots(): ArrayList<TrendsDatum> {
//        val spots = ArrayList<TrendsDatum>()
//
//        return spots
//    }


    private fun retrunrequestData(b: Boolean): CreateLikeReq {

        val ouruserid = CSPreferences.readString(requireActivity(), Utils.USERID)
        val anotheruserid = datalist!!.get(manager!!.topPosition-1)._id
        return CreateLikeReq(b, ouruserid!!, anotheruserid)
    }

    private fun returncreatesuperlike(): CreateSuperlikeRequest {
        val ouruserid = CSPreferences.readString(requireActivity(), Utils.USERID)
        val anotheruserid = datalist!!.get(manager!!.topPosition)._id
        return CreateSuperlikeRequest(true, ouruserid!!, anotheruserid!!)
    }

    private fun bindobserverscreatelike() {
        viewmodel.createlikeliveData.value = null
        viewmodel.createlikeliveData.removeObservers(viewLifecycleOwner)
        viewmodel.createlikeliveData.observe(viewLifecycleOwner, Observer { response ->
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
//                    Utils.showDialogMethod(activity, activity?.fragmentManager)
                }
            }
        })
    }

    private fun bindobserverscreatesuperlike() {
        viewmodel.createsuperlikeliveData.value = null
        viewmodel.createsuperlikeliveData.removeObservers(viewLifecycleOwner)
        viewmodel.createsuperlikeliveData.observe(viewLifecycleOwner, Observer { response ->
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
//                    Utils.showDialogMethod(activity, activity?.fragmentManager)
                }
            }
        })
    }

    private fun bindobserverscreatedislike() {
        viewmodel.createdislikeliveData.value = null
        viewmodel.createdislikeliveData.removeObservers(viewLifecycleOwner)
        viewmodel.createdislikeliveData.observe(viewLifecycleOwner, Observer { response ->
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
//                    Utils.showDialogMethod(activity, activity?.fragmentManager)
                }
            }
        })
    }

}