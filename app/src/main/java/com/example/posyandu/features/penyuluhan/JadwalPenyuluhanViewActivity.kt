package com.example.posyandu.features.penyuluhan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.R
import com.example.posyandu.databinding.ActivityJadwalPenyuluhanViewBinding
import com.example.posyandu.utils.ApiConfig
import com.example.posyandu.utils.ApiService
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JadwalPenyuluhanViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalPenyuluhanViewBinding
    private lateinit var viewModel: JadwalPenyuluhanViewModel
    private lateinit var prefs: SharedPreferences
    private lateinit var token: String
    private lateinit var apiClient: ApiService
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>

    companion object {
        private const val TAG = "JadwalPenyuluhanViewActivity"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal_penyuluhan_view)

        binding = ActivityJadwalPenyuluhanViewBinding.inflate(layoutInflater)
        val view = binding.root
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        token = prefs.getString("token", "no token")!!
        apiClient = ApiConfig.getApiService()

        val JadwalPenyuluhanid = intent.getIntExtra("JadwalPenyuluhanid", 0)
        populateFields(JadwalPenyuluhanid)

        setContentView(view)

        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    Snackbar.make(view, snackbarMessage!!, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, JadwalPenyuluhanEditActivity::class.java)
            intent.putExtra("JadwalPenyuluhanid",JadwalPenyuluhanid)
            startNewActivity.launch(intent)
        }
    }

    private fun finishWithMessage(message: String) {
        val intent = Intent()
        intent.putExtra("snackbar_message", message)
        setResult(Activity.RESULT_OK, intent)
        finish()
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

                    binding.tanggal.setText(userData.waktuMulai)
                    binding.feedback.setText(userData.feedback)
                    binding.isi.setText(userData.materi)
                    binding.title.setText(userData.title)

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

}