package com.example.posyandu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posyandu.databinding.ActivityDaftarRemajaBinding
import com.example.posyandu.databinding.FragmentDaftarRemajaShowBinding
import com.example.posyandu.features.daftarRemaja.DaftarRemajaCreateActivity
import com.example.posyandu.features.daftarRemaja.DaftarRemajaIndexAdapter
import com.example.posyandu.features.daftarRemaja.DaftarRemajaViewModel
import com.example.posyandu.features.daftarRemaja.RemajaDataItem
import com.example.posyandu.features.daftarRemaja.UpdateRemajaRequest
import com.example.posyandu.features.daftarRemaja.UpdateRemajaResponse
import com.example.posyandu.utils.ApiConfig
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarRemajaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarRemajaBinding
    private lateinit var viewModel: DaftarRemajaViewModel

    val startNewActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                Snackbar.make(binding.root, snackbarMessage!!, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

    companion object {
        private const val TAG = "DaftarRemajaActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarRemajaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[DaftarRemajaViewModel::class.java]
        viewModel.listRemaja.observe(this) { listRemaja ->
            setRemajaData(listRemaja, view)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvReview.layoutManager = layoutManager

        binding.btnTambah.setOnClickListener {
            val intent = Intent(this, DaftarRemajaCreateActivity::class.java)
            startNewActivity.launch(intent)
        }
    }

    private fun setRemajaData(remaja: List<RemajaDataItem>, view: View) {
        val adapter = DaftarRemajaIndexAdapter()
        adapter.submitList(remaja)
        binding.rvReview.adapter = adapter

        adapter.setOnClickListener(object :
            DaftarRemajaIndexAdapter.OnClickListener {
            override fun onClick(position: Int, model: RemajaDataItem) {
                val binding = FragmentDaftarRemajaShowBinding.inflate(layoutInflater)
                val customView = binding.root

                val remajaDialog = MaterialAlertDialogBuilder(this@DaftarRemajaActivity)
                    .setView(customView)
                    .show()

                binding.optProfil.setOnClickListener {
                    remajaDialog.cancel()
                    val intent =
                        Intent(this@DaftarRemajaActivity, DaftarRemajaEditActivity::class.java)
                    startNewActivity.launch(intent)
                }

                binding.optPemeriksaan.setOnClickListener {
                    remajaDialog.cancel()
                    val intent = Intent(this@DaftarRemajaActivity, PemeriksaanActivity::class.java)
                    startNewActivity.launch(intent)
                }

                if (model.isKader) {
                    binding.optRemaja.visibility = View.GONE
                    binding.optKader.visibility = View.VISIBLE

                    val prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
                    val token = prefs.getString("token", "no token")

                    val remajaRequest = UpdateRemajaRequest(
                        model.isKader,
                        model.namaIbu,
                        model.namaAyah,
                        model.posyandu.id
                    )

                    val client =
                        ApiConfig.getApiService().updateRemaja(
                            token = "Bearer $token",
                            remajaRequest,
                        )

                    binding.optKader.setOnClickListener {
                        remajaDialog.cancel()
                        MaterialAlertDialogBuilder(this@DaftarRemajaActivity)
                            .setTitle("Anda yakin untuk mencabut akses kader?")
                            .setPositiveButton("Ya") { _, _ ->
                                remajaRequest.isKader = false

                                client.enqueue(object : Callback<UpdateRemajaResponse> {
                                    override fun onResponse(
                                        call: Call<UpdateRemajaResponse>,
                                        response: Response<UpdateRemajaResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            viewModel.indexRemaja()
                                            val intent = Intent()
                                            intent.putExtra(
                                                "snackbar_message",
                                                "Akses kader berhasil dicabut"
                                            )
                                            setResult(Activity.RESULT_OK, intent)
                                            finish()
                                        } else {
                                            Log.e(TAG, "onFailure: ${response.message()}")
                                            Snackbar.make(
                                                view,
                                                "Terjadi error, harap coba lagi",
                                                Snackbar.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<UpdateRemajaResponse>,
                                        t: Throwable
                                    ) {
                                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                                    }
                                })
                            }
                            .setNegativeButton("Tidak") { _, _ ->
                                remajaDialog.show()
                            }
                            .show()
                    }
                } else {
                    binding.optRemaja.visibility = View.VISIBLE
                    binding.optKader.visibility = View.GONE
                    val prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
                    val token = prefs.getString("token", "no token")

                    val remajaRequest = UpdateRemajaRequest(
                        model.isKader,
                        model.namaIbu,
                        model.namaAyah,
                        model.posyandu.id
                    )

                    val client =
                        ApiConfig.getApiService().updateRemaja(
                            token = "Bearer $token",
                            remajaRequest,
                        )

                    binding.optRemaja.setOnClickListener {
                        remajaDialog.cancel()
                        MaterialAlertDialogBuilder(this@DaftarRemajaActivity)
                            .setTitle("Anda yakin untuk memberikan akses kader?")
                            .setPositiveButton("Ya") { _, _ ->
                                remajaRequest.isKader = true

                                client.enqueue(object : Callback<UpdateRemajaResponse> {
                                    override fun onResponse(
                                        call: Call<UpdateRemajaResponse>,
                                        response: Response<UpdateRemajaResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            viewModel.indexRemaja()
                                            val intent = Intent()
                                            intent.putExtra(
                                                "snackbar_message",
                                                "Akses kader berhasil diberikan"
                                            )
                                            setResult(Activity.RESULT_OK, intent)
                                            finish()
                                        } else {
                                            Log.e(TAG, "onFailure: ${response.message()}")
                                            Snackbar.make(
                                                view,
                                                "Terjadi error, harap coba lagi",
                                                Snackbar.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<UpdateRemajaResponse>,
                                        t: Throwable
                                    ) {
                                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                                    }
                                })
                            }
                            .setNegativeButton("Tidak") { _, _ ->
                                remajaDialog.show()
                            }
                            .show()
                    }
                }
            }
        })
    }
}