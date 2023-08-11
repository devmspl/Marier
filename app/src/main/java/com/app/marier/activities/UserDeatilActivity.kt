package com.app.marier.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.marier.R
import com.app.marier.adapter.OtherPhotsRecyclerAdapter
import com.app.marier.databinding.ActivityUserDeatilBinding
import com.app.marier.datamodel.CreateSuperlike.createsuperlikeRequest.CreateSuperlikeRequest
import com.app.marier.datamodel.createlike.createlikerequest.CreateLikeReq
import com.app.marier.datamodel.getcurrentuserbyid.Gallery
import com.app.marier.repository.UserModuleResponseRepository
import com.app.marier.utils.CSPreferences
import com.app.marier.utils.Resource
import com.app.marier.utils.Utils
import com.app.marier.utils.factories.USerViewModelFactory
import com.app.marier.viewmodel.UserModuleviewModel
import com.bumptech.glide.Glide

class UserDeatilActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityUserDeatilBinding? = null
    lateinit var viewModel: UserModuleviewModel
    var userid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDeatilBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val repository = UserModuleResponseRepository()

        val viewModelProviderFactory =
            USerViewModelFactory(application, repository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(UserModuleviewModel::class.java)
        listners()
        userid = intent.getStringExtra("userid")
        Log.d("checkingnewc",userid!!)
        var token = CSPreferences.readString(this, Utils.TOKEN)

        Log.d("tokenval",token!!)
        viewModel.getcurrentuserByid(token!!, userid!!)
        bindobserver()


    }

    private fun initrecyclerview(gallery: List<Gallery>?) {
        val otherPhotsRecyclerAdapter = OtherPhotsRecyclerAdapter(this,gallery)
        binding?.otherphotosrecyclerview?.apply {
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            adapter = otherPhotsRecyclerAdapter

        }
    }

    private fun listners() {
        binding?.imgmenu?.setOnClickListener(this)
        binding?.imgback?.setOnClickListener(this)
        binding?.imgexit?.setOnClickListener(this)
        binding?.linlike?.setOnClickListener(this)
        binding?.imgsuperlike?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgmenu -> {
                val appLink = "https://example.com/app" // Replace with your app link
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, appLink)
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }

            R.id.imgback -> finish()
            R.id.imgexit -> finish()
            R.id.linlike -> {
                viewModel.createlikeMethod(retrunrequestData())
                bindobserverCreatelike()
            }

            R.id.imgsuperlike -> {
                viewModel.createsuperlike(returncreatesuperlike())
                bindobserverCreatesuperlike()
            }


        }
    }

    private fun retrunrequestData(): CreateLikeReq {
        var ouruserid = CSPreferences.readString(this, Utils.USERID)
        return CreateLikeReq(false, ouruserid!!, userid!!)
    }

    private fun bindobserverCreatelike() {
        viewModel.createlikeliveData.value = null
        viewModel.createlikeliveData.removeObservers(this)
        viewModel.createlikeliveData.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    Utils.hideDialog()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    Utils.showDialogMethod(this, fragmentManager)
                }
            }
        })
    }


    private fun bindobserver() {
        viewModel.getcurrentuserByidliveData.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()
                    binding?.tvname?.text = it.data?.data?.name
                    binding?.tvgender?.text = it.data?.data?.sex
                    binding?.tvbio?.text = it.data?.data?.bio
                    Glide.with(this)
                        .load(it?.data?.data?.avatar)
                        .placeholder(R.drawable.user) // optional placeholder image
                        .error(R.drawable.user) // optional error image
                        .into(binding!!.img)
                    initrecyclerview(it?.data?.data?.gallery)

                }

                is Resource.Error -> {
                    Utils.hideDialog()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }

                is Resource.Loading -> {
                    Utils.showDialogMethod(this, fragmentManager)
                }
            }
        })
    }

    private fun bindobserverCreatesuperlike() {
        viewModel.createsuperlikeliveData.value = null
        viewModel.createsuperlikeliveData.removeObservers(this)
        viewModel.createsuperlikeliveData.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    Utils.hideDialog()
                    Toast.makeText(this, it.data?.message, Toast.LENGTH_SHORT).show()

                }

                is Resource.Error -> {
                    Utils.hideDialog()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }

                is Resource.Loading -> {
                    Utils.showDialogMethod(this, fragmentManager)
                }
            }
        })
    }

    private fun returncreatesuperlike(): CreateSuperlikeRequest {
        val ouruserid = CSPreferences.readString(this, Utils.USERID)
        return CreateSuperlikeRequest(true, ouruserid!!, userid!!)
    }


}