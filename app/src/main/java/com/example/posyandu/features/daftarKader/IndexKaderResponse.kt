package com.example.posyandu.features.daftarKader

import com.google.gson.annotations.SerializedName

data class IndexKaderResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<KaderItem>,

	@field:SerializedName("status")
	val status: String
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

data class Posyandu(

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("alamat")
	val alamat: String
)

data class KaderItem(

	@field:SerializedName("is_kader")
	val isKader: Boolean,

	@field:SerializedName("nama_ibu")
	val namaIbu: String,

	@field:SerializedName("posyandu")
	val posyandu: Posyandu,

	@field:SerializedName("nama_ayah")
	val namaAyah: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("user")
	val user: User
)
