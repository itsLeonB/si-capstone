package com.example.posyandu.features.penyuluhan

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posyandu.features.jadwalPosyandu.CreateJadwalPosyanduResponse
import com.example.posyandu.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JadwalPenyuluhanViewModel(application: Application) : AndroidViewModel(application) {
    private val _listJadwalPenyuluhan = MutableLiveData<List<JadwalPenyuluhan>>()
    val listJadwalPenyuluhan: LiveData<List<JadwalPenyuluhan>> = _listJadwalPenyuluhan

    companion object {
        private const val TAG = "JadwalPenyuluhanViewModel"
    }

    init {
        indexJadwal()
    }

    fun indexJadwal() {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().indexJadwalPenyuluhan(token = "Bearer $token")

        client.enqueue(object : Callback<IndexJadwalPenyuluhanResponse> {
            override fun onResponse(
                call: Call<IndexJadwalPenyuluhanResponse>,
                response: Response<IndexJadwalPenyuluhanResponse>
            ) {
                if (response.isSuccessful) {
                    _listJadwalPenyuluhan.value = response.body()?.data
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<IndexJadwalPenyuluhanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun createJadwal(jadwalPenyuluhan: CreateJadwalPenyuluhanRequest) {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().createJadwalPenyuluhan(
                token = "Bearer $token",
                jadwalPenyuluhan
            )

        client.enqueue(object : Callback<CreateJadwalPenyuluhanResponse> {
            override fun onResponse(
                call: Call<CreateJadwalPenyuluhanResponse>,
                response: Response<CreateJadwalPenyuluhanResponse>
            ) {
                if (response.isSuccessful) {
                    indexJadwal()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CreateJadwalPenyuluhanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun updateJadwal(id: Int, jadwalPenyuluhan: CreateJadwalPenyuluhanRequest) {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().updateJadwalPenyuluhan(
                id,
                token = "Bearer $token",
                jadwalPenyuluhan
            )

        client.enqueue(object : Callback<UpdateJadwalPenyuluhanResponse> {
            override fun onResponse(
                call: Call<UpdateJadwalPenyuluhanResponse>,
                response: Response<UpdateJadwalPenyuluhanResponse>
            ) {
                if (response.isSuccessful) {
                    indexJadwal()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdateJadwalPenyuluhanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun deleteJadwal(id: Int) {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().deleteJadwalPenyuluhan(
                id,
                token = "Bearer $token",
            )

        client.enqueue(object : Callback<Void> {
            override fun onResponse(
                call: Call<Void>,
                response: Response<Void>
            ) {
                if (response.isSuccessful) {
                    indexJadwal()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}