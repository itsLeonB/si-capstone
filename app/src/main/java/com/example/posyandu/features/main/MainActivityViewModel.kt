package com.example.posyandu.features.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posyandu.features.daftarRemaja.IndexRemajaByPosyanduResponse
import com.example.posyandu.features.daftarRemaja.RemajaDataItem
import com.example.posyandu.features.pemeriksaan.IndexPemeriksaanByRemajaResponse
import com.example.posyandu.features.pemeriksaan.Pemeriksaan
import com.example.posyandu.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val _mainBidanData = MutableLiveData<MainBidanData>()
    val mainBidanData: LiveData<MainBidanData> = _mainBidanData

    private val _mainRemajaData = MutableLiveData<List<RemajaDataItem>>()
    val mainRemajaData: LiveData<List<RemajaDataItem>> = _mainRemajaData

    private val _mainData = MutableLiveData<MainData>()
    val mainData: LiveData<MainData> = _mainData

    private val _mainPemeriksaanData = MutableLiveData<List<Pemeriksaan>>()
    val mainPemeriksaanData: LiveData<List<Pemeriksaan>> = _mainPemeriksaanData

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
        val role = prefs.getString("role", "no role")
        val isKader = prefs.getBoolean("isKader", false)

        if (role == "bidan") {
            val client =
                ApiConfig.getApiService().loadMainBidan(token = "Bearer $token")

            client.enqueue(object : Callback<MainBidanResponse> {
                override fun onResponse(
                    call: Call<MainBidanResponse>,
                    response: Response<MainBidanResponse>
                ) {
                    if (response.isSuccessful) {
                        _mainBidanData.value = response.body()?.data
                        val posyanduId = _mainBidanData.value!!.posyandu.id

                        val edit = prefs.edit()
                        edit.putInt("posyanduId", posyanduId)
                        edit.putInt("bidanId", _mainBidanData.value!!.bidan.id)
                        edit.apply()

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
        } else {
            val client =
                ApiConfig.getApiService().loadMain(token = "Bearer $token")

            client.enqueue(object : Callback<MainResponse> {
                override fun onResponse(
                    call: Call<MainResponse>,
                    response: Response<MainResponse>
                ) {
                    if (response.isSuccessful) {
                        _mainData.value = response.body()?.data
                        val posyanduId = _mainData.value?.remaja?.posyandu?.id

                        val edit = prefs.edit()
                        if (posyanduId != null) {
                            edit.putInt("posyanduId", posyanduId)
                        }
                        _mainData.value?.remaja?.user?.let { edit.putInt("userId", it.id) }
                        _mainData.value?.remaja?.let { edit.putBoolean("isKader", it.isKader) }
                        edit.apply()

                        val pemeriksaanClient = ApiConfig.getApiService()
                            .indexPemeriksaanByRemaja(prefs.getInt("userId", 0), "Bearer $token")

                        pemeriksaanClient.enqueue(object :
                            Callback<IndexPemeriksaanByRemajaResponse> {
                            override fun onResponse(
                                call: Call<IndexPemeriksaanByRemajaResponse>,
                                response: Response<IndexPemeriksaanByRemajaResponse>
                            ) {
                                if (response.isSuccessful) {
                                    _mainPemeriksaanData.value =
                                        response.body()?.sortedPemeriksaan()

                                    val remajaClient =
                                        posyanduId?.let {
                                            ApiConfig.getApiService()
                                                .indexRemajaByPosyandu(it, token = "Bearer $token")
                                        }

                                    remajaClient?.enqueue(object :
                                        Callback<IndexRemajaByPosyanduResponse> {
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

                            override fun onFailure(
                                call: Call<IndexPemeriksaanByRemajaResponse>,
                                t: Throwable
                            ) {
                                Log.e(TAG, "onFailure: ${t.message.toString()}")
                            }
                        })
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }

        if (isKader) {
            val client =
                ApiConfig.getApiService().loadMain(token = "Bearer $token")

            client.enqueue(object : Callback<MainResponse> {
                override fun onResponse(
                    call: Call<MainResponse>,
                    response: Response<MainResponse>
                ) {
                    if (response.isSuccessful) {
                        _mainData.value = response.body()?.data
                        val posyanduId = _mainData.value?.remaja?.posyandu?.id

                        val edit = prefs.edit()
                        if (posyanduId != null) {
                            edit.putInt("posyanduId", posyanduId)
                        }
                        _mainData.value?.remaja?.user?.let { edit.putInt("userId", it.id) }
                        _mainData.value?.remaja?.let { edit.putBoolean("isKader", it.isKader) }
                        edit.apply()

                        val pemeriksaanClient = ApiConfig.getApiService()
                            .indexPemeriksaanByRemaja(prefs.getInt("userId", 0), "Bearer $token")

                        pemeriksaanClient.enqueue(object :
                            Callback<IndexPemeriksaanByRemajaResponse> {
                            override fun onResponse(
                                call: Call<IndexPemeriksaanByRemajaResponse>,
                                response: Response<IndexPemeriksaanByRemajaResponse>
                            ) {
                                if (response.isSuccessful) {
                                    _mainPemeriksaanData.value =
                                        response.body()?.sortedPemeriksaan()
                                } else {
                                    Log.e(TAG, "onFailure: ${response.message()}")
                                }
                            }

                            override fun onFailure(
                                call: Call<IndexPemeriksaanByRemajaResponse>,
                                t: Throwable
                            ) {
                                Log.e(TAG, "onFailure: ${t.message.toString()}")
                            }
                        })
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }
    }
}