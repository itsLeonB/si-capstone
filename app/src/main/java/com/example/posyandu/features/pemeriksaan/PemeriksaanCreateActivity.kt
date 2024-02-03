package com.example.posyandu.features.pemeriksaan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityPemeriksaanCreateBinding
import com.example.posyandu.databinding.ActivityPemeriksaanEditBinding
import com.example.posyandu.features.daftarRemaja.RemajaIdNama
import com.example.posyandu.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class PemeriksaanCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanCreateBinding
    private lateinit var editBinding: ActivityPemeriksaanEditBinding

    companion object {
        private const val TAG = "PemeriksaanCreateActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val remajaIdNamaList =
            intent.getSerializableExtra("remajaIdNamaList") as ArrayList<RemajaIdNama>?
        val prefs = getSharedPreferences("Preferences", MODE_PRIVATE)
        val token = prefs.getString("token", "no token")
        val role = prefs.getString("role", "no role")

        if (remajaIdNamaList != null) {
            binding = ActivityPemeriksaanCreateBinding.inflate(layoutInflater)
            val view = binding.root

            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                remajaIdNamaList.map { it.nama }
            )

            binding.remajaList.setAdapter(adapter)

            if (role == "kader") {
                binding.feSwitch.visibility = View.GONE
                binding.kuInput.visibility = View.GONE
                binding.kondisiUmum.visibility = View.GONE
                binding.ketInput.visibility = View.GONE
                binding.ketEdit.visibility = View.GONE
            }

            binding.btnTambah.setOnClickListener {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                dateFormat.timeZone =
                    TimeZone.getTimeZone("Asia/Jakarta") // Adjust the time zone as needed
                val currentDate = Date()

                val foundRemaja =
                    remajaIdNamaList.find { it.nama == binding.remajaList.text.toString() }

                val pemeriksaanData = CreatePemeriksaanRequest(
                    posyanduId = prefs.getInt("posyanduId", 0),
                    remajaId = foundRemaja!!.id,
                    beratBadan = binding.bbEdit.text.toString().toInt(),
                    tinggiBadan = binding.tbEdit.text.toString().toInt(),
                    sistole = binding.tdsEdit.text.toString().toInt(),
                    diastole = binding.tddEdit.text.toString().toInt(),
                    lingkarLengan = binding.llEdit.text.toString().toInt(),
                    tingkatGlukosa = binding.kgdEdit.text.toString().toInt(),
                    kadarHemoglobin = binding.khbEdit.text.toString().toInt(),
                    pemberianFe = binding.feSwitch.isChecked,
                    waktuPengukuran = dateFormat.format(currentDate),
                    kondisiUmum = binding.kondisiUmum.text.toString(),
                    keterangan = binding.ketEdit.text.toString()
                )

                val client =
                    ApiConfig.getApiService().createPemeriksaan("Bearer $token", pemeriksaanData)

                client.enqueue(object : Callback<CreatePemeriksaanResponse> {
                    override fun onResponse(
                        call: Call<CreatePemeriksaanResponse>,
                        response: Response<CreatePemeriksaanResponse>
                    ) {
                        if (response.isSuccessful) {
                            val pemeriksaanId = response.body()!!.data.id
                            val newIntent = Intent(
                                this@PemeriksaanCreateActivity,
                                PemeriksaanViewActivity::class.java
                            )
                            newIntent.putExtra("snackbar_message", "Pemeriksaan berhasil disimpan")
                            newIntent.putExtra("pemeriksaanId", pemeriksaanId)
                            setResult(Activity.RESULT_OK, newIntent)
                            startActivity(newIntent)
                            finish()
                        } else {
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<CreatePemeriksaanResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                    }
                })
            }

            setContentView(view)
        } else {
            editBinding = ActivityPemeriksaanEditBinding.inflate(layoutInflater)
            val view = editBinding.root

            editBinding.title.text = "Data Pemeriksaan Baru"
            editBinding.nama.text = intent.getStringExtra("nama")
            editBinding.tanggal.visibility = View.GONE
            editBinding.waktu.visibility = View.GONE
            editBinding.btnDel.visibility = View.GONE

            if (role == "kader") {
                editBinding.feSwitch.visibility = View.GONE
                editBinding.kuInput.visibility = View.GONE
                editBinding.kuDropdown.visibility = View.GONE
                editBinding.ketInput.visibility = View.GONE
                editBinding.ketEdit.visibility = View.GONE
            }

            editBinding.btnTambah.setOnClickListener {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                dateFormat.timeZone =
                    TimeZone.getTimeZone("Asia/Jakarta") // Adjust the time zone as needed
                val currentDate = Date()

                val pemeriksaanData = CreatePemeriksaanRequest(
                    posyanduId = prefs.getInt("posyanduId", 0),
                    remajaId = intent.getIntExtra("remajaId", 0),
                    beratBadan = editBinding.bbEdit.text.toString().toInt(),
                    tinggiBadan = editBinding.tbEdit.text.toString().toInt(),
                    sistole = editBinding.tdsEdit.text.toString().toInt(),
                    diastole = editBinding.tddEdit.text.toString().toInt(),
                    lingkarLengan = editBinding.llEdit.text.toString().toInt(),
                    tingkatGlukosa = editBinding.kgdEdit.text.toString().toInt(),
                    kadarHemoglobin = editBinding.khbEdit.text.toString().toInt(),
                    pemberianFe = editBinding.feSwitch.isChecked,
                    waktuPengukuran = dateFormat.format(currentDate),
                    kondisiUmum = editBinding.kuDropdown.text.toString(),
                    keterangan = editBinding.ketEdit.text.toString()
                )

                val client =
                    ApiConfig.getApiService().createPemeriksaan("Bearer $token", pemeriksaanData)

                client.enqueue(object : Callback<CreatePemeriksaanResponse> {
                    override fun onResponse(
                        call: Call<CreatePemeriksaanResponse>,
                        response: Response<CreatePemeriksaanResponse>
                    ) {
                        if (response.isSuccessful) {
                            val pemeriksaanId = response.body()!!.data.id
                            val newIntent = Intent(
                                this@PemeriksaanCreateActivity,
                                PemeriksaanViewActivity::class.java
                            )
                            newIntent.putExtra("snackbar_message", "Pemeriksaan berhasil disimpan")
                            newIntent.putExtra("pemeriksaanId", pemeriksaanId)
                            setResult(Activity.RESULT_OK, newIntent)
                            startActivity(newIntent)
                            finish()
                        } else {
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<CreatePemeriksaanResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                    }
                })
            }

            setContentView(view)
        }
    }
}