package com.example.posyandu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.databinding.ActivityPemeriksaanCreateBinding

class PemeriksaanCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPemeriksaanCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPemeriksaanCreateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnTambah.setOnClickListener {
            val intent = Intent()
            val newIntent = Intent(this, PemeriksaanViewActivity::class.java)
            newIntent.putExtra("snackbar_message", "Pemeriksaan berhasil disimpan")
            setResult(Activity.RESULT_OK, newIntent)
            startActivity(newIntent)
            finish()
        }
    }
}