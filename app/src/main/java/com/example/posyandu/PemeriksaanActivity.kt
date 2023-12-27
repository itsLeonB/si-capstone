package com.example.posyandu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityPemeriksaanBinding
import com.google.android.material.snackbar.Snackbar

class PemeriksaanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanBinding
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    Snackbar.make(view, snackbarMessage!!, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        binding.cardOk.setOnClickListener {
            val intent = Intent(this, PemeriksaanViewActivity::class.java)
            startNewActivity.launch(intent)
        }

        binding.btnTambah.setOnClickListener {
            val intent = Intent(this, PemeriksaanCreateActivity::class.java)
            startNewActivity.launch(intent)
        }
    }
}