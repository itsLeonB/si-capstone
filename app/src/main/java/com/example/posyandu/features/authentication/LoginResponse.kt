package com.example.posyandu.features.authentication

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("expires_at")
	val expiresAt: String,

	@field:SerializedName("token")
	val token: String
)
