package com.example.posyandu.features.main.posyandu

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

class PosyanduEditViewModel(application: Application) : AndroidViewModel(application) {
    private val _posyanduData = MutableLiveData<Posyandu>()
    val posyanduData: LiveData<Posyandu> = _posyanduData

    companion object {
        private const val TAG = "PosyanduEditViewModel"
    }

    init {
        loadData()
    }

    private fun loadData() {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val posyanduId = prefs.getInt("posyanduId", 0)

        val client = ApiConfig.getApiService().getPosyandu(posyanduId, token = "Bearer $token")

        client.enqueue(object : Callback<GetPosyanduResponse> {
            override fun onResponse(
                call: Call<GetPosyanduResponse>,
                response: Response<GetPosyanduResponse>
            ) {
                if (response.isSuccessful) {
                    _posyanduData.value = response.body()?.data
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetPosyanduResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        }
        )
    }
}