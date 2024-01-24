package com.example.posyandu.features.main.posyandu

import com.google.gson.annotations.SerializedName

data class ListPosyanduResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<PosyanduItem>,

	@field:SerializedName("status")
	val status: String
)

data class Bidan(

	@field:SerializedName("jabatan")
	val jabatan: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("user")
	val user: User
)

data class PosyanduData(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("alamat")
	val alamat: String
)

data class PosyanduItem(

	@field:SerializedName("posyandu")
	val posyandu: PosyanduData,

	@field:SerializedName("active")
	val active: Boolean,

	@field:SerializedName("bidan")
	val bidan: Bidan
)

data class User(

	@field:SerializedName("nik")
	val nik: Long,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String
)
