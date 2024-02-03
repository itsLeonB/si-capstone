package com.example.posyandu.features.pemeriksaan

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posyandu.databinding.ActivityPemeriksaanBinding
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class PemeriksaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanBinding
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>
    private lateinit var viewModel: PemeriksaanViewModel
    private lateinit var nama: String
    private var nik by Delegates.notNull<Long>()
    private var usia by Delegates.notNull<Int>()
    private var remajaId by Delegates.notNull<Int>()
    private var userId by Delegates.notNull<Int>()
    private var currentUserId by Delegates.notNull<Int>()
    private lateinit var role: String

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            viewModel.indexPemeriksaan(userId)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.indexPemeriksaan(userId)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanBinding.inflate(layoutInflater)
        val view = binding.root

        userId = intent.getIntExtra("userId", 0)
        remajaId = intent.getIntExtra("remajaId", 0)
        nama = intent.getStringExtra("nama").toString()
        nik = intent.getLongExtra("nik", 0)
        usia = intent.getIntExtra("usia", 0)
        val prefs = getSharedPreferences("Preferences", MODE_PRIVATE)
        role = prefs.getString("role", "")!!
        currentUserId = prefs.getInt("currentUserId", 0)

        viewModel = ViewModelProvider(this)[PemeriksaanViewModel::class.java]
        viewModel.listPemeriksaan.observe(this) { listPemeriksaan ->
            setPemeriksaanData(listPemeriksaan)
        }

        viewModel.indexPemeriksaan(userId)
        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager

        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    if (snackbarMessage != null) {
                        Snackbar.make(view, snackbarMessage, Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        when (role) {
            "remaja" -> {
                binding.btnTambah.visibility = View.GONE
            }
            "kader" -> {
                if (currentUserId == userId) {
                    binding.btnTambah.visibility = View.GONE
                } else {
                    binding.btnTambah.setOnClickListener {
                        val intent = Intent(this, PemeriksaanCreateActivity::class.java)
                        intent.putExtra("nama", nama)
                        intent.putExtra("remajaId", remajaId)
                        startNewActivity.launch(intent)
                    }
                }
            }
            "bidan" -> {
                binding.btnTambah.setOnClickListener {
                    val intent = Intent(this, PemeriksaanCreateActivity::class.java)
                    intent.putExtra("nama", nama)
                    intent.putExtra("remajaId", remajaId)
                    startNewActivity.launch(intent)
                }
            }
        }

        setContentView(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setPemeriksaanData(listPemeriksaan: List<Pemeriksaan>) {
        binding.nama.text = nama
        binding.nik.text = "NIK: ${nik.toString()}"
        binding.ttl.text = "Usia: $usia tahun"

        if (listPemeriksaan.isNotEmpty()) {
            // Hide the nullAlert view
            binding.nullAlert.visibility = View.GONE
        } else {
            // Show the nullAlert view
            binding.nullAlert.visibility = View.VISIBLE
        }

        val adapter = PemeriksaanAdapter()
        adapter.submitList(listPemeriksaan)
        binding.rvReview.adapter = adapter

        adapter.setOnClickListener(object : PemeriksaanAdapter.OnClickListener {
            override fun onClick(position: Int, model: Pemeriksaan) {
                val intent = Intent(this@PemeriksaanActivity, PemeriksaanViewActivity::class.java)
                intent.putExtra("pemeriksaanId", model.id)
                startNewActivity.launch(intent)
            }
        })
    }
}