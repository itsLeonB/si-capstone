package com.example.posyandu.features.main.posyandu

import com.google.gson.annotations.SerializedName

data class UpdatePosyanduRequest(

	@field:SerializedName("provinsi")
	val provinsi: String,

	@field:SerializedName("rt")
	val rt: Int,

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("rw")
	val rw: Int,

	@field:SerializedName("kecamatan")
	val kecamatan: String,

	@field:SerializedName("kode_pos")
	val kodePos: Int,

	@field:SerializedName("kelurahan")
	val kelurahan: String,

	@field:SerializedName("alamat")
	val alamat: String
)
