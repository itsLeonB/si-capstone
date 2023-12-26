package com.example.posyandu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityDaftarRemajaBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class DaftarRemajaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarRemajaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarRemajaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    Snackbar.make(view, snackbarMessage!!, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        binding.kader.setOnClickListener {
            val customView = LayoutInflater
                .from(this)
                .inflate(R.layout.fragment_daftar_remaja_show_kader, null)

            val optProfil: MaterialCardView = customView.findViewById(R.id.opt_profil)
            val optPemeriksaan: MaterialCardView = customView.findViewById(R.id.opt_pemeriksaan)
            val optKader: MaterialCardView = customView.findViewById(R.id.opt_kader)

            val remajaDialog = MaterialAlertDialogBuilder(this)
                .setView(customView)
                .show()

            optProfil.setOnClickListener {
                remajaDialog.cancel()
                val intent = Intent(this, DaftarRemajaEditActivity::class.java)
                startNewActivity.launch(intent)
            }

            optPemeriksaan.setOnClickListener {
                remajaDialog.cancel()
                val intent = Intent(this, PemeriksaanActivity::class.java)
                startNewActivity.launch(intent)
            }

            optKader.setOnClickListener {
                remajaDialog.cancel()
                MaterialAlertDialogBuilder(this)
                    .setTitle("Anda yakin untuk mencabut akses kader?")
                    .setPositiveButton("Ya") { _, _ ->
                        Snackbar.make(
                            view,
                            "Akses kader berhasil dicabut",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    .setNegativeButton("Tidak") { _, _ ->
                        remajaDialog.show()
                    }
                    .show()
            }
        }

        binding.remaja.setOnClickListener {
            val customView = LayoutInflater
                .from(this)
                .inflate(R.layout.fragment_daftar_remaja_show, null)

            val optProfil: MaterialCardView = customView.findViewById(R.id.opt_profil)
            val optPemeriksaan: MaterialCardView = customView.findViewById(R.id.opt_pemeriksaan)
            val optKader: MaterialCardView = customView.findViewById(R.id.opt_kader)

            val remajaDialog = MaterialAlertDialogBuilder(this)
                .setView(customView)
                .show()

            optProfil.setOnClickListener {
                remajaDialog.cancel()
                val intent = Intent(this, DaftarRemajaEditActivity::class.java)
                startNewActivity.launch(intent)
            }

            optPemeriksaan.setOnClickListener {
                remajaDialog.cancel()
                val intent = Intent(this, PemeriksaanActivity::class.java)
                startNewActivity.launch(intent)
            }

            optKader.setOnClickListener {
                remajaDialog.cancel()
                MaterialAlertDialogBuilder(this)
                    .setTitle("Apakah anda yakin untuk menjadikan kader?")
                    .setPositiveButton("Ya") { _, _ ->
                        Snackbar.make(
                            view,
                            "Akses kader berhasil diberikan",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    .setNegativeButton("Tidak") { _, _ ->
                        remajaDialog.show()
                    }
                    .show()
            }
        }

        binding.btnTambah.setOnClickListener {
            val intent = Intent(this, DaftarRemajaCreateActivity::class.java)
            startNewActivity.launch(intent)
        }
    }
}