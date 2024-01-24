package com.example.posyandu.features.daftarRemaja

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.posyandu.databinding.ActivityDaftarRemajaCreateBinding
import com.example.posyandu.features.register.RegisterUserRequest
import com.example.posyandu.features.register.RegisterUserResponse
import com.example.posyandu.utils.ApiConfig
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

class DaftarRemajaCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarRemajaCreateBinding
    private lateinit var viewModel: DaftarRemajaViewModel
    private var userId: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarRemajaCreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[DaftarRemajaViewModel::class.java]

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTitleText("Pilih tanggal lahir")
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val dateTime = LocalDateTime.ofInstant(
                datePicker.selection?.let { it1 ->
                    Instant.ofEpochMilli(it1)
                },
                TimeZone.getDefault().toZoneId()
            )
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            binding.tanggalEdit.setText(dateTime.format(formatter))
        }

        binding.tanggalEdit.setOnClickListener {
            datePicker.show(
                supportFragmentManager,
                datePicker.toString()
            )
        }

        binding.btnSimpan.setOnClickListener {
            val user = RegisterUserRequest(
                nama = binding.namaEdit.text.toString(),
                email = binding.emailEdit.text.toString(),
                username = binding.emailEdit.text.toString(),
                password = binding.passwordEdit.text.toString(),
                nik = binding.nikEdit.text.toString().toLong(),
                tempatLahir = binding.kotaEdit.text.toString(),
                tanggalLahir = binding.tanggalEdit.text.toString() + "T20:00:00+07:00",
                alamat = "Silakan atur alamat anda",
                provinsi = "Silakan pilih provinsi anda",
                kota = "Silakan pilih kota anda",
                kecamatan = "Silakan pilih kecamatan anda",
                kelurahan = "Silakan pilih kelurahan anda",
                kodePos = 10000,
                rt = 1,
                rw = 1,
                telepon = binding.teleponEdit.text.toString(),
                foto = "/storage/image/default.png",
                role = "remaja"
            )

            val prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
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
                        userId = response.body()!!.data.id

                        val remaja = CreateRemajaRequest(
                            userId,
                            "Nama Ibu anda",
                            "Nama Ayah anda",
                            1
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
                                    viewModel.indexRemaja()
                                    val intent = Intent()
                                    intent.putExtra(
                                        "snackbar_message",
                                        "Remaja berhasil didaftarkan"
                                    )
                                    setResult(Activity.RESULT_OK, intent)
                                    finish()
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

                            override fun onFailure(call: Call<CreateRemajaResponse>, t: Throwable) {
                                Log.e(TAG, "onFailure: ${t.message.toString()}")
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

    companion object {
        private const val TAG = "DaftarRemajaCreateActivity"
    }
}