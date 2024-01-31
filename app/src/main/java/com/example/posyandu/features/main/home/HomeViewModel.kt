package com.example.posyandu.features.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.posyandu.utils.UserManager
import java.text.SimpleDateFormat
import java.util.Locale


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val homeRepository = HomeRepository()

    private val _bidanData = MutableLiveData<Bidan>()
    private val _kaderData = MutableLiveData<Kader>()
    private val _remajaData = MutableLiveData<Remaja>()
    val bidanData: LiveData<Bidan> = _bidanData
    val kaderData: LiveData<Kader> = _kaderData
    val remajaData: LiveData<Remaja> = _remajaData

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:", Locale.getDefault())


    fun refreshHomeData() {

        val userManager = UserManager.getInstance(getApplication())
        val userRole = userManager.getRole()


        if (userRole == "admin") {
            val home = homeRepository.getHomeDataBidan()

            _bidanData.value = home
        } else if (userRole == "kader") {
            val home = homeRepository.getHomeDataKader()

            _kaderData.value = home
        } else {
            val home = homeRepository.getHomeDataRemaja()

            _remajaData.value = home
        }

    }
}