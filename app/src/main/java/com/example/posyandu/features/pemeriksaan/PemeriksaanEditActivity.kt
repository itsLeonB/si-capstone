package com.example.posyandu.features.pemeriksaan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.posyandu.databinding.ActivityPemeriksaanEditBinding
import com.example.posyandu.utils.ApiConfig
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.properties.Delegates

class PemeriksaanEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanEditBinding
    private var remajaId by Delegates.notNull<Int>()
    private lateinit var viewModel: PemeriksaanViewModel
    private var isKader: Boolean = false

    companion object {
        private const val TAG = "PemeriksaanEditActivity"
    }

    private fun finishWithMessage(message: String) {
        val intent = Intent()
        intent.putExtra("snackbar_message", message)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanEditBinding.inflate(layoutInflater)
        val view = binding.root
        val pemeriksaanId = intent.getIntExtra("pemeriksaanId", 0)
        val prefs = getSharedPreferences("Preferences", MODE_PRIVATE)
        val token = prefs.getString("token", "no token")
        isKader = prefs.getBoolean("isKader", false)
        viewModel = ViewModelProvider(this)[PemeriksaanViewModel::class.java]

        if (isKader) {
            binding.feSwitch.isEnabled = false
            binding.kuDropdown.isEnabled = false
            binding.ketEdit.isEnabled = false
            binding.btnDel.visibility = View.GONE
        } else {
            binding.btnDel.setOnClickListener {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Apakah anda yakin untuk menghapus pemeriksaan?")
                    .setPositiveButton("Ya") { _, _ ->
                        val client = ApiConfig.getApiService()
                            .deletePemeriksaan(pemeriksaanId, "Bearer $token")

                        client.enqueue(object : Callback<Void> {
                            override fun onResponse(
                                call: Call<Void>,
                                response: Response<Void>
                            ) {
                                if (response.isSuccessful) {
                                    viewModel.indexPemeriksaan(remajaId)
                                    finishWithMessage("Pemeriksaan berhasil dihapus")
                                } else {
                                    Log.e(TAG, "onFailure: ${response.message()}")
                                }
                            }

                            override fun onFailure(
                                call: Call<Void>,
                                t: Throwable
                            ) {
                                Log.e(TAG, "onFailure: ${t.message.toString()}")
                            }
                        })
                    }
                    .setNegativeButton("Tidak") { _, _ ->
                    }
                    .show()
            }
        }

        val client =
            ApiConfig.getApiService().getPemeriksaan(pemeriksaanId, "Bearer $token")

        client.enqueue(object : Callback<GetPemeriksaanResponse> {
            override fun onResponse(
                call: Call<GetPemeriksaanResponse>,
                response: Response<GetPemeriksaanResponse>
            ) {
                if (response.isSuccessful) {
                    val pemeriksaan = response.body()?.data

                    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id"))
                    val outputDateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID"))
                    val outputTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                    if (pemeriksaan != null) {
                        val date = inputFormat.parse(pemeriksaan.waktuPengukuran)
                        val formattedDate = date?.let { outputDateFormat.format(it) }
                        val formattedTime =
                            "Pukul " + date?.let { outputTimeFormat.format(it) } + " WIB"

                        remajaId = pemeriksaan.remaja.id
                        binding.nama.text = pemeriksaan.remaja.user.nama
                        binding.tanggal.text = formattedDate
                        binding.waktu.text = formattedTime
                        binding.bbEdit.setText(pemeriksaan.beratBadan.toString())
                        binding.tbEdit.setText(pemeriksaan.tinggiBadan.toString())
                        binding.tdsEdit.setText(pemeriksaan.sistole.toString())
                        binding.tddEdit.setText(pemeriksaan.diastole.toString())
                        binding.llEdit.setText(pemeriksaan.lingkarLengan.toString())
                        binding.kgdEdit.setText(pemeriksaan.tingkatGlukosa.toString())
                        binding.khbEdit.setText(pemeriksaan.kadarHemoglobin.toString())

                        if (pemeriksaan.pemberianFe) {
                            binding.feSwitch.isChecked = true
                        } else {
                            binding.feSwitch.isChecked = false
                        }

                        binding.kuDropdown.setText(pemeriksaan.kondisiUmum)
                        binding.ketEdit.setText(pemeriksaan.keterangan)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetPemeriksaanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

        binding.btnTambah.setOnClickListener {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            dateFormat.timeZone =
                TimeZone.getTimeZone("Asia/Jakarta") // Adjust the time zone as needed
            val currentDate = Date()

            val pemeriksaan = PemeriksaanUpdateRequest(
                posyanduId = prefs.getInt("posyanduId", 0),
                remajaId = remajaId,
                beratBadan = binding.bbEdit.text.toString().toInt(),
                tinggiBadan = binding.tbEdit.text.toString().toInt(),
                sistole = binding.tdsEdit.text.toString().toInt(),
                diastole = binding.tddEdit.text.toString().toInt(),
                lingkarLengan = binding.llEdit.text.toString().toInt(),
                tingkatGlukosa = binding.kgdEdit.text.toString().toInt(),
                kadarHemoglobin = binding.khbEdit.text.toString().toInt(),
                pemberianFe = binding.feSwitch.isChecked,
                waktuPengukuran = dateFormat.format(currentDate),
                kondisiUmum = binding.kuDropdown.text.toString(),
                keterangan = binding.ketEdit.text.toString()
            )

            val client = ApiConfig.getApiService()
                .updatePemeriksaan(pemeriksaanId, "Bearer $token", pemeriksaan)

            client.enqueue(object : Callback<PemeriksaanUpdateResponse> {
                override fun onResponse(
                    call: Call<PemeriksaanUpdateResponse>,
                    response: Response<PemeriksaanUpdateResponse>
                ) {
                    if (response.isSuccessful) {
                        val pemeriksaanId = response.body()!!.data.id
                        val newIntent = Intent(
                            this@PemeriksaanEditActivity,
                            PemeriksaanViewActivity::class.java
                        )
                        viewModel.indexPemeriksaan(remajaId)
                        newIntent.putExtra("snackbar_message", "Pemeriksaan berhasil disimpan")
                        newIntent.putExtra("pemeriksaanId", pemeriksaanId)
                        setResult(Activity.RESULT_OK, newIntent)
                        startActivity(newIntent)
                        finish()
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<PemeriksaanUpdateResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
        }

        setContentView(view)
    }
}