package com.uni.data.internal.common

import com.uni.data.models.*
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface MyApi {

    @POST("riders/daily_runsheet_api/")
    fun uploadDailyRunsheet(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<SimpleResponse>

    @POST("riders/login/")
    suspend fun userLogin(
        @Body request: RequestBody
    ): Response<User>

    @POST("riders/rider_details_update/")
    suspend fun editProfile(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @GET("riders/rider_runsheets_stats_list/")
    suspend fun getRunsheets(
            @Query("month") month: String?,
            @Query("year") year: String?,
            @Header("Authorization") token: String?
    ): Response<List<Runsheet>>

    @GET("riders/feedbacks_list_api/")
    suspend fun getFeedbacks(
            @Header("Authorization") token: String?
    ): Response<FeedbackResponse>

    @GET("superadmin/lma/rider_salary_list_api/")
    suspend fun getSalary(
            @Header("Authorization") token: String?
    ): Response<SalaryResponse>

    @POST("riders/feedback_api/")
    suspend fun customerDetails(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @PUT("spanel/close_runsheet/")
    suspend fun closeRunsheet(
            @Query("id") id: String?,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @GET("riders/profile_otp/")
    suspend fun sendOtp(
            @Query("ph_num") phone: String?,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @POST("riders/verify_profile_otp/")
    suspend fun verifyOtp(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @POST("spanel/ref_no_verification/")
    suspend fun refVerifiedPost(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    @POST("spanel/ce_selfie_upload/")
    fun uploadSelfie(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<SimpleResponse>

    @POST("spanel/shop_bill_image_upload/")
    fun uploadCollected(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<SimpleResponse>

    @POST("spanel/deposit_image_upload/")
    fun uploadDeposit(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<SimpleResponse>

    @POST("spanel/profile_pic_upload/")
    fun uploadProfilePic(
            @Body file: RequestBody?,
            @Header("Authorization") token: String?
    ): Call<ProfilePicResponse>

    @POST("riders/change_password/")
    suspend fun changePassword(
            @Body request: RequestBody,
            @Header("Authorization") token: String?
    ): Response<SimpleResponse>

    companion object {
        operator fun invoke(): MyApi {
            val okkHttpclient = OkHttpClient.Builder()
                .build()
            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}
