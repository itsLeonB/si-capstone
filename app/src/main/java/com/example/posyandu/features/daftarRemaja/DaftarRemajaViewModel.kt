package com.example.posyandu.features.daftarRemaja

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posyandu.features.register.RegisterUserRequest
import com.example.posyandu.features.register.RegisterUserResponse
import com.example.posyandu.utils.ApiConfig
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarRemajaViewModel(application: Application) : AndroidViewModel(application) {
    private val _listRemaja = MutableLiveData<List<RemajaDataItem>>()
    val listRemaja: LiveData<List<RemajaDataItem>> = _listRemaja
    private val prefs = application.getSharedPreferences("Preferences", Context.MODE_PRIVATE)
    private val _registrationResult = MutableLiveData<Boolean>()
    val registrationResult: LiveData<Boolean>
        get() = _registrationResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

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

    fun createRemaja(user: RegisterUserRequest, view: View) {
        val token = prefs.getString("token", "no token")

        val client =
            ApiConfig.getApiService().registerUser(
                token = "Bearer $token",
                user
            )

        client.enqueue(object : Callback<RegisterUserResponse> {
            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                if (response.isSuccessful) {
                    val userId = response.body()!!.data.id

                    val remaja = CreateRemajaRequest(
                        userId,
                        "Nama Ibu anda",
                        "Nama Ayah anda",
                        prefs.getInt("posyanduId", 0)
                    )

                    val remajaClient =
                        ApiConfig.getApiService().createRemaja(
                            token = "Bearer $token",
                            remaja
                        )

                    remajaClient.enqueue(object : Callback<CreateRemajaResponse> {
                        override fun onResponse(
                            call: Call<CreateRemajaResponse>,
                            response: Response<CreateRemajaResponse>
                        ) {
                            if (response.isSuccessful) {
                                // If the response is successful, update LiveData
                                _registrationResult.postValue(true)
                            } else {
                                // If the response is not successful, update LiveData with an error message
                                _errorMessage.postValue("Remaja gagal didaftarkan, periksa kembali data")
                            }
                        }

                        override fun onFailure(call: Call<CreateRemajaResponse>, t: Throwable) {
                            // Handle failure, update LiveData with an error message
                            _errorMessage.postValue("Failed to register Remaja: ${t.message.toString()}")
                        }
                    })
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Snackbar.make(
                        view,
                        "Remaja gagal didaftarkan, periksa kembali data",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}