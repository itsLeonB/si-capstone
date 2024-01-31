package com.example.posyandu.features.main.posyandu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.posyandu.databinding.ActivityPosyanduEditBinding
import com.example.posyandu.utils.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PosyanduEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPosyanduEditBinding
    private lateinit var viewModel: PosyanduEditViewModel

    companion object {
        private const val TAG = "PosyanduEditActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPosyanduEditBinding.inflate(layoutInflater)
        val view = binding.root
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[PosyanduEditViewModel::class.java]
        viewModel.posyanduData.observe(this) {
            populateFields(it)
        }

        binding.btnSimpan.setOnClickListener {
            saveData()
        }

        setContentView(view)
    }

    private fun saveData() {
        val prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val posyandu = UpdatePosyanduRequest(
            nama = binding.namaEdit.text.toString(),
            alamat = binding.alamatEdit.text.toString(),
            provinsi = binding.provinsiEdit.text.toString(),
            kota = binding.kotaEdit.text.toString(),
            kecamatan = binding.kecamatanEdit.text.toString(),
            kelurahan = binding.kelurahanEdit.text.toString(),
            kodePos = binding.kodeposEdit.text.toString().toInt(),
            rt = binding.rtEdit.text.toString().toInt(),
            rw = binding.rwEdit.text.toString().toInt(),
            foto = "/storage/image/posyandu.png"
        )

        val client = ApiConfig.getApiService()
            .updatePosyandu(
                viewModel.posyanduData.value!!.id,
                token = "Bearer $token",
                posyandu
            )

        client.enqueue(object : Callback<UpdatePosyanduResponse> {
            override fun onResponse(
                call: Call<UpdatePosyanduResponse>,
                response: Response<UpdatePosyanduResponse>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent()
                    intent.putExtra("snackbar_message", "Posyandu berhasil diperbarui")
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdatePosyanduResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        }
        )
    }

    private fun populateFields(posyandu: Posyandu) {
        binding.namaEdit.setText(posyandu.nama)
        binding.alamatEdit.setText(posyandu.alamat)
        binding.provinsiEdit.setText(posyandu.provinsi)
        binding.kotaEdit.setText(posyandu.kota)
        binding.kecamatanEdit.setText(posyandu.kecamatan)
        binding.kelurahanEdit.setText(posyandu.kelurahan)
        binding.kodeposEdit.setText(posyandu.kodePos.toString())
        binding.rtEdit.setText(posyandu.rt.toString())
        binding.rwEdit.setText(posyandu.rw.toString())
    }
}