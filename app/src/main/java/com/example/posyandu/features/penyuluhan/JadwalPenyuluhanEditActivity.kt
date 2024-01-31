package com.example.posyandu.features.penyuluhan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.R
import com.example.posyandu.databinding.ActivityJadwalPenyuluhanEditBinding
import com.example.posyandu.utils.ApiConfig
import com.example.posyandu.utils.ApiService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JadwalPenyuluhanEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalPenyuluhanEditBinding
    private lateinit var viewModel: JadwalPenyuluhanViewModel
    private lateinit var prefs: SharedPreferences
    private lateinit var token: String
    private lateinit var apiClient: ApiService
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>

    companion object {
        private const val TAG = "JadwalPenyuluhanEditActivity"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal_penyuluhan_edit)

        binding = ActivityJadwalPenyuluhanEditBinding.inflate(layoutInflater)

        val view = binding.root
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        token = prefs.getString("token", "no token")!!
        apiClient = ApiConfig.getApiService()

        val JadwalPenyuluhanid = intent.getIntExtra("JadwalPenyuluhanid", 0)
        populateFields(JadwalPenyuluhanid)

        binding.btnSimpan.setOnClickListener {
            sendUpdate(JadwalPenyuluhanid)
            Snackbar.make(view, "Jadwal berhasil diperbarui", Snackbar.LENGTH_SHORT)
                .show()
        }
        binding.btnDel.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Apakah anda yakin untuk menghapus jadwal penyuluhan?")
                .setPositiveButton("Ya") { _, _ ->
                    deleteData(JadwalPenyuluhanid)
                }
                .setNegativeButton("Tidak") { _, _ ->
                }
                .show()
        }
        setContentView(view)

    }

    private fun sendUpdate(JadwalPenyuluhanid: Int) {
        val jadwalrequest = CreateJadwalPenyuluhanRequest(

            waktuMulai = binding.jamMulaiEdit.text.toString(),
            waktuSelesai = binding.jamSelesaiEdit.text.toString(),
            feedback = binding.feedbackPenyuluhanEdit.text.toString(),
            materi = binding.materiPenyuluhanEdit.text.toString(),
            posyanduId = prefs.getInt("posyanduid",0),
            title = binding.judulPenyuluhanEdit.text.toString()
        )

        val client =
            apiClient.updateJadwalPenyuluhan(JadwalPenyuluhanid, "Bearer $token", jadwalrequest)

        client.enqueue(object : Callback<UpdateJadwalPenyuluhanResponse> {
            override fun onResponse(
                call: Call<UpdateJadwalPenyuluhanResponse>,
                response: Response<UpdateJadwalPenyuluhanResponse>
            ) {
                if (response.isSuccessful) {
                    finishWithMessage("Jadwal penyuluhan berhasil diubah")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    finishWithMessage("Error saat mengirim data")
                }
            }

            override fun onFailure(call: Call<UpdateJadwalPenyuluhanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                finishWithMessage("Error saat mengirim data")
            }
        })
    }

    private fun populateFields(JadwalPenyuluhanid: Int) {
        val client = apiClient.getJadwalPenyuluhan(JadwalPenyuluhanid, "Bearer $token")

        client.enqueue(object : Callback<GetJadwalPenyuluhanResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GetJadwalPenyuluhanResponse>,
                response: Response<GetJadwalPenyuluhanResponse>
            ) {
                if (response.isSuccessful) {
                    val userData = response.body()!!.data

                    binding.tanggalEdit.setText(userData.toString())
                    binding.jamMulaiEdit.setText(userData.waktuMulai)
                    binding.jamSelesaiEdit.setText(userData.waktuSelesai)
                    binding.feedbackPenyuluhanEdit.setText(userData.feedback)
                    binding.materiPenyuluhanEdit.setText(userData.materi)
                    binding.judulPenyuluhanEdit.setText(userData.title)

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    finishWithMessage("Error saat mengambil data jadwal penyuluhan")
                }
            }

            override fun onFailure(call: Call<GetJadwalPenyuluhanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                finishWithMessage("Error saat mengambil data jadwal penyuluhan")
            }
        })

    }

    private fun finishWithMessage(message: String) {
        val intent = Intent()
        intent.putExtra("snackbar_message", message)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun deleteData(JadwalPenyuluhanid: Int) {
        val client = apiClient.deleteJadwalPenyuluhan(JadwalPenyuluhanid, "Bearer $token")

        client.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    finishWithMessage("Jadwal penyuluhan berhasil dihapus")
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
}





