package com.example.posyandu.features.main.home

data class Bidan(
    val id: Int,
    val nama: String,
    val namaPosyandu: String,
    val alamatPosyandu: String,
    val jumlahRemaja: Int? = null,
    val jumlahKader: Int? = null,
    val jumlahRemajaBeresiko: Int? = null,
    var hariJadwalTerdekat1: String? = null,
    var tanggalJadwalTerdekat1: String? = null,
    var jenisAktivitasTerdekat1: String? = null,
    var iconJenis1: String? = null,
    var hariJadwalTerdekat2: String? = null,
    var tanggalJadwalTerdekat2: String? = null,
    var jenisAktivitasTerdekat2: String? = null,
    var iconJenis2: String? = null,
    var namaSender1: String? = null,
    var kontenChat1: String? = null,
    var namaSender2: String? = null,
    var kontenChat2: String? = null
)
