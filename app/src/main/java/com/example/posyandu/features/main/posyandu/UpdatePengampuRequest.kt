package com.example.posyandu.features.main.posyandu

import com.google.gson.annotations.SerializedName

data class UpdatePengampuRequest(

	@field:SerializedName("bidan_id")
	val bidanId: Int,

	@field:SerializedName("active")
	val active: Boolean,

	@field:SerializedName("posyandu_id")
	val posyanduId: Int
)
