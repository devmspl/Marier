package com.app.marier.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.chattyai.api.RetrofitInstance
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
import com.app.marier.datamodel.dislike.getalldislikes.GetalldislikesExample
import com.app.marier.datamodel.getallinterest.GetAllinterestExample
import com.app.marier.datamodel.getcurrentuserbyid.GetCurrentUserByIdExample
import com.app.marier.datamodel.getfaq.GetFaqExample
import com.app.marier.datamodel.getinteresetByUserId.GetInterestByUserIDExample
import com.app.marier.datamodel.getinterestByid.GetInterestByIdExample
import com.app.marier.datamodel.getlikeotheruser.GetLikeByOtheruserExample
import com.app.marier.datamodel.getusermodel.GetAlluserExample
import com.app.marier.datamodel.otpmodel.OtpExample
import com.app.marier.datamodel.sociallogin.SocialLoginExample
import com.app.marier.datamodel.sociallogin.socialrequest.SocialRequest
import com.app.marier.datamodel.unblock.UnblockExample
import com.app.marier.datamodel.unblock.unblockreq.UnblockRequest
import com.app.marier.datamodel.uploaduserprofileimage.UploadProfileimageExample
import com.app.marier.utils.Resource
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

class UserModuleResponseRepository {
    val loginliveData: MutableLiveData<Resource<LoginExample>> = MutableLiveData()
    val otpliveData: MutableLiveData<Resource<OtpExample>> = MutableLiveData()
    val adduserliveData: MutableLiveData<Resource<AdduserExample>> = MutableLiveData()
    val getallinterliveData: MutableLiveData<Resource<GetAllinterestExample>> = MutableLiveData()
    val addintersetliveData: MutableLiveData<Resource<AddinterestExample>> = MutableLiveData()
    val addmultipleimagesliveData: MutableLiveData<Resource<AddphotosExample>> = MutableLiveData()
    val getalluserliveData: MutableLiveData<Resource<GetAlluserExample>> = MutableLiveData()
    val getcurrentBYIDliveData: MutableLiveData<Resource<GetCurrentUserByIdExample>> =
        MutableLiveData()
    val uploadprofileimageliveData: MutableLiveData<Resource<UploadProfileimageExample>> =
        MutableLiveData()
    val createLikeliveData: MutableLiveData<Resource<CreateLikeExample>> = MutableLiveData()
    val getallLikeliveData: MutableLiveData<Resource<GetLikeByOtheruserExample>> = MutableLiveData()
    val createsuperLikeliveData: MutableLiveData<Resource<CreateSuperLikeExample>> = MutableLiveData()
    val getsuperLikeliveData: MutableLiveData<Resource<Getsuperlikeexample>> = MutableLiveData()
    val createdisLikeliveData: MutableLiveData<Resource<CreateDislikeExample>> = MutableLiveData()
    val getalldisLikeliveData: MutableLiveData<Resource<GetalldislikesExample>> = MutableLiveData()
    val createblockreqliveData: MutableLiveData<Resource<BlockExample>> = MutableLiveData()
    val getallblockliveData: MutableLiveData<Resource<GetblockuserExample>> = MutableLiveData()
    val unblockliveData: MutableLiveData<Resource<UnblockExample>> = MutableLiveData()
    val createtokenliveData: MutableLiveData<Resource<CreateAgoraTokenExample>> = MutableLiveData()
    val getinterestByidliveData: MutableLiveData<Resource<GetInterestByIdExample>> = MutableLiveData()
    val getallfaqsliveData: MutableLiveData<Resource<GetFaqExample>> = MutableLiveData()
    val getinterestByuseridliveData: MutableLiveData<Resource<GetInterestByUserIDExample>> = MutableLiveData()
    val socialLoginliveData: MutableLiveData<Resource<SocialLoginExample>> = MutableLiveData()


