package com.example.posyandu.utils

import com.example.posyandu.features.authentication.LoginRequest
import com.example.posyandu.features.authentication.LoginResponse
import com.example.posyandu.features.daftarRemaja.CreateRemajaRequest
import com.example.posyandu.features.daftarRemaja.CreateRemajaResponse
import com.example.posyandu.features.daftarRemaja.GetRemajaResponse
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
import com.example.posyandu.features.pemeriksaan.CreatePemeriksaanRequest
import com.example.posyandu.features.pemeriksaan.CreatePemeriksaanResponse
import com.example.posyandu.features.pemeriksaan.GetPemeriksaanResponse
import com.example.posyandu.features.pemeriksaan.IndexPemeriksaanByRemajaResponse
import com.example.posyandu.features.pemeriksaan.PemeriksaanUpdateRequest
import com.example.posyandu.features.pemeriksaan.PemeriksaanUpdateResponse
import com.example.posyandu.features.register.GetUserResponse
import com.example.posyandu.features.main.profile.GetProfileByIdResponse
import com.example.posyandu.features.main.profile.PutUpdateProfileRequest
import com.example.posyandu.features.main.profile.PutUpdateProfileResponse
import com.example.posyandu.features.register.RegisterUserRequest
import com.example.posyandu.features.register.RegisterUserResponse
import com.example.posyandu.features.penyuluhan.CreateJadwalPenyuluhanRequest
import com.example.posyandu.features.penyuluhan.CreateJadwalPenyuluhanResponse
import com.example.posyandu.features.penyuluhan.GetJadwalPenyuluhanResponse
import com.example.posyandu.features.penyuluhan.IndexJadwalPenyuluhanResponse
import com.example.posyandu.features.penyuluhan.UpdateJadwalPenyuluhanResponse
import com.example.posyandu.features.register.UpdateUserRequest
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
        @Path("id") posyanduId: Int,
        @Header("Authorization") token: String
    ): Call<GetPosyanduResponse>

    @PUT("posyandu/{id}")
    fun updatePosyandu(
        @Path("id") posyanduId: Int,
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

    @GET("jadwal-posyandu/posyandu/{id}")
    fun indexJadwalPosyandu(
        @Path("id") posyanduId: Int,
        @Header("Authorization") token: String
    ): Call<JadwalPosyanduIndexResponse>

    @POST("jadwal-posyandu")
    fun createJadwalPosyandu(
        @Header("Authorization") token: String,
        @Body jadwalPosyandu: CreateJadwalPosyanduResponse
    ): Call<CreateJadwalPosyanduResponse>

    @PUT("jadwal-posyandu/{id}")
    fun updateJadwalPosyandu(
        @Path("id") jadwalPosyanduId: Int,
        @Header("Authorization") token: String,
        @Body jadwalPosyandu: CreateJadwalPosyanduResponse
    ): Call<UpdateJadwalPosyanduResponse>

    @DELETE("jadwal-posyandu/{id}")
    fun deleteJadwalPosyandu(
        @Path("id") jadwalPosyanduId: Int,
        @Header("Authorization") token: String
    ): Call<Void>

    @GET("remaja/posyandu/{id}")
    fun indexRemajaByPosyandu(
        @Path("id") posyanduId: Int,
        @Header("Authorization") token: String
    ): Call<IndexRemajaByPosyanduResponse>

    @POST("remaja")
    fun createRemaja(
        @Header("Authorization") token: String,
        @Body remaja: CreateRemajaRequest
    ): Call<CreateRemajaResponse>

    @GET("remaja/{id}")
    fun getRemaja(
        @Path("id") remajaId: Int,
        @Header("Authorization") token: String
    ): Call<GetRemajaResponse>

    @PUT("remaja/{id}")
    fun updateRemaja(
        @Path("id") remajaId: Int,
        @Header("Authorization") token: String,
        @Body remajaUpdate: UpdateRemajaRequest
    ): Call<UpdateRemajaResponse>

    @POST("pemeriksaan")
    fun createPemeriksaan(
        @Header("Authorization") token: String,
        @Body remajaUpdate: CreatePemeriksaanRequest
    ): Call<CreatePemeriksaanResponse>

    @GET("pemeriksaan/{id}")
    fun getPemeriksaan(
        @Path("id") pemeriksaanId: Int,
        @Header("Authorization") token: String
    ): Call<GetPemeriksaanResponse>

    @GET("pemeriksaan/remaja/{id}")
    fun indexPemeriksaanByRemaja(
        @Path("id") remajaId: Int,
        @Header("Authorization") token: String
    ): Call<IndexPemeriksaanByRemajaResponse>

    @PUT("pemeriksaan/{id}")
    fun updatePemeriksaan(
        @Path("id") pemeriksaanId: Int,
        @Header("Authorization") token: String,
        @Body pemeriksaanUpdate: PemeriksaanUpdateRequest
    ): Call<PemeriksaanUpdateResponse>

    @DELETE("pemeriksaan/{id}")
    fun deletePemeriksaan(
        @Path("id") pemeriksaanId: Int,
        @Header("Authorization") token: String
    ): Call<Void>

    @GET("user/{id}")
    fun getUser(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Call<GetUserResponse>

    @POST("user/register")
    fun registerUser(
        @Header("Authorization") token: String,
        @Body user: RegisterUserRequest
    ): Call<RegisterUserResponse>

    @GET("jadwal-penyuluhan")
    fun indexJadwalPenyuluhan(
        @Header("Authorization") token: String
    ): Call<IndexJadwalPenyuluhanResponse>

    @POST("jadwal-penyuluhan")
    fun createJadwalPenyuluhan(
        @Header("Authorization") token: String,
        @Body jadwalPenyuluhan: CreateJadwalPenyuluhanRequest
    ): Call<CreateJadwalPenyuluhanResponse>

    @GET("jadwal-penyuluhan/{id}")
    fun getJadwalPenyuluhan(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<GetJadwalPenyuluhanResponse>

    @PUT("jadwal-penyuluhan/{id}")
    fun updateJadwalPenyuluhan(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body jadwalPenyuluhan: CreateJadwalPenyuluhanRequest
    ): Call<UpdateJadwalPenyuluhanResponse>

    @PUT("user/{id}")
    fun updateUser(
        @Path("id") userId: Int,
        @Header("Authorization") token: String,
        @Body user: UpdateUserRequest
    ): Call<Void>

    @DELETE("user/{id}")
    fun deleteUser(
        @Path("id") userId: Int,
        @Header("Authorization") token: String
    ): Call<Void>

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

    @DELETE("jadwal-penyuluhan/{id}")
    fun deleteJadwalPenyuluhan(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<Void>

}