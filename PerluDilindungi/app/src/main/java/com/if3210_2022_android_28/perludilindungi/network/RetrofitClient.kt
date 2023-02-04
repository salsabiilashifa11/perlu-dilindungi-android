package com.if3210_2022_android_28.perludilindungi.network

import com.if3210_2022_android_28.perludilindungi.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://perludilindungi.herokuapp.com/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create()).baseUrl(BASE_URL).build()

interface NewsApi {
    @GET("api/get-news")
    suspend fun getNews(): NewsResponse
}

interface FaskesApi {
    @GET("api/get-faskes-vaksinasi")
    suspend fun getFaskes(
        @Query("province") province: String,
        @Query("city") city: String
    ) : FaskesResponse

}

interface KotaApi {
    @GET("api/get-city")
    suspend fun getKota(
        @Query("start_id") province: String
    ) : KotaResponse
}

interface ProvinsiApi {
    @GET("api/get-province")
    suspend fun getProvinsi() : ProvinsiResponse
}

interface QrCodeApi {
    @Headers("Content-Type: application/json")
    @POST("check-in")
    suspend fun getUserStatus(@Body qrCodePost: QrCodePost): Response<QrCode>
}

object RetrofitClient {
    val newsInstance: NewsApi by lazy {
        retrofit.create(NewsApi::class.java)
    }
    val faskesInstance: FaskesApi by lazy {
        retrofit.create(FaskesApi::class.java)
    }

    val provinsiInstance: ProvinsiApi by lazy {
        retrofit.create(ProvinsiApi::class.java)
    }

    val kotaInstance: KotaApi by lazy {
        retrofit.create(KotaApi::class.java)
    }
    val qrCodeInstance: QrCodeApi by lazy {
        retrofit.create(QrCodeApi::class.java)
    }
}