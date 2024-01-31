package com.example.posyandu.features.penyuluhan

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.posyandu.databinding.ActivityJadwalPenyuluhanCreateBinding
import com.example.posyandu.features.jadwalPosyandu.CreateJadwalPosyanduResponse
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

class JadwalPenyuluhanCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalPenyuluhanCreateBinding
    private lateinit var viewModel: JadwalPenyuluhanViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalPenyuluhanCreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[JadwalPenyuluhanViewModel::class.java]

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(today)

        binding.tanggalEdit.setOnClickListener {
            val datePicker = createDatePicker(binding.tanggalEdit, constraintsBuilder, today)
            datePicker.show(supportFragmentManager, datePicker.toString())
        }
        binding.jamMulaiEdit.setOnClickListener {
            val timePicker = createTimePicker(binding.jamMulaiEdit)
            timePicker.show(supportFragmentManager, timePicker.toString())
        }
        binding.jamSelesaiEdit.setOnClickListener {
            val timePicker = createTimePicker(binding.jamSelesaiEdit)
            timePicker.show(supportFragmentManager, timePicker.toString())
        }

        binding.btnTambah.setOnClickListener {
            val tanggal = binding.tanggalEdit.text.toString()
            val jamMulai = binding.jamMulaiEdit.text.toString()
            val jamSelesai = binding.jamSelesaiEdit.text.toString()
            val feedback = binding.feedbackPenyuluhanEdit.text.toString()
            val materi = binding.materiPenyuluhanEdit.text.toString()
            val title = binding.judulPenyuluhanEdit.text.toString()

            val jadwalPenyuluhan = CreateJadwalPenyuluhanRequest(
                "${tanggal}T$jamSelesai:00+07:00",
                feedback = feedback,
                materi = materi,
                1,
                title = title ,
                "${tanggal}T$jamMulai:00+07:00"
            )

            viewModel.createJadwal(jadwalPenyuluhan)

            Snackbar.make(view, "Jadwal berhasil ditambahkan", Snackbar.LENGTH_SHORT)
                .show()

            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDatePicker(
        editText: TextInputEditText,
        constraintsBuilder: CalendarConstraints.Builder,
        today: Long,
    ): MaterialDatePicker<Long> {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(today)
                .setTitleText("Pilih tanggal")
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val dateTime = LocalDateTime.ofInstant(
                datePicker.selection?.let { it1 -> Instant.ofEpochMilli(it1) },
                TimeZone.getDefault().toZoneId()
            )
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = dateTime.format(formatter)
            editText.setText(formattedDate)
        }

        return datePicker
    }

    private fun createTimePicker(editText: TextInputEditText): MaterialTimePicker {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Pilih jam")
                .build()

        timePicker.addOnPositiveButtonClickListener {
            // Handle the selected time
            val selectedTime = String.format("%02d:%02d", timePicker.hour, timePicker.minute)

            // Set the selected time to the appropriate EditText
            editText.setText(selectedTime)
        }

        return timePicker
    }
}