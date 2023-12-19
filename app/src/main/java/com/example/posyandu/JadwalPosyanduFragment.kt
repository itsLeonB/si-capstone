package com.example.posyandu

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
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

        val btnTambah: FloatingActionButton = view.findViewById(R.id.btn_tambah)
        val cardJadwal: MaterialCardView = view.findViewById(R.id.card_jadwal_1)

        btnTambah.setOnClickListener {
            showCreateDialog()
        }

        cardJadwal.setOnClickListener {
            showViewDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showViewDialog() {
        val customView =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_jadwal_posyandu_view, null)

        val viewDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(customView)
            .show()

        val btnTambah: Button = customView.findViewById(R.id.btn_tambah)

        val editJadwal: TextInputEditText = customView.findViewById(R.id.jam_2_edit)

        editJadwal.setOnClickListener {
            viewDialog.cancel()
            showEditDialog()
        }

        btnTambah.setOnClickListener {
            viewDialog.cancel()
            showCreateDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEditDialog() {
        val customView =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_jadwal_posyandu_edit, null)
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
            datePicker.show(parentFragmentManager, datePicker.toString())
        }

        jamMulaiEdit.setOnClickListener {
            val timePicker = createTimePicker(jamMulaiEdit)
            timePicker.show(parentFragmentManager, timePicker.toString())
        }

        jamSelesaiEdit.setOnClickListener {
            val timePicker = createTimePicker(jamSelesaiEdit)
            timePicker.show(parentFragmentManager, timePicker.toString())
        }

        val editDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(customView)
            .show()

        btnTambah.setOnClickListener {
            val btmBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

            editDialog.cancel()

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, JadwalPosyanduFragment())
                .commit()

            Snackbar.make(btmBar!!, "Jadwal berhasil diperbarui", Snackbar.LENGTH_SHORT)
                .setAnchorView(btmBar)
                .show()
        }

        btnDel.setOnClickListener {
            val btmBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

            editDialog.cancel()

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, JadwalPosyanduFragment())
                .commit()

            Snackbar.make(btmBar!!, "Jadwal berhasil dihapus", Snackbar.LENGTH_SHORT)
                .setAnchorView(btmBar)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showCreateDialog() {
        val customView =
            LayoutInflater.from(requireContext()).inflate(R.layout.fragment_jadwal_posyandu_create, null)
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
            datePicker.show(parentFragmentManager, datePicker.toString())
        }

        jamMulaiEdit.setOnClickListener {
            val timePicker = createTimePicker(jamMulaiEdit)
            timePicker.show(parentFragmentManager, timePicker.toString())
        }

        jamSelesaiEdit.setOnClickListener {
            val timePicker = createTimePicker(jamSelesaiEdit)
            timePicker.show(parentFragmentManager, timePicker.toString())
        }

        val createDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(customView)
            .show()

        btnTambah.setOnClickListener {
            val btmBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

            createDialog.cancel()

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, JadwalPosyanduFragment())
                .commit()

            Snackbar.make(btmBar!!, "Jadwal berhasil ditambahkan", Snackbar.LENGTH_SHORT)
                .setAnchorView(btmBar)
                .show()
        }
    }

    @SuppressLint("NewApi")
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