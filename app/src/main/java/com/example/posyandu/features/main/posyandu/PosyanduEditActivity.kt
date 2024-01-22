package com.example.posyandu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityPosyanduEditBinding

class PosyanduEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPosyanduEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPosyanduEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSimpan.setOnClickListener {
            val intent = Intent()
            intent.putExtra("snackbar_message", "Posyandu berhasil diperbarui")
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}