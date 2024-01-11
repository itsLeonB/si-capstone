package com.example.posyandu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityPemeriksaanViewBinding
import com.google.android.material.snackbar.Snackbar

class PemeriksaanViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanViewBinding
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Retrieve the message from the intent
        val message = intent.getStringExtra("snackbar_message")

        // Check if the message is not null and display it using a Snackbar
        message?.let {
            val snackbar =
                Snackbar.make(findViewById(android.R.id.content), it, Snackbar.LENGTH_SHORT)
            snackbar.show()
        }

        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    Snackbar.make(view, snackbarMessage!!, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, PemeriksaanEditActivity::class.java)
            startNewActivity.launch(intent)
        }
    }
}