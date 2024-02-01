package com.example.posyandu.features.penyuluhan

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posyandu.features.daftarRemaja.DaftarRemajaEditActivity
import com.example.posyandu.R
import com.example.posyandu.databinding.ActivityJadwalPenyuluhanBinding
import com.example.posyandu.databinding.ActivityJadwalPosyanduBinding
import com.example.posyandu.features.jadwalPosyandu.JadwalPosyandu
import com.example.posyandu.features.jadwalPosyandu.JadwalPosyanduIndexAdapter
import com.example.posyandu.features.jadwalPosyandu.JadwalPosyanduViewModel
import com.google.android.material.snackbar.Snackbar

class JadwalPenyuluhanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJadwalPenyuluhanBinding
    private lateinit var viewModel: JadwalPenyuluhanViewModel

    val startNewActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                Snackbar.make(binding.root, snackbarMessage!!, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    private fun setJadwalPenyuluhanData(jadwalPenyuluhan: List<JadwalPenyuluhan>, view: View) {
        val adapter = JadwalPenyuluhanIndexAdapter()
        adapter.submitList(jadwalPenyuluhan)
        binding.rvReview.adapter = adapter

        adapter.setOnClickListener(object :
            JadwalPenyuluhanIndexAdapter.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(position: Int, model: JadwalPenyuluhan) {
                val intent = Intent(baseContext, JadwalPenyuluhanViewActivity::class.java)
                intent.putExtra("JadwalPenyuluhanid",model.id)
                startNewActivity.launch(intent)
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJadwalPenyuluhanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[JadwalPenyuluhanViewModel::class.java]
        viewModel.listJadwalPenyuluhan.observe(this) { listJadwalPenyuluhan ->
            setJadwalPenyuluhanData(listJadwalPenyuluhan, view)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager

        binding.btnTambah.setOnClickListener {
            val intent = Intent(baseContext, JadwalPenyuluhanCreateActivity::class.java)
            startNewActivity.launch(intent)
        }
    }
}


