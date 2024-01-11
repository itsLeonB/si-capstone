package com.example.posyandu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityDaftarRemajaEditBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DaftarRemajaEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarRemajaEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarRemajaEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSimpan.setOnClickListener {
            val intent = Intent()
            intent.putExtra("snackbar_message", "Remaja berhasil diperbarui")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.btnDel.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Apakah anda yakin untuk menghapus remaja?")
                .setPositiveButton("Ya") { _, _ ->
                    val intent = Intent()
                    intent.putExtra("snackbar_message", "Remaja berhasil dihapus")
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                .setNegativeButton("Tidak") { _, _ ->
                }
                .show()
        }
    }
}