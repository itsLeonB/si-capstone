package com.example.posyandu.features.main.profile

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posyandu.features.main.MainBidanData
import com.example.posyandu.features.main.User
import com.example.posyandu.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val _profileData = MutableLiveData<ProfileData>()
    val profileData: LiveData<ProfileData?> = _profileData

    private val _mainBidanData = MutableLiveData<MainBidanData>()
    val MainBidanData: LiveData<MainBidanData> = _mainBidanData

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    init {
        refreshProfileData()
    }

    fun refreshProfileData() {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")
        val id = prefs.getInt("userId", 0)

        Log.d(TAG, "refreshProfileData: Fetching profile for userId $id")

        val client = ApiConfig.getApiService().getProfileById(id, token = "Bearer $token")




        client.enqueue(object : Callback<GetProfileByIdResponse> {
            override fun onResponse(
                call: Call<GetProfileByIdResponse>, response: Response<GetProfileByIdResponse>
            ) {
                if (response.isSuccessful) {
                    _profileData.value = response.body()?.data

                } else {
                    Log.e(ProfileViewModel.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetProfileByIdResponse>, t: Throwable) {
                Log.e(ProfileViewModel.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun updateProfile(profile: PutUpdateProfileRequest) {
        val prefs =
            getApplication<Application>().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")
        val userId = prefs.getInt("userId", 0)

        val client =
            ApiConfig.getApiService().updateProfile(
                userId,
                token = "Bearer $token",
                profile
            )

        client.enqueue(object : Callback<PutUpdateProfileResponse> {
            override fun onResponse(
                call: Call<PutUpdateProfileResponse>,
                response: Response<PutUpdateProfileResponse>
            ) {
                if (response.isSuccessful) {
                    refreshProfileData()
                } else {
                    Log.e(ProfileViewModel.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PutUpdateProfileResponse>, t: Throwable) {
                Log.e(ProfileViewModel.TAG, "onFailure: ${t.message.toString()}")
            }
        })


    }
}