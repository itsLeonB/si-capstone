package com.example.posyandu.features.main.posyandu

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.posyandu.PosyanduRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PosyanduViewModel : ViewModel() {
    private val posyanduRepository = PosyanduRepository()

    private val _posyanduData = MutableLiveData<Posyandu>()
    val posyanduData: LiveData<Posyandu> = _posyanduData

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:", Locale.getDefault())

    fun refreshPosyanduData() {
        val posyandu = posyanduRepository.getPosyanduData()

        val jadwalPosyanduTerdekat = dateFormat.parse(posyandu.jadwalPosyanduTerdekat)
        posyandu.tanggalPosyandu =
            SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID")).format(jadwalPosyanduTerdekat)
        posyandu.jamPosyandu =
            SimpleDateFormat("HH:mm", Locale("id", "ID")).format(jadwalPosyanduTerdekat)

        posyandu.kapanPosyandu =
            DateUtils.getRelativeTimeSpanString(jadwalPosyanduTerdekat.time, Date().time, 0)
                .toString()

        _posyanduData.value = posyandu
    }
}