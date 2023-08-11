package com.app.chattyai.api

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
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface UserModuleAPI {
    @POST("users/loginOtp")
    suspend fun loginAPI(
        @Body jsonObject: JsonObject
    ): Response<LoginExample>

    @POST("users/loginOtpVerification")
    suspend fun otpAPI(
        @Header("x-access-token") token: String,
        @Body jsonObject: JsonObject
    ): Response<OtpExample>

    @PUT("users/update/{id}")
    suspend fun adduserDataAPI(
        @Header("x-access-token") token: String,
        @Path("id") id: String,
        @Body adduserDataRequest: AdduserDataRequest
    ): Response<AdduserExample>

    @PUT("users/addIntersts/{id}")
    suspend fun addinterestAPI(
        @Header("x-access-token") token: String,
        @Path("id") id: String,
        @Body addinterestRequest: AddinterestRequest
    ): Response<AddinterestExample>

    @GET("interests/getAll")
    suspend fun getAllInterest(@Header("x-access-token") token: String): Response<GetAllinterestExample>

    @Multipart
    @PUT("users/uploadImagesByUserId/{id}")
    suspend fun addphpotosAPI(
        @Part image: ArrayList<MultipartBody.Part>,
        @Path("id") id: String
    ): Response<AddphotosExample>

    @GET("users/getAllUser")
    suspend fun getAlluserAPI(
        @Header("x-access-token") token: String,
        @Query("pageSize") pagesize: String,
        @Query("pageNo") pageNo: String
    ): Response<GetAlluserExample>

    @GET("users/current/{id}")
    suspend fun getcurrentuserByid(
        @Header("x-access-token") token: String,
        @Path("id") id: String
    ): Response<GetCurrentUserByIdExample>

    @Multipart
    @PUT("users/profileImageUpload/{id}")
    suspend fun uploadprofileimage(
        @Part image: MultipartBody.Part,
        @Path("id") id: String
    ): Response<UploadProfileimageExample>

    //createlike
    @POST("likes/create")
    suspend fun createLikeAPI(@Body createLikeReq: CreateLikeReq): Response<CreateLikeExample>

    @GET("likes/likesByOther/{userId}")
    suspend fun getallikesByUserid(
        @Header("x-access-token") token: String,
        @Path("userId") id: String
    ): Response<GetLikeByOtheruserExample>

    @POST("likes/superLike")
    suspend fun createSuperlikes(@Body createSuperlikeRequest: CreateSuperlikeRequest): Response<CreateSuperLikeExample>

    @GET("likes/getSuperLikes/{userId}")
    suspend fun getsuperlikes(@Path("userId") userid: String): Response<Getsuperlikeexample>

    //createdislike
    @POST("dislikes/create")
    suspend fun createdislike(@Body jsonObject: JsonObject): Response<CreateDislikeExample>

    //getalldislikes
    @GET("dislikes/getDislikes")
    suspend fun getalldislikes(
        @Query("userId") userid: String,
        @Query("pageNo") pageNo: String,
        @Query("pageSize") pagesize: String
    ): Response<GetalldislikesExample>

    //createblock
    @POST("blocks/create")
    suspend fun createblocks(@Body blockRequest: BlockRequest): Response<BlockExample>

    //getallblockusers
    @GET("blocks/getUserBlocks")
    suspend fun getallblockusers(
        @Query("userId") userid: String, @Query("pageNo") pageNo: String,
        @Query("pageSize") pageSize: String
    ): Response<GetblockuserExample>

    @PUT("blocks/unblock")
    suspend fun unblockuser(@Body unblockRequest: UnblockRequest):Response<UnblockExample>
    @POST("users/createAgoraToken")
    suspend fun createagoratoken(@Body jsonObject: JsonObject):Response<CreateAgoraTokenExample>
    //getinterestbyidAPI___________
    @GET("interests/getById/{id}")
    suspend fun getinterestbyid(@Header("x-access-token")token: String,@Path("id")id: String):Response<GetInterestByIdExample>

    //getfaqapi
    @GET("faqs/getAll")
    suspend fun getallfaqsAPI():Response<GetFaqExample>
    //getinterestByUserid
    @GET("users/getUserInterest/{userId}")
    suspend fun getinterestByuserid(@Path("userId")userid: String):Response<GetInterestByUserIDExample>
    //socialloginapi-----
    @POST("users/socialLogin")
    suspend fun socialLoginAPI(@Body socialRequest: SocialRequest):Response<SocialLoginExample>

}

