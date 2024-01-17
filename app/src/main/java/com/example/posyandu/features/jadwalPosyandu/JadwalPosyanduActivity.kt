package com.example.posyandu.features.jadwalPosyandu

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posyandu.databinding.ActivityJadwalPosyanduBinding
import com.example.posyandu.databinding.FragmentJadwalPosyanduCreateBinding
import com.example.posyandu.databinding.FragmentJadwalPosyanduEditBinding
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

class JadwalPosyanduActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJadwalPosyanduBinding
    private lateinit var viewModel: JadwalPosyanduViewModel

    companion object {
        private const val TAG = "JadwalPosyanduActivity"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalPosyanduBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[JadwalPosyanduViewModel::class.java]
        viewModel.listJadwalPosyandu.observe(this) { listJadwalPosyandu ->
            setJadwalPosyanduData(listJadwalPosyandu, view)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager

        binding.btnTambah.setOnClickListener {
            showCreateDialog(view)
        }
    }

    private fun setJadwalPosyanduData(jadwalPosyandu: List<JadwalPosyandu>, view: View) {
        val adapter = JadwalPosyanduIndexAdapter()
        adapter.submitList(jadwalPosyandu)
        binding.rvReview.adapter = adapter

        adapter.setOnClickListener(object :
            JadwalPosyanduIndexAdapter.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(position: Int, model: JadwalPosyandu) {
                showEditDialog(view, model)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEditDialog(view: View, model: JadwalPosyandu) {
        val binding = FragmentJadwalPosyanduEditBinding.inflate(layoutInflater)
        val customView = binding.root

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

        val utcMulai = ZonedDateTime.parse(
            "${model.waktuMulai}+00:00",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX")
        )
        val utcSelesai = ZonedDateTime.parse(
            "${model.waktuSelesai}+00:00",
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX")
        )

        val jakartaMulai = utcMulai.withZoneSameInstant(ZoneId.of("Asia/Jakarta"))
        val jakartaSelesai = utcSelesai.withZoneSameInstant(ZoneId.of("Asia/Jakarta"))

        var jamMulai = jakartaMulai.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        var jamSelesai = jakartaSelesai.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

        val tanggal = jamMulai.substring(0, 10)

        jamMulai = jamMulai.substring(11, 16)
        jamSelesai = jamSelesai.substring(11, 16)

        binding.tanggalEdit.setText(tanggal)
        binding.jamMulaiEdit.setText(jamMulai)
        binding.jamSelesaiEdit.setText(jamSelesai)

        val editDialog = MaterialAlertDialogBuilder(this)
            .setView(customView)
            .show()

        binding.btnSimpan.setOnClickListener {
            val tanggal = binding.tanggalEdit.text.toString()
            val jamMulai = binding.jamMulaiEdit.text.toString()
            val jamSelesai = binding.jamSelesaiEdit.text.toString()

            val jadwalPosyandu = CreateJadwalPosyanduResponse(
                model.posyandu.id,
                "${tanggal}T$jamMulai:00+07:00",
                "${tanggal}T$jamSelesai:00+07:00"
            )

            viewModel.updateJadwal(model.id, jadwalPosyandu)

            editDialog.cancel()

            Snackbar.make(view, "Jadwal berhasil diperbarui", Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.btnDel.setOnClickListener {
            viewModel.deleteJadwal(model.id)

            editDialog.cancel()

            Snackbar.make(view, "Jadwal berhasil dihapus", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showCreateDialog(view: View) {
        val binding = FragmentJadwalPosyanduCreateBinding.inflate(layoutInflater)
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

        val createDialog = MaterialAlertDialogBuilder(this)
            .setView(binding.root)
            .show()

        binding.btnTambah.setOnClickListener {
            val tanggal = binding.tanggalEdit.text.toString()
            val jamMulai = binding.jamMulaiEdit.text.toString()
            val jamSelesai = binding.jamSelesaiEdit.text.toString()

            val jadwalPosyandu = CreateJadwalPosyanduResponse(
                1,
                "${tanggal}T$jamMulai:00+07:00",
                "${tanggal}T$jamSelesai:00+07:00"
            )

            viewModel.createJadwal(jadwalPosyandu)

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