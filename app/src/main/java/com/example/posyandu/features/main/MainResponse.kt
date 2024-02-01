package com.example.posyandu.features.main

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class MainResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: MainData,

	@field:SerializedName("status")
	val status: String
)

data class RemajaData(

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

data class MainData(

	@field:SerializedName("jadwal_penyuluhan")
	val jadwalPenyuluhan: List<JadwalPenyuluhanItem>,

	@field:SerializedName("remaja")
	val remaja: RemajaData,

	@field:SerializedName("jadwal_posyandu")
	val jadwalPosyandu: List<JadwalPosyanduItem>
) {
	fun sortedPosyandu(): List<JadwalPosyanduItem> {
		val dateFormat: SimpleDateFormat =
			SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

		val sortedPosyandu: List<JadwalPosyanduItem> = jadwalPosyandu.sortedByDescending {
			dateFormat.parse(it.waktuMulai)
		}

		return sortedPosyandu
	}
}
