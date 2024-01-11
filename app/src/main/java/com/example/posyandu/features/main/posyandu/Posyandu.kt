package com.example.posyandu.features.main.posyandu

data class Posyandu(
    val id: Int,
    val nama: String,
    val alamat: String,
    val foto: String,
    val jadwalPosyanduTerdekat: String? = null,
    val jadwalPenyuluhanTerdekat: String? = null,
    val jumlahRemaja: Int,
    val jumlahKader: Int,
    val jumlahBerisiko: Int,
    var tanggalPosyandu: String? = null,
    var jamPosyandu: String? = null,
    var kapanPosyandu: String? = null
)