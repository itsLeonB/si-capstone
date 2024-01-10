package com.example.posyandu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityPemeriksaanEditBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PemeriksaanEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnTambah.setOnClickListener {
            val intent = Intent()
            intent.putExtra("snackbar_message", "Pemeriksaan berhasil disimpan")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.btnDel.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Apakah anda yakin untuk menghapus pemeriksaan?")
                .setPositiveButton("Ya") { _, _ ->
                    val intent = Intent()
                    intent.putExtra("snackbar_message", "Pemeriksaan berhasil dihapus")
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                .setNegativeButton("Tidak") { _, _ ->
                }
                .show()

        }
    }
}