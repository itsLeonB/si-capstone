package com.example.posyandu

import com.example.posyandu.features.authentication.LoginResponse
import com.example.posyandu.features.daftarRemaja.CreateRemajaRequest
import com.example.posyandu.features.daftarRemaja.CreateRemajaResponse
import com.example.posyandu.features.daftarRemaja.IndexRemajaByPosyanduResponse
import com.example.posyandu.features.daftarRemaja.UpdateRemajaRequest
import com.example.posyandu.features.daftarRemaja.UpdateRemajaResponse
import com.example.posyandu.features.jadwalPosyandu.CreateJadwalPosyanduResponse
import com.example.posyandu.features.jadwalPosyandu.JadwalPosyanduIndexResponse
import com.example.posyandu.features.jadwalPosyandu.UpdateJadwalPosyanduResponse
import com.example.posyandu.features.main.MainBidanResponse
import com.example.posyandu.features.main.posyandu.GetPosyanduResponse
import com.example.posyandu.features.main.posyandu.ListPosyanduResponse
import com.example.posyandu.features.main.posyandu.UpdatePengampuRequest
import com.example.posyandu.features.main.posyandu.UpdatePosyanduRequest
import com.example.posyandu.features.main.posyandu.UpdatePosyanduResponse
import com.example.posyandu.features.main.profile.GetProfileByIdResponse
import com.example.posyandu.features.main.profile.PutUpdateProfileRequest
import com.example.posyandu.features.main.profile.PutUpdateProfileResponse
import com.example.posyandu.features.register.RegisterUserRequest
import com.example.posyandu.features.register.RegisterUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("home/bidan")
    fun loadMainBidan(
        @Header("Authorization") token: String
    ): Call<MainBidanResponse>

    @GET("posyandu/{id}")
    fun getPosyandu(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<GetPosyanduResponse>

    @PUT("posyandu/{id}")
    fun updatePosyandu(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body posyandu: UpdatePosyanduRequest
    ): Call<UpdatePosyanduResponse>

    @GET("pengampu/bidan/{id}")
    fun getListPosyandu(
        @Path("id") bidanId: Int,
        @Header("Authorization") token: String
    ): Call<ListPosyanduResponse>

    @PUT("pengampu")
    fun updatePengampu(
        @Header("Authorization") token: String,
        @Body pengampu: UpdatePengampuRequest
    ): Call<Void>

    @GET("jadwal-posyandu")
    fun indexJadwalPosyandu(
        @Header("Authorization") token: String
    ): Call<JadwalPosyanduIndexResponse>

    @POST("jadwal-posyandu")
    fun createJadwalPosyandu(
        @Header("Authorization") token: String,
        @Body jadwalPosyandu: CreateJadwalPosyanduResponse
    ): Call<CreateJadwalPosyanduResponse>

    @PUT("jadwal-posyandu/{id}")
    fun updateJadwalPosyandu(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body jadwalPosyandu: CreateJadwalPosyanduResponse
    ): Call<UpdateJadwalPosyanduResponse>

    @DELETE("jadwal-posyandu/{id}")
    fun deleteJadwalPosyandu(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<Void>

    @GET("remaja/posyandu/{id}")
    fun indexRemajaByPosyandu(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<IndexRemajaByPosyanduResponse>

    @POST("remaja")
    fun createRemaja(
        @Header("Authorization") token: String,
        @Body remaja: CreateRemajaRequest
    ): Call<CreateRemajaResponse>

    @PUT("remaja/{id}")
    fun updateRemaja(
        @Header("Authorization") token: String,
        @Body remajaUpdate: UpdateRemajaRequest
    ): Call<UpdateRemajaResponse>

    @POST("user/register")
    fun registerUser(
        @Header("Authorization") token: String,
        @Body user: RegisterUserRequest
    ): Call<RegisterUserResponse>

    @GET("user/{id}")
    fun getProfileById (
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<GetProfileByIdResponse>

    @PUT("user/{id}")
    fun updateProfile(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body profile: PutUpdateProfileRequest
    ): Call<PutUpdateProfileResponse>
}


