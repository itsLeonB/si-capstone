package com.example.posyandu.features.pemeriksaan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.posyandu.R
import com.example.posyandu.databinding.ActivityPemeriksaanViewBinding
import com.example.posyandu.utils.ApiConfig
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.properties.Delegates

class PemeriksaanViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanViewBinding
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>
    private lateinit var viewModel: PemeriksaanViewModel
    private var remajaId by Delegates.notNull<Int>()
    private var userId by Delegates.notNull<Int>()
    private var currentUserId by Delegates.notNull<Int>()
    private lateinit var role: String

    companion object {
        private const val TAG = "PemeriksaanViewActivity"
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            viewModel.indexPemeriksaan(userId)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanViewBinding.inflate(layoutInflater)
        val view = binding.root

        val prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")
        role = prefs.getString("role", "")!!
        currentUserId = prefs.getInt("currentUserId", 0)

        // Retrieve the message from the intent
        val message = intent.getStringExtra("snackbar_message")

        // Check if the message is not null and display it using a Snackbar
        message?.let {
            val snackbar =
                Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_SHORT)
            snackbar.show()
        }

        val pemeriksaanId = intent.getIntExtra("pemeriksaanId", 0)

        loadData(pemeriksaanId, token!!)

        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    Snackbar.make(view, snackbarMessage!!, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        setContentView(view)
    }

    private fun loadData(pemeriksaanId: Int, token: String) {
        val client =
            ApiConfig.getApiService().getPemeriksaan(pemeriksaanId, "Bearer $token")

        client.enqueue(object : Callback<GetPemeriksaanResponse> {
            override fun onResponse(
                call: Call<GetPemeriksaanResponse>,
                response: Response<GetPemeriksaanResponse>
            ) {
                if (response.isSuccessful) {
                    val pemeriksaan = response.body()!!.data

                    userId = pemeriksaan.remaja.user.id
                    remajaId = pemeriksaan.remaja.id

                    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id"))
                    val outputDateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
                    val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                    val date = inputFormat.parse(pemeriksaan.waktuPengukuran)
                    val formattedDate = date?.let { outputDateFormat.format(it) }
                    val formattedTime =
                        "Pukul " + date?.let { outputTimeFormat.format(it) } + " WIB"

                    binding.remaja.text = pemeriksaan.remaja.user.nama
                    binding.tanggal.text = formattedDate
                    binding.waktu.text = formattedTime
                    binding.beratBadan.text = "Berat Badan: ${pemeriksaan.beratBadan} kg"
                    binding.tinggiBadan.text = "Tinggi Badan: ${pemeriksaan.tinggiBadan} cm"
                    binding.tekananDarah.text =
                        "Tekanan Darah: ${pemeriksaan.sistole}/${pemeriksaan.diastole} mmHg"
                    binding.lingkarLengan.text = "Lingkar Lengan: ${pemeriksaan.lingkarLengan} cm"
                    binding.kadarGlukosa.text =
                        "Kadar Gula Darah: ${pemeriksaan.tingkatGlukosa} mg/dL"
                    binding.kadarHemoglobin.text =
                        "Kadar Hemoglobin: ${pemeriksaan.kadarHemoglobin} gram/dL"
                    binding.pemberianFe.text =
                        "Pemberian Zat Besi: " + if (pemeriksaan.pemberianFe) "Ya" else "Tidak"
                    binding.kondisiUmum.text = "Kondisi Umum: ${pemeriksaan.kondisiUmum}"
                    binding.keterangan.text = pemeriksaan.keterangan

                    if (pemeriksaan.berisiko()) {
                        binding.textRisiko.text = "Berisiko stunting"
                        binding.textRisiko.setTextColor(getColor(R.color.md_theme_light_error))
                        binding.drawRisiko.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@PemeriksaanViewActivity,
                                R.drawable.error_cross
                            )
                        )
                    }

                    when (role) {
                        "remaja" -> {
                            binding.btnEdit.visibility = View.GONE
                        }
                        "kader" -> {
                            if (currentUserId == userId) {
                                binding.btnEdit.visibility = View.GONE
                            } else {
                                binding.btnEdit.setOnClickListener {
                                    val intent = Intent(this@PemeriksaanViewActivity, PemeriksaanEditActivity::class.java)
                                    intent.putExtra("pemeriksaanId", pemeriksaanId)
                                    startNewActivity.launch(intent)
                                    finish()
                                }
                            }
                        }
                        "bidan" -> {
                            binding.btnEdit.setOnClickListener {
                                val intent = Intent(this@PemeriksaanViewActivity, PemeriksaanEditActivity::class.java)
                                intent.putExtra("pemeriksaanId", pemeriksaanId)
                                startNewActivity.launch(intent)
                                finish()
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetPemeriksaanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}