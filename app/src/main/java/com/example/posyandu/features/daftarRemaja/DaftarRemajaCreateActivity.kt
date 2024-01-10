package com.example.posyandu.features.daftarRemaja

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityDaftarRemajaCreateBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

class DaftarRemajaCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarRemajaCreateBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarRemajaCreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
            val formatter = DateTimeFormatter.ofPattern("dd MMM YY")
            binding.tanggalEdit.setText(dateTime.format(formatter))
        }

        binding.tanggalEdit.setOnClickListener {
            datePicker.show(
                supportFragmentManager,
                datePicker.toString()
            )
        }

        binding.btnSimpan.setOnClickListener {
            val intent = Intent()
            intent.putExtra("snackbar_message", "Remaja berhasil didaftarkan")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}