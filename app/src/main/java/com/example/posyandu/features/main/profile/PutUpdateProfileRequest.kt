package com.example.posyandu.features.main.profile

import com.google.gson.annotations.SerializedName

data class PutUpdateProfileRequest(

	@field:SerializedName("provinsi")
	val provinsi: String,

	@field:SerializedName("rt")
	val rt: Int,

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("rw")
	val rw: Int,

	@field:SerializedName("kode_pos")
	val kodePos: Int,

	@field:SerializedName("telepon")
	val telepon: String,

	@field:SerializedName("kelurahan")
	val kelurahan: String,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("kecamatan")
	val kecamatan: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
