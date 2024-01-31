package com.example.posyandu.features.daftarRemaja

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityDaftarRemajaEditBinding
import com.example.posyandu.features.register.GetUserResponse
import com.example.posyandu.features.register.UpdateUserRequest
import com.example.posyandu.features.register.User
import com.example.posyandu.utils.ApiConfig
import com.example.posyandu.utils.ApiService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DaftarRemajaEditActivity : AppCompatActivity() {
    private lateinit var userData: User
    private lateinit var binding: ActivityDaftarRemajaEditBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var token: String
    private lateinit var apiClient: ApiService

    companion object {
        private const val TAG = "DaftarRemajaEditActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarRemajaEditBinding.inflate(layoutInflater)
        val view = binding.root
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        token = prefs.getString("token", "no token")!!
        apiClient = ApiConfig.getApiService()

        val userId = intent.getIntExtra("userId", 0)
        populateFields(userId)

        binding.btnSimpan.setOnClickListener {
            sendUpdate(userId)
        }

        binding.btnDel.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Apakah anda yakin untuk menghapus remaja?")
                .setPositiveButton("Ya") { _, _ ->
                    deleteData(userId)
                }
                .setNegativeButton("Tidak") { _, _ ->
                }
                .show()
        }

        setContentView(view)
    }

    private fun deleteData(userId: Int) {
        val client = apiClient.deleteUser(userId, "Bearer $token")

        client.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    finishWithMessage("Data remaja berhasil dihapus")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    finishWithMessage("Terjadi error")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                finishWithMessage("Terjadi error")
            }
        })
    }

    private fun sendUpdate(userId: Int) {
        val userRequest = UpdateUserRequest(
            nama = binding.nama.text.toString(),
            email = binding.emailEdit.text.toString(),
            username = userData.username,
            alamat = binding.alamatEdit.text.toString(),
            provinsi = binding.provinsiEdit.text.toString(),
            kota = binding.kotaEdit.text.toString(),
            kecamatan = binding.kecamatanEdit.text.toString(),
            kelurahan = binding.kelurahanEdit.text.toString(),
            kodePos = binding.kodeposEdit.text.toString().toInt(),
            rt = binding.rtEdit.text.toString().toInt(),
            rw = binding.rwEdit.text.toString().toInt(),
            telepon = binding.teleponEdit.text.toString(),
            foto = userData.foto
        )

        val client = apiClient.updateUser(userId, "Bearer $token", userRequest)

        client.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    finishWithMessage("Data remaja berhasil diubah")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    finishWithMessage("Error saat mengirim data")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                finishWithMessage("Error saat mengirim data")
            }
        })
    }

    private fun populateFields(userId: Int) {
        val client = apiClient.getUser(userId, "Bearer $token")

        client.enqueue(object : Callback<GetUserResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GetUserResponse>,
                response: Response<GetUserResponse>
            ) {
                if (response.isSuccessful) {
                    userData = response.body()!!.data

                    val tanggalLahir = LocalDate.parse(
                        userData.tanggalLahir,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                    val birthDate = tanggalLahir.format(
                        DateTimeFormatter.ofPattern(
                            "dd MMMM yyyy",
                            Locale("id")
                        )
                    )

                    binding.nama.text = userData.nama
                    binding.nik.text = userData.nik.toString()
                    binding.ttl.text = birthDate
                    binding.emailEdit.setText(userData.email)
                    binding.teleponEdit.setText(userData.telepon)
                    binding.alamatEdit.setText(userData.alamat)
                    binding.provinsiEdit.setText(userData.provinsi)
                    binding.kotaEdit.setText(userData.kota)
                    binding.kecamatanEdit.setText(userData.kecamatan)
                    binding.kelurahanEdit.setText(userData.kelurahan)
                    binding.kodeposEdit.setText(userData.kodePos.toString())
                    binding.rtEdit.setText(userData.rt.toString())
                    binding.rwEdit.setText(userData.rw.toString())
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    finishWithMessage("Error saat mengambil data remaja")
                }
            }

            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                finishWithMessage("Error saat mengambil data remaja")
            }
        })
    }

    private fun finishWithMessage(message: String) {
        val intent = Intent()
        intent.putExtra("snackbar_message", message)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}