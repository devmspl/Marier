package com.app.marier.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.marier.datamodel.CreateSuperlike.CreateSuperLikeExample
import com.app.marier.datamodel.CreateSuperlike.createsuperlikeRequest.CreateSuperlikeRequest
import com.app.marier.datamodel.Getsuperlikemodel.Getsuperlikeexample
import com.app.marier.datamodel.LoginExample
import com.app.marier.datamodel.addinterest.AddinterestExample
import com.app.marier.datamodel.addinterest.AddinterestRequest
import com.app.marier.datamodel.addphotos.AddphotosExample
import com.app.marier.datamodel.adduserdatamodel.AdduserExample
import com.app.marier.datamodel.adduserdatamodel.request.AdduserDataRequest
import com.app.marier.datamodel.block.BlockExample
import com.app.marier.datamodel.block.blockreq.BlockRequest
import com.app.marier.datamodel.block.getblockuser.GetblockuserExample
import com.app.marier.datamodel.createagoratoken.CreateAgoraTokenExample
import com.app.marier.datamodel.createlike.CreateLikeExample
import com.app.marier.datamodel.createlike.createlikerequest.CreateLikeReq
import com.app.marier.datamodel.dislike.CreateDislikeExample
import com.app.marier.datamodel.getallinterest.GetAllinterestExample
import com.app.marier.datamodel.getcurrentuserbyid.GetCurrentUserByIdExample
import com.app.marier.datamodel.getfaq.GetFaqExample
import com.app.marier.datamodel.getinteresetByUserId.GetInterestByUserIDExample
import com.app.marier.datamodel.getlikeotheruser.GetLikeByOtheruserExample
import com.app.marier.datamodel.getusermodel.GetAlluserExample
import com.app.marier.datamodel.otpmodel.OtpExample
import com.app.marier.datamodel.sociallogin.SocialLoginExample
import com.app.marier.datamodel.sociallogin.socialrequest.SocialRequest
import com.app.marier.datamodel.unblock.UnblockExample
import com.app.marier.datamodel.unblock.unblockreq.UnblockRequest
import com.app.marier.datamodel.uploaduserprofileimage.UploadProfileimageExample
import com.app.marier.repository.UserModuleResponseRepository
import com.app.marier.utils.MyApplication
import com.app.marier.utils.Resource
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserModuleviewModel(
    val app: Application,
    val repository: UserModuleResponseRepository
) : AndroidViewModel(app) {

    val loginlivedata: MutableLiveData<Resource<LoginExample>> = repository.loginliveData
    val otplivedata: MutableLiveData<Resource<OtpExample>> = repository.otpliveData
    val adduserlivedata: MutableLiveData<Resource<AdduserExample>> = repository.adduserliveData
    val getallinterestlivedata: MutableLiveData<Resource<GetAllinterestExample>> =
        repository.getallinterliveData
    val addinterestliveData: MutableLiveData<Resource<AddinterestExample>> =
        repository.addintersetliveData
    val addphotosliveData: MutableLiveData<Resource<AddphotosExample>> =
        repository.addmultipleimagesliveData
    val getallusersliveData: MutableLiveData<Resource<GetAlluserExample>> =
        repository.getalluserliveData
    val getcurrentuserByidliveData: MutableLiveData<Resource<GetCurrentUserByIdExample>> =
        repository.getcurrentBYIDliveData
    val uploadimageliveData: MutableLiveData<Resource<UploadProfileimageExample>> =
        repository.uploadprofileimageliveData
    val createlikeliveData: MutableLiveData<Resource<CreateLikeExample>> =
        repository.createLikeliveData
    val getalllikeliveData: MutableLiveData<Resource<GetLikeByOtheruserExample>> =
        repository.getallLikeliveData

    val createsuperlikeliveData: MutableLiveData<Resource<CreateSuperLikeExample>> =
        repository.createsuperLikeliveData
    val getsuperlikeliveData: MutableLiveData<Resource<Getsuperlikeexample>> =
        repository.getsuperLikeliveData
    val createdislikeliveData: MutableLiveData<Resource<CreateDislikeExample>> =
        repository.createdisLikeliveData
    val createblocklivedata: MutableLiveData<Resource<BlockExample>> =
        repository.createblockreqliveData
    val getallblockuserslivedata: MutableLiveData<Resource<GetblockuserExample>> =
        repository.getallblockliveData
    val unblocklivedata: MutableLiveData<Resource<UnblockExample>> = repository.unblockliveData
    val createtokenlivedata: MutableLiveData<Resource<CreateAgoraTokenExample>> =
        repository.createtokenliveData
    val getfaqlivedata: MutableLiveData<Resource<GetFaqExample>> = repository.getallfaqsliveData
    val getinterestByuseridliveData: MutableLiveData<Resource<GetInterestByUserIDExample>> = repository.getinterestByuseridliveData
    val socialloginliveData: MutableLiveData<Resource<SocialLoginExample>> = repository.socialLoginliveData


    fun loginMethod(jsonObject: JsonObject) = viewModelScope.launch {
        if (hasInternetConnection()) {

            repository.loginFun(jsonObject)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

    }

    //otpapi________________________
    fun otpMethod(token: String, otp: String) = viewModelScope.launch {
        if (hasInternetConnection()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("otp", otp)
            repository.otpFun(token, jsonObject)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    //adduserdata
    fun addUserDataFun(token: String, id: String, adduserDataRequest: AdduserDataRequest) =
        viewModelScope.launch {
            if (hasInternetConnection()) {
                repository.adduserDataFun(token, id, adduserDataRequest)
            } else {
                Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    //getallinterestAPI

    fun getallInterestFun(token: String) = viewModelScope.launch {
        if (hasInternetConnection()) {
            repository.getallinterestFun(token)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    fun addinterstFun(token: String, id: String, addinterestRequest: AddinterestRequest) =
        viewModelScope.launch {
            repository.addinterestFun(token, id, addinterestRequest)
        }

    fun addmultiplephotos(images: ArrayList<MultipartBody.Part>, id: String) =
        viewModelScope.launch {
            repository.addmultipleimages(images, id)
        }

    //getalluserapi
    fun getalluserFun(token: String, pagesize: String, pageNo: String) = viewModelScope.launch {
        repository.getalluseraFun(token, pagesize, pageNo)
    }

    //getcurrentBYid
    fun getcurrentuserByid(token: String, id: String) = viewModelScope.launch {

        if (hasInternetConnection()) repository.getcurrentuserByid(token, id) else Toast.makeText(
            app,
            "No Internet Connection",
            Toast.LENGTH_SHORT
        ).show()
    }

    //uploaduserprofile
    fun uploadUserprofileimage(image: MultipartBody.Part, id: String) = viewModelScope.launch {
        if (hasInternetConnection()) {
            repository.uploadprofileimage(image, id)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    //createlikeAPI____________________________________

    fun createlikeMethod(createLikeReq: CreateLikeReq) = viewModelScope.launch {
        if (hasInternetConnection()) {
            repository.createLikeFun(createLikeReq)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    //getallcreatelike
    fun getallcreatelikes(token: String, id: String) = viewModelScope.launch {

        repository.getallcreatlike(token, id)
    }

    //createsuperlike
    fun createsuperlike(createSuperlikeRequest: CreateSuperlikeRequest) = viewModelScope.launch {
        repository.createsuperlike(createSuperlikeRequest)
    }

    //getsuperlikes
    fun getsuperlikes(userid: String) = viewModelScope.launch {
        repository.getsuperlike(userid)
    }

    //createdislike
    fun createdislike(ouruserid: String, anotheruserid: String) = viewModelScope.launch {
        val jsonObject = JsonObject()
        jsonObject.addProperty("dislikeBy", ouruserid)
        jsonObject.addProperty("dislikeTo", anotheruserid)
        if (hasInternetConnection()) {
            repository.createdislike(jsonObject)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    //blockuser
    fun createblock(blockRequest: BlockRequest) = viewModelScope.launch {
        repository.createblockRequest(blockRequest)
    }

    //getallblockusers
    fun getallblockusers(userid: String, pageNo: String, pagesize: String) = viewModelScope.launch {
        if (hasInternetConnection()) {
            repository.getallblockusers(userid!!, pageNo!!, pagesize)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }
    //unblockyuser

    fun unblockuser(unblockRequest: UnblockRequest) = viewModelScope.launch {
        if (hasInternetConnection()) {
            repository.unblockfun(unblockRequest)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    fun createagoratoken(channelname: String) = viewModelScope.launch {
        if (hasInternetConnection()) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("channelName", channelname)
            repository.createagoratoken(jsonObject)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    //getinterestByid
    fun getinterestByidMethod(token: String, userid: String) = viewModelScope.launch {
        if (hasInternetConnection()) {
            repository.getinterestByid(token!!, userid!!)
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    fun getfaqs() = viewModelScope.launch {
        if (hasInternetConnection()) {
            repository.getallfaqs()
        } else {
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    fun getinterestByUSerID( userid: String) = viewModelScope.launch {
        if (hasInternetConnection()){
            repository.getinterestByuserid(userid)
        }else{
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()

        }
    }

    fun socialLoginAPi(socialRequest: SocialRequest) =viewModelScope.launch {
        if (hasInternetConnection()){
            repository.socialloginAPI(socialRequest)
        }else{
            Toast.makeText(app, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}