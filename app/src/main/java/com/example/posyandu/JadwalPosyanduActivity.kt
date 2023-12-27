package com.example.posyandu

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityJadwalPosyanduBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

class JadwalPosyanduActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalPosyanduBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalPosyanduBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnTambah.setOnClickListener {
            showCreateDialog(view)
        }

        binding.cardJadwal1.setOnClickListener {
            showViewDialog(view)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showViewDialog(view: View) {
        val customView =
            LayoutInflater.from(this).inflate(R.layout.fragment_jadwal_posyandu_view, null)

        val viewDialog = MaterialAlertDialogBuilder(this)
            .setView(customView)
            .show()

        val btnTambah: Button = customView.findViewById(R.id.btn_tambah)

        val editJadwal: TextInputEditText = customView.findViewById(R.id.jam_2_edit)

        editJadwal.setOnClickListener {
            viewDialog.cancel()
            showEditDialog(view)
        }

        btnTambah.setOnClickListener {
            viewDialog.cancel()
            showCreateDialog(view)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEditDialog(view: View) {
        val customView =
            LayoutInflater.from(this).inflate(R.layout.fragment_jadwal_posyandu_edit, null)
        val tanggalEdit: TextInputEditText = customView.findViewById(R.id.tanggal_edit)
        val jamMulaiEdit: TextInputEditText = customView.findViewById(R.id.jam_mulai_edit)
        val jamSelesaiEdit: TextInputEditText = customView.findViewById(R.id.jam_selesai_edit)

        val btnTambah: Button = customView.findViewById(R.id.btn_tambah)
        val btnDel: Button = customView.findViewById(R.id.btn_del)

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(today)

        tanggalEdit.setOnClickListener {
            val datePicker = createDatePicker(tanggalEdit, constraintsBuilder, today)
            datePicker.show(supportFragmentManager, datePicker.toString())
        }

        jamMulaiEdit.setOnClickListener {
            val timePicker = createTimePicker(jamMulaiEdit)
            timePicker.show(supportFragmentManager, timePicker.toString())
        }

        jamSelesaiEdit.setOnClickListener {
            val timePicker = createTimePicker(jamSelesaiEdit)
            timePicker.show(supportFragmentManager, timePicker.toString())
        }

        val editDialog = MaterialAlertDialogBuilder(this)
            .setView(customView)
            .show()

        btnTambah.setOnClickListener {
            editDialog.cancel()

            Snackbar.make(view, "Jadwal berhasil diperbarui", Snackbar.LENGTH_SHORT)
                .show()
        }

        btnDel.setOnClickListener {
            editDialog.cancel()

            Snackbar.make(view, "Jadwal berhasil dihapus", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showCreateDialog(view: View) {
        val customView =
            LayoutInflater.from(this).inflate(R.layout.fragment_jadwal_posyandu_create, null)
        val tanggalEdit: TextInputEditText = customView.findViewById(R.id.tanggal_edit)
        val jamMulaiEdit: TextInputEditText = customView.findViewById(R.id.jam_mulai_edit)
        val jamSelesaiEdit: TextInputEditText = customView.findViewById(R.id.jam_selesai_edit)
        val btnTambah: Button = customView.findViewById(R.id.btn_tambah)

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(today)

        tanggalEdit.setOnClickListener {
            val datePicker = createDatePicker(tanggalEdit, constraintsBuilder, today)
            datePicker.show(supportFragmentManager, datePicker.toString())
        }

        jamMulaiEdit.setOnClickListener {
            val timePicker = createTimePicker(jamMulaiEdit)
            timePicker.show(supportFragmentManager, timePicker.toString())
        }

        jamSelesaiEdit.setOnClickListener {
            val timePicker = createTimePicker(jamSelesaiEdit)
            timePicker.show(supportFragmentManager, timePicker.toString())
        }

        val createDialog = MaterialAlertDialogBuilder(this)
            .setView(customView)
            .show()

        btnTambah.setOnClickListener {
            createDialog.cancel()

            Snackbar.make(view, "Jadwal berhasil ditambahkan", Snackbar.LENGTH_SHORT)
                .show()
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
            val formatter = DateTimeFormatter.ofPattern("dd MMM YY")
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