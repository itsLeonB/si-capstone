package com.example.posyandu

import com.example.posyandu.features.authentication.LoginResponse
import com.example.posyandu.features.jadwalPosyandu.CreateJadwalPosyanduResponse
import com.example.posyandu.features.jadwalPosyandu.JadwalPosyanduIndexResponse
import com.example.posyandu.features.jadwalPosyandu.UpdateJadwalPosyanduResponse
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
}
