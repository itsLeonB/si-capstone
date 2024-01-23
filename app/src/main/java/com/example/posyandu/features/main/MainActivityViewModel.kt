package com.example.posyandu.features.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posyandu.features.daftarRemaja.IndexRemajaByPosyanduResponse
import com.example.posyandu.features.daftarRemaja.RemajaDataItem
import com.example.posyandu.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val _mainBidanData = MutableLiveData<MainBidanData>()
    val mainBidanData: LiveData<MainBidanData> = _mainBidanData

    private val _mainRemajaData = MutableLiveData<List<RemajaDataItem>>()
    val mainRemajaData: LiveData<List<RemajaDataItem>> = _mainRemajaData

    companion object {
        private const val TAG = "MainActivityViewModel"
    }

    init {
        refreshData()
    }

    fun refreshData() {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().loadMainBidan(token = "Bearer $token")

        client.enqueue(object : Callback<MainBidanResponse> {
            override fun onResponse(
                call: Call<MainBidanResponse>,
                response: Response<MainBidanResponse>
            ) {
                if (response.isSuccessful) {
                    _mainBidanData.value = response.body()?.data
                    val edit = prefs.edit()
                    edit.putInt("posyanduId", _mainBidanData.value!!.posyandu.id)
                    edit.apply()

                    val posyanduId = _mainBidanData.value!!.posyandu.id

                    val remajaClient =
                        ApiConfig.getApiService()
                            .indexRemajaByPosyandu(posyanduId, token = "Bearer $token")

                    remajaClient.enqueue(object : Callback<IndexRemajaByPosyanduResponse> {
                        override fun onResponse(
                            call: Call<IndexRemajaByPosyanduResponse>,
                            response: Response<IndexRemajaByPosyanduResponse>
                        ) {
                            if (response.isSuccessful) {
                                _mainRemajaData.value = response.body()?.data
                            } else {
                                Log.e(TAG, "onFailure: ${response.message()}")
                            }
                        }

                        override fun onFailure(
                            call: Call<IndexRemajaByPosyanduResponse>,
                            t: Throwable
                        ) {
                            Log.e(TAG, "onFailure: ${t.message.toString()}")
                        }
                    })
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MainBidanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}