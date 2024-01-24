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

class DaftarRemajaViewModel(application: Application) : AndroidViewModel(application) {
    private val _listRemaja = MutableLiveData<List<RemajaDataItem>>()
    val listRemaja: LiveData<List<RemajaDataItem>> = _listRemaja

    companion object {
        private const val TAG = "DaftarRemajaViewModel"
    }

    init {
        indexRemaja()
    }

    fun indexRemaja() {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")
        val posyanduId = prefs.getInt("posyanduId", 0)

        val client =
            ApiConfig.getApiService().indexRemajaByPosyandu(posyanduId, token = "Bearer $token")

        client.enqueue(object : Callback<IndexRemajaByPosyanduResponse> {
            override fun onResponse(
                call: Call<IndexRemajaByPosyanduResponse>,
                response: Response<IndexRemajaByPosyanduResponse>
            ) {
                if (response.isSuccessful) {
                    _listRemaja.value = response.body()?.data
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<IndexRemajaByPosyanduResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}