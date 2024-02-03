package com.example.posyandu.features.daftarKader

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posyandu.databinding.ActivityDaftarKaderBinding
import com.example.posyandu.features.main.konsultasi.KonsultasiChatActivity
import com.google.android.material.snackbar.Snackbar

class DaftarKaderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarKaderBinding
    private lateinit var viewModel: DaftarKaderViewModel
    private lateinit var token: String

    val startNewActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                Snackbar.make(binding.root, snackbarMessage!!, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

    companion object {
        private const val TAG = "DaftarKaderActivity"
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        intent.putExtra("snackbar_message", "Your message")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarKaderBinding.inflate(layoutInflater)
        val view = binding.root
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[DaftarKaderViewModel::class.java]
        viewModel.listKader.observe(this) { listKader ->
            displayData(listKader, view)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager

        val prefs = getSharedPreferences("Preferences", MODE_PRIVATE)
        token = prefs.getString("token", "no token").toString()

        setContentView(view)
    }

    private fun displayData(kader: List<KaderItem>, view: View) {
        val adapter = DaftarKaderAdapter(this)
        adapter.submitList(kader)
        binding.rvReview.adapter = adapter

        if (kader.isNotEmpty()) {
            // Hide the nullAlert view
            binding.nullAlert.visibility = View.GONE
        } else {
            // Show the nullAlert view
            binding.nullAlert.visibility = View.VISIBLE
        }

        binding.kaderCount.setText("${kader.count()} kader terdaftar")

        adapter.setOnClickListener(object :
            DaftarKaderAdapter.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(position: Int, model: KaderItem) {
                val intent = Intent(this@DaftarKaderActivity, KonsultasiChatActivity::class.java)
                intent.putExtra("receiverId", model.user.id)
                startNewActivity.launch(intent)
            }
        })
    }
}