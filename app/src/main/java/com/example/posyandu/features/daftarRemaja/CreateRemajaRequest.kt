package com.example.posyandu.features.daftarRemaja

import com.google.gson.annotations.SerializedName

data class CreateRemajaRequest(

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("nama_ibu")
	val namaIbu: String,

	@field:SerializedName("nama_ayah")
	val namaAyah: String,

	@field:SerializedName("posyandu_id")
	val posyanduId: Int
)
