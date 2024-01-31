package com.example.posyandu.features.jadwalPosyandu

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

class JadwalPosyanduViewModel(application: Application) : AndroidViewModel(application) {
    private val _listJadwalPosyandu = MutableLiveData<List<JadwalPosyandu>>()
    val listJadwalPosyandu: LiveData<List<JadwalPosyandu>> = _listJadwalPosyandu

    companion object {
        private const val TAG = "JadwalPosyanduViewModel"
    }

    init {
        indexJadwal()
    }

    private fun indexJadwal() {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().indexJadwalPosyandu(
                posyanduId = prefs.getInt("posyanduId", 0),
                token = "Bearer $token"
            )

        client.enqueue(object : Callback<JadwalPosyanduIndexResponse> {
            override fun onResponse(
                call: Call<JadwalPosyanduIndexResponse>,
                response: Response<JadwalPosyanduIndexResponse>
            ) {
                if (response.isSuccessful) {
                    _listJadwalPosyandu.value = response.body()?.sortedData()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<JadwalPosyanduIndexResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun createJadwal(jadwalPosyandu: CreateJadwalPosyanduResponse) {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().createJadwalPosyandu(
                token = "Bearer $token",
                jadwalPosyandu
            )

        client.enqueue(object : Callback<CreateJadwalPosyanduResponse> {
            override fun onResponse(
                call: Call<CreateJadwalPosyanduResponse>,
                response: Response<CreateJadwalPosyanduResponse>
            ) {
                if (response.isSuccessful) {
                    indexJadwal()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CreateJadwalPosyanduResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun updateJadwal(id: Int, jadwalPosyandu: CreateJadwalPosyanduResponse) {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().updateJadwalPosyandu(
                id,
                token = "Bearer $token",
                jadwalPosyandu
            )

        client.enqueue(object : Callback<UpdateJadwalPosyanduResponse> {
            override fun onResponse(
                call: Call<UpdateJadwalPosyanduResponse>,
                response: Response<UpdateJadwalPosyanduResponse>
            ) {
                if (response.isSuccessful) {
                    indexJadwal()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdateJadwalPosyanduResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun deleteJadwal(id: Int) {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().deleteJadwalPosyandu(
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