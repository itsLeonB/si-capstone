package com.example.posyandu

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

class JadwalPosyanduFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_jadwal_posyandu, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnTambah: ExtendedFloatingActionButton = view.findViewById(R.id.btn_tambah)
        val cardJadwal: MaterialCardView = view.findViewById(R.id.card_jadwal_1)

        btnTambah.setOnClickListener {
            showAlertDialog()
        }

        cardJadwal.setOnClickListener {
            showViewDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showViewDialog() {
        val customView =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_ubah_jadwal, null)

        MaterialAlertDialogBuilder(requireContext())
            .setView(customView)
            .show()

        val btnTambah: Button = customView.findViewById(R.id.btn_tambah)

        val editJadwal: TextInputEditText = customView.findViewById(R.id.jam_2_edit)

        editJadwal.setOnClickListener {
            showEditDialog()
        }

        btnTambah.setOnClickListener {
            showAlertDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEditDialog() {
        val customView =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_edit_jadwal, null)
        val tanggalEdit: TextInputEditText = customView.findViewById(R.id.tanggal_edit)
        val jamMulaiEdit: TextInputEditText = customView.findViewById(R.id.jam_mulai_edit)
        val jamSelesaiEdit: TextInputEditText = customView.findViewById(R.id.jam_selesai_edit)

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(today)

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
            tanggalEdit.setText(formattedDate)
        }

        var selectedEditText: TextInputEditText? = null

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Pilih jam mulai")
                .build()

        timePicker.addOnPositiveButtonClickListener {
            // Handle the selected time
            val selectedTime = String.format("%02d:%02d", timePicker.hour, timePicker.minute)

            // Set the selected time to the appropriate EditText
            selectedEditText?.setText(selectedTime)
        }

        tanggalEdit.setOnClickListener {
            datePicker.show(parentFragmentManager, datePicker.toString())
        }

        jamMulaiEdit.setOnClickListener {
            selectedEditText = jamMulaiEdit
            timePicker.show(parentFragmentManager, timePicker.toString())
        }

        jamSelesaiEdit.setOnClickListener {
            selectedEditText = jamSelesaiEdit
            timePicker.show(parentFragmentManager, timePicker.toString())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setView(customView)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAlertDialog() {
        val customView =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_tambah_jadwal, null)
        val tanggalEdit: TextInputEditText = customView.findViewById(R.id.tanggal_edit)
        val jamMulaiEdit: TextInputEditText = customView.findViewById(R.id.jam_mulai_edit)
        val jamSelesaiEdit: TextInputEditText = customView.findViewById(R.id.jam_selesai_edit)

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setStart(today)

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
            tanggalEdit.setText(formattedDate)
        }

        var selectedEditText: TextInputEditText? = null

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Pilih jam mulai")
                .build()

        timePicker.addOnPositiveButtonClickListener {
            // Handle the selected time
            val selectedTime = String.format("%02d:%02d", timePicker.hour, timePicker.minute)

            // Set the selected time to the appropriate EditText
            selectedEditText?.setText(selectedTime)
        }

        tanggalEdit.setOnClickListener {
            datePicker.show(parentFragmentManager, datePicker.toString())
        }

        jamMulaiEdit.setOnClickListener {
            selectedEditText = jamMulaiEdit
            timePicker.show(parentFragmentManager, timePicker.toString())
        }

        jamSelesaiEdit.setOnClickListener {
            selectedEditText = jamSelesaiEdit
            timePicker.show(parentFragmentManager, timePicker.toString())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setView(customView)
            .show()
    }
}