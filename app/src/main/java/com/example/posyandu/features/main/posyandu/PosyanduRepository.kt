package com.example.posyandu

import com.example.posyandu.features.main.posyandu.Posyandu

class PosyanduRepository {
    fun getPosyanduData(): Posyandu {
        return Posyandu(
            1,
            "Posyandu Terpadu",
            "Jalan Sidosermo 1 no 24",
            "https://upload.wikimedia.org/wikipedia/commons/6/65/Posyandu_kampung_bungaraya.jpg",
            "2024-01-10 10:00:00",
            "2024-01-11 11:00:00",
            24,
            3,
            4
        )
    }
}