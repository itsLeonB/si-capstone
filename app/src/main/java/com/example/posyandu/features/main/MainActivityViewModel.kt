package com.example.posyandu.features.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posyandu.features.daftarKader.IndexKaderResponse
import com.example.posyandu.features.daftarKader.KaderItem
import com.example.posyandu.features.daftarRemaja.IndexRemajaByPosyanduResponse
import com.example.posyandu.features.daftarRemaja.RemajaDataItem
import com.example.posyandu.features.pemeriksaan.IndexPemeriksaanByRemajaResponse
import com.example.posyandu.features.pemeriksaan.Pemeriksaan
import com.example.posyandu.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var prefs: SharedPreferences
    private lateinit var token: String
    private lateinit var role: String
    private var posyanduId: Int = 0

    private val _mainBidanData = MutableLiveData<MainBidanData>()
    val mainBidanData: LiveData<MainBidanData> = _mainBidanData

    private val _mainRemajaData = MutableLiveData<List<RemajaDataItem>>()
    val mainRemajaData: LiveData<List<RemajaDataItem>> = _mainRemajaData

    private val _mainData = MutableLiveData<MainData>()
    val mainData: LiveData<MainData> = _mainData

    private val _mainPemeriksaanData = MutableLiveData<List<Pemeriksaan>>()
    val mainPemeriksaanData: LiveData<List<Pemeriksaan>> = _mainPemeriksaanData

    private val _mainKaderData = MutableLiveData<List<KaderItem>>()
    val mainKaderData: LiveData<List<KaderItem>> = _mainKaderData

    companion object {
        private const val TAG = "MainActivityViewModel"
    }

    init {
        refreshData()
    }

    private fun getRoleToken() {
        prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        token = prefs.getString("token", "no token").toString()
        role = prefs.getString("role", "no role").toString()
    }

    private fun loadMainBidan() {
        val call = ApiConfig.getApiService().loadMainBidan(token = "Bearer $token")

        call.enqueue(object : Callback<MainBidanResponse> {
            override fun onResponse(
                call: Call<MainBidanResponse>, response: Response<MainBidanResponse>
            ) {
                if (response.isSuccessful) {
                    _mainBidanData.value = response.body()?.data
                    posyanduId = _mainBidanData.value!!.posyandu.id

                    val edit = prefs.edit()
                    edit.putInt("posyanduId", posyanduId)
                    edit.putInt("bidanId", _mainBidanData.value!!.bidan.id)
                    edit.apply()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MainBidanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun loadRemajaAsBidan() {
        val posyanduId = prefs.getInt("posyanduId", 0)

        val call =
            ApiConfig.getApiService().indexRemajaByPosyandu(posyanduId, token = "Bearer $token")

        call.enqueue(object : Callback<IndexRemajaByPosyanduResponse> {
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
                call: Call<IndexRemajaByPosyanduResponse>, t: Throwable
            ) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun loadMain() {
        val client = ApiConfig.getApiService().loadMain(token = "Bearer $token")

        client.enqueue(object : Callback<MainResponse> {
            override fun onResponse(
                call: Call<MainResponse>, response: Response<MainResponse>
            ) {
                if (response.isSuccessful) {
                    _mainData.value = response.body()?.data
                    posyanduId = _mainData.value?.remaja?.posyandu?.id!!
                    val currentUserId = _mainData.value?.remaja?.user?.id
                    val currentRemajaId = _mainData.value?.remaja?.id

                    val edit = prefs.edit()
                    edit.putInt("posyanduId", posyanduId)
                    currentUserId?.let { edit.putInt("currentUserId", it) }
                    _mainData.value?.remaja?.user?.let { edit.putInt("userId", it.id) }
                    _mainData.value?.remaja?.let { edit.putBoolean("isKader", it.isKader) }
                    currentRemajaId?.let { edit.putInt("currentRemajaId", it) }
                    edit.apply()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun loadSelfPemeriksaan() {
        val call = ApiConfig.getApiService()
            .indexPemeriksaanByRemaja(prefs.getInt("userId", 0), "Bearer $token")

        call.enqueue(object : Callback<IndexPemeriksaanByRemajaResponse> {
            override fun onResponse(
                call: Call<IndexPemeriksaanByRemajaResponse>,
                response: Response<IndexPemeriksaanByRemajaResponse>
            ) {
                if (response.isSuccessful) {
                    _mainPemeriksaanData.value = response.body()?.sortedPemeriksaan()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<IndexPemeriksaanByRemajaResponse>, t: Throwable
            ) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun loadRemajaAsKader() {
        val call = ApiConfig.getApiService().indexRemajaByPosyandu(posyanduId, "Bearer $token")

        call.enqueue(object : Callback<IndexRemajaByPosyanduResponse> {
            override fun onResponse(
                call: Call<IndexRemajaByPosyanduResponse>,
                response: Response<IndexRemajaByPosyanduResponse>
            ) {
                if (response.isSuccessful) {
                    _mainRemajaData.value = response.body()?.data?.filter {
                        it.posyandu.id == posyanduId
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<IndexRemajaByPosyanduResponse>, t: Throwable
            ) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun loadKader() {
        val call = ApiConfig.getApiService().indexKader("Bearer $token")

        call.enqueue(object : Callback<IndexKaderResponse> {
            override fun onResponse(
                call: Call<IndexKaderResponse>,
                response: Response<IndexKaderResponse>
            ) {
                if (response.isSuccessful) {
                    _mainKaderData.value = response.body()?.data
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

    fun refreshData() {

        getRoleToken()

        when (role) {
            "bidan" -> {
                loadMainBidan()
                loadRemajaAsBidan()
            }

            "remaja" -> {
                loadMain()
                loadSelfPemeriksaan()
                loadKader()
            }

            "kader" -> {
                loadMain()
                loadSelfPemeriksaan()
                loadKader()
                loadRemajaAsKader()
            }
        }
    }
}
