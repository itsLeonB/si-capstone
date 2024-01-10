package com.example.posyandu

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}
