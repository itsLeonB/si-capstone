package com.example.posyandu.features.daftarRemaja

import com.google.gson.annotations.SerializedName

data class IndexRemajaAsKaderResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<RemajaDataItem>,

	@field:SerializedName("status")
	val status: String
)
