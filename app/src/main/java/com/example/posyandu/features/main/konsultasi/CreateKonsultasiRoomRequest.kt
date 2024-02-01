package com.example.posyandu.features.main.konsultasi

import com.google.gson.annotations.SerializedName

data class CreateKonsultasiRoomRequest(

	@field:SerializedName("konsultan_id")
	val konsultanId: Int,

	@field:SerializedName("pasien_id")
	val pasienId: Int
)