    suspend fun loginFun(jsonObject: JsonObject) {
        loginliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.loginAPI(jsonObject)
            handleLoginResponse(response)

        } catch (t: Throwable) {
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            loginliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handleLoginResponse(response: Response<LoginExample>) {
        if (response.isSuccessful) {
            response.body()?.let { loginResponse ->
                loginliveData.postValue(Resource.Success(loginResponse))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            loginliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //otpapi_________________________________________
    suspend fun otpFun(token: String, jsonObject: JsonObject) {
        otpliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.otpAPI(token, jsonObject)
            handleOtpResponse(response)

        } catch (t: Throwable) {
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            otpliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handleOtpResponse(response: Response<OtpExample>) {
        if (response.isSuccessful) {
            response.body()?.let { otpResponse ->
                otpliveData.postValue(Resource.Success(otpResponse))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            otpliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //adduserdata
    suspend fun adduserDataFun(token: String, id: String, adduserDataRequest: AdduserDataRequest) {
        adduserliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.adduserDataAPI(token, id, adduserDataRequest)
            handleUserDataResponse(response)

        } catch (t: Throwable) {
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            adduserliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handleUserDataResponse(response: Response<AdduserExample>) {
        if (response.isSuccessful) {
            response.body()?.let { adduserResponse ->
                adduserliveData.postValue(Resource.Success(adduserResponse))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            adduserliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //getallinterestapi
    suspend fun getallinterestFun(token: String) {
        getallinterliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.getAllInterest(token)
            handlegetinterestResponse(response)
        } catch (t: Throwable) {
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getallinterliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handlegetinterestResponse(response: Response<GetAllinterestExample>) {
        if (response.isSuccessful) {
            response.body()?.let { adduserResponse ->
                getallinterliveData.postValue(Resource.Success(adduserResponse))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            getallinterliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //addinterestreq
    suspend fun addinterestFun(token: String, id: String, addinterestRequest: AddinterestRequest) {
        addintersetliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.addinterestAPI(token, id, addinterestRequest)
            handleaddinterestResponse(response)

        } catch (t: Throwable) {
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            addintersetliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handleaddinterestResponse(response: Response<AddinterestExample>) {
        if (response.isSuccessful) {
            response.body()?.let { addinterestResponse ->
                addintersetliveData.postValue(Resource.Success(addinterestResponse))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            addintersetliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //addmultipleimagesapi
    suspend fun addmultipleimages(images: ArrayList<MultipartBody.Part>, id: String) {
        addmultipleimagesliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.addphpotosAPI(images, id)
            handlemultipleimageresponse(response)
        } catch (t: Throwable) {
            Log.d("checkingerror",t.toString())
            val errorMessages = when (t) {

                is IOException -> "Network Failure"
                else -> "Conversion Error"

            }
            addmultipleimagesliveData.postValue(Resource.Error(errorMessages))
        }

    }

    private fun handlemultipleimageresponse(response: Response<AddphotosExample>) {
        if (response.isSuccessful) {
            response.body()?.let {
                addmultipleimagesliveData.postValue(Resource.Success(it))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            addintersetliveData.postValue(Resource.Error(errorMessage))
        }

    }

    //getalluser
    suspend fun getalluseraFun(token: String, pagesize: String, pageNo: String) {
        getalluserliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.getAlluserAPI(token, pagesize, pageNo)
            handlegetalluserresponse(response)
        } catch (t: Throwable) {
            val errorMessge = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getalluserliveData.postValue(Resource.Error(errorMessge))
        }

    }

    private fun handlegetalluserresponse(response: Response<GetAlluserExample>) {
        if (response.isSuccessful) {
            response.body()?.let {
                getalluserliveData.postValue(Resource.Success(it))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            getalluserliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //getcurrentuserbyid
    suspend fun getcurrentuserByid(token: String, id: String) {
        getcurrentBYIDliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.getcurrentuserByid(token, id)
            handlecurrentuserByidResponse(response)
        } catch (t: Throwable) {
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getcurrentBYIDliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handlecurrentuserByidResponse(response: Response<GetCurrentUserByIdExample>) {
        if (response.isSuccessful) {
            response.body()?.let {
                getcurrentBYIDliveData.postValue(Resource.Success(it))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            getcurrentBYIDliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //uploadprofileimage______________________________________
    suspend fun uploadprofileimage(image: MultipartBody.Part, userid: String) {
        uploadprofileimageliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.uploadprofileimage(image, userid)
            handleuploadprofileimageResponse(response)
        } catch (t: Throwable) {
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            uploadprofileimageliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handleuploadprofileimageResponse(response: Response<UploadProfileimageExample>) {
        if (response.isSuccessful) {
            response.body()?.let {
                uploadprofileimageliveData.postValue(Resource.Success(it))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            uploadprofileimageliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //createlikeAPI_____________________________________________

    suspend fun createLikeFun(createLikeReq: CreateLikeReq) {
        createLikeliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.createLikeAPI(createLikeReq)
            handleCreateLikeresponse(response)
        } catch (t: Throwable) {
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            createLikeliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handleCreateLikeresponse(response: Response<CreateLikeExample>) {
        if (response.isSuccessful) {
            response.body()?.let {
                createLikeliveData.postValue(Resource.Success(it))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            createLikeliveData.postValue(Resource.Error(errorMessage))
        }

    }

    //getallcreatelike
    suspend fun getallcreatlike(token: String, id: String) {
        getallLikeliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.getallikesByUserid(token, id)
            handlegetalllikeResponse(response)
        } catch (t: Throwable) {
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getallLikeliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private  fun handlegetalllikeResponse(response: Response<GetLikeByOtheruserExample>) {
        if (response.isSuccessful) {
            response.body()?.let {
                getallLikeliveData.postValue(Resource.Success(it))
            }
        } else {
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            createLikeliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //createsuperlike_______________________________________________________________
    suspend fun createsuperlike(createSuperlikeRequest: CreateSuperlikeRequest){
        createsuperLikeliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.createSuperlikes(createSuperlikeRequest)
            handleresponseofcreatesuperlike(response)
        }catch (t:Throwable){
            when(t){
                is IOException->"Network Failure"
                else -> "Conversion Error"
            }
        }
    }

    private fun handleresponseofcreatesuperlike(response: Response<CreateSuperLikeExample>){
        if (response.isSuccessful){
            response.body()?.let {
                createsuperLikeliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            createsuperLikeliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //getsuperlikeapi-----------------

    suspend fun getsuperlike(userid: String){
        getsuperLikeliveData.postValue(Resource.Loading())
        try {
            val response  = RetrofitInstance.api.getsuperlikes(userid)
            handlegetsuperlikeresponse(response)
        }catch (t:Throwable){
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getsuperLikeliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handlegetsuperlikeresponse(response: Response<Getsuperlikeexample>){
        if (response.isSuccessful){
            response.body()?.let {
                getsuperLikeliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            getsuperLikeliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //createdislike
    suspend fun createdislike(jsonObject: JsonObject){
        createdisLikeliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.createdislike(jsonObject)
            handlecreatedislikeresponse(response)
        }catch (t:Throwable){
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            createdisLikeliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handlecreatedislikeresponse(response: Response<CreateDislikeExample>){
        if (response.isSuccessful){
            response?.body()?.let {
                createdisLikeliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            createdisLikeliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //getalldislike

    suspend fun getalldislikes(userid: String,pageNo: String,pagesize: String){
        getalldisLikeliveData.postValue(Resource.Loading())
        try {
            val response  = RetrofitInstance.api.getalldislikes(userid!!,pageNo!!,pagesize)
            handlegetalldislikesresponse(response)
        }catch (t:Throwable){
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getalldisLikeliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handlegetalldislikesresponse(response: Response<GetalldislikesExample>){
        if (response.isSuccessful){
            response.body()?.let {
                getalldisLikeliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            getalldisLikeliveData.postValue(Resource.Error(errorMessage))
        }

    }

    //createblockreq

    suspend fun createblockRequest(blockRequest: BlockRequest){
        createblockreqliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.createblocks(blockRequest)
            handleblockreqResponse(response)
        }catch (t:Throwable){
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            createblockreqliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handleblockreqResponse(response: Response<BlockExample>){
        if(response.isSuccessful){
            response?.body()?.let {
                createblockreqliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            createblockreqliveData.postValue(Resource.Error(errorMessage))
        }

    }
    //getallblockusers________________________________________________________________________________________
   suspend fun getallblockusers(userid: String,pageNo: String,pagesize: String){
        getallblockliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.getallblockusers(userid!!,pageNo!!,pagesize!!)
            handlegetallblocksusers(response)
        }catch (t:Throwable){
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getallblockliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handlegetallblocksusers(response: Response<GetblockuserExample>){
        if (response.isSuccessful){
            response.body()?.let {
                getallblockliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            getallblockliveData.postValue(Resource.Error(errorMessage))
        }

    }
    //unblockapi
    suspend fun unblockfun(unblockRequest: UnblockRequest){
        unblockliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.unblockuser(unblockRequest)
            handleunblockresponse(response)
        }catch (t:Throwable){
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            unblockliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handleunblockresponse(response: Response<UnblockExample>){
        if (response.isSuccessful){
            response?.body()?.let {
                unblockliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            unblockliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //createagoratoken_____________________________________________________________
    suspend fun createagoratoken(jsonObject: JsonObject){
        createtokenliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.createagoratoken(jsonObject)
            handlecreatetokenresponse(response)
        }catch (t:Throwable){
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            createtokenliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handlecreatetokenresponse(response: Response<CreateAgoraTokenExample>){
        if (response.isSuccessful){
            response?.body()?.let {
                createtokenliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            createtokenliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //getinterestByid_____________
    suspend fun getinterestByid(token: String,id: String){
        getinterestByidliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.getinterestbyid(token,id)
            handleresponsegetinterstByid(response)
        }catch (t:Throwable){
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getinterestByidliveData.postValue(Resource.Error(errorMessage))
        }
    }
    private fun handleresponsegetinterstByid(response: Response<GetInterestByIdExample>){
        if (response.isSuccessful){
            response?.body()?.let {
                getinterestByidliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            getinterestByidliveData.postValue(Resource.Error(errorMessage))
        }
    }
    //getfaqsapi______________
    suspend fun getallfaqs(){
        getallfaqsliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.getallfaqsAPI()
            handlegetfaqsResponse(response)
        }catch (t:Throwable){
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getallfaqsliveData.postValue(Resource.Error(errorMessage))
        }
    }
    private fun handlegetfaqsResponse(response: Response<GetFaqExample>){
        if(response.isSuccessful){
            response?.body()?.let {
                getallfaqsliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            getallfaqsliveData.postValue(Resource.Error(errorMessage))
        }
    }

    //getinterestBYUserid
    suspend fun getinterestByuserid(userid: String){
        getinterestByuseridliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.getinterestByuserid(userid)
            handlegetinterestByuseridResponse(response)
        }catch (t:Throwable){
            Log.d("checkingerror",t.toString())
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            getinterestByuseridliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handlegetinterestByuseridResponse(response: Response<GetInterestByUserIDExample>){
        if (response.isSuccessful){
            response?.body()?.let {
                getinterestByuseridliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            getinterestByidliveData.postValue(Resource.Error(errorMessage))
        }
    }
    suspend fun socialloginAPI(socialRequest: SocialRequest){
        socialLoginliveData.postValue(Resource.Loading())
        try {
            val response = RetrofitInstance.api.socialLoginAPI(socialRequest)
            handlesocialapiresponse(response)
        }catch (t:Throwable){
            Log.d("checkingerror",t.toString())
            val errorMessage = when (t) {
                is IOException -> "Network Failure"
                else -> "Conversion Error"
            }
            socialLoginliveData.postValue(Resource.Error(errorMessage))
        }
    }

    private fun handlesocialapiresponse(response: Response<SocialLoginExample>){
        if (response.isSuccessful){
            response?.body()?.let {
                socialLoginliveData.postValue(Resource.Success(it))
            }
        }else{
            val errorMessage = response.errorBody()?.string()?.let {
                try {
                    JSONObject(it).getString("message")
                } catch (e: Exception) {
                    "Unknown error"
                }
            } ?: "Unknown error"
            Log.e("API Error", "Response code: ${response.code()}. Error message: $errorMessage")

            socialLoginliveData.postValue(Resource.Error(errorMessage))
        }
    }

}