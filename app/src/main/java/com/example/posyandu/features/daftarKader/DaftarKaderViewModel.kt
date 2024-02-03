package com.example.posyandu.features.daftarKader

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posyandu.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarKaderViewModel(application: Application) : AndroidViewModel(application) {
    private val _listKader = MutableLiveData<List<KaderItem>>()
    val listKader: LiveData<List<KaderItem>> = _listKader
    private val prefs = application.getSharedPreferences("Preferences", Context.MODE_PRIVATE)

    companion object {
        private const val TAG = "DaftarKaderViewModel"
    }

    init {
        refreshData()
    }

    fun refreshData() {
        val token = prefs.getString("token", "no token")
        val posyanduId = prefs.getInt("posyanduId", 0)

        val client = ApiConfig.getApiService().indexKader(token = "Bearer $token")

        client.enqueue(object : Callback<IndexKaderResponse> {
            override fun onResponse(
                call: Call<IndexKaderResponse>,
                response: Response<IndexKaderResponse>
            ) {
                if (response.isSuccessful) {
                    _listKader.value = response.body()?.data?.filter {
                        it.posyandu.id == posyanduId
                    }
                } else {
                    Toast.makeText(getApplication(), response.message(), Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<IndexKaderResponse>, t: Throwable) {
                Toast.makeText(getApplication(), t.message, Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}