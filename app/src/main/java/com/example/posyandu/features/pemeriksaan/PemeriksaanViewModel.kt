package com.example.posyandu.features.pemeriksaan

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

class PemeriksaanViewModel(application: Application) : AndroidViewModel(application) {
    private val _listPemeriksaan = MutableLiveData<List<Pemeriksaan>>()
    val listPemeriksaan: LiveData<List<Pemeriksaan>> = _listPemeriksaan

    companion object {
        private const val TAG = "PemeriksaanViewModel"
    }

    fun indexPemeriksaan(remajaId: Int) {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().indexPemeriksaanByRemaja(remajaId, "Bearer $token")

        client.enqueue(object : Callback<IndexPemeriksaanByRemajaResponse> {
            override fun onResponse(
                call: Call<IndexPemeriksaanByRemajaResponse>,
                response: Response<IndexPemeriksaanByRemajaResponse>
            ) {
                if (response.isSuccessful) {
                    _listPemeriksaan.value = response.body()?.data
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<IndexPemeriksaanByRemajaResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}