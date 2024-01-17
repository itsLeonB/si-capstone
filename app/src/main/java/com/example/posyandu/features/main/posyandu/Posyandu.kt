package com.example.posyandu.features.main.posyandu

import com.google.gson.annotations.SerializedName

data class Posyandu(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("nama") var nama: String? = null,
    @SerializedName("alamat") var alamat: String? = null,
    @SerializedName("foto") var foto: String? = null,
    val jadwalPosyanduTerdekat: String? = null,
    val jadwalPenyuluhanTerdekat: String? = null,
    val jumlahRemaja: Int? = null,
    val jumlahKader: Int? = null,
    val jumlahBerisiko: Int? = null,
    var tanggalPosyandu: String? = null,
    var jamPosyandu: String? = null,
    var kapanPosyandu: String? = null
)