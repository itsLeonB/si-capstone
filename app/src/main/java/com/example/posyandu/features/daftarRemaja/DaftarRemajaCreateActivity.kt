package com.example.posyandu.features.daftarRemaja

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.posyandu.databinding.ActivityDaftarRemajaCreateBinding
import com.example.posyandu.features.register.RegisterUserRequest
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
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

        viewModel = ViewModelProvider(this)[DaftarRemajaViewModel::class.java]

        viewModel.registrationResult.observe(this, Observer { success ->
            if (success) {
                // Registration was successful, perform the necessary actions
                val intent = Intent()
                intent.putExtra("snackbar_message", "Remaja berhasil didaftarkan")
                setResult(RESULT_OK, intent)
                finish()
            }
        })

        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            // Show a Snackbar or handle the error message
            Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show()
        })

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

            viewModel.createRemaja(user, view)
        }

        setContentView(view)
    }

    companion object {
        private const val TAG = "DaftarRemajaCreateActivity"
    }
}