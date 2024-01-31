package com.example.posyandu.features.daftarRemaja

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posyandu.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarRemajaEditViewModel(application: Application): AndroidViewModel(application) {
    private val _remajaData = MutableLiveData<Remaja>()
    val remajaData: LiveData<Remaja> = _remajaData

    companion object {
        private const val TAG = "DaftarRemajaEditViewModel"
    }


}