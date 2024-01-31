package com.example.posyandu.features.daftarRemaja

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.posyandu.R
import com.example.posyandu.databinding.ActivityDaftarRemajaBinding
import com.example.posyandu.databinding.FragmentDaftarRemajaShowBinding
import com.example.posyandu.features.pemeriksaan.PemeriksaanActivity
import com.example.posyandu.features.pemeriksaan.PemeriksaanCreateActivity
import com.example.posyandu.utils.ApiConfig
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            viewModel.indexRemaja()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.indexRemaja()
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
        binding = ActivityDaftarRemajaBinding.inflate(layoutInflater)
        val view = binding.root
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

        setContentView(view)
    }

    private fun setRemajaData(remaja: List<RemajaDataItem>, view: View) {
        val adapter = DaftarRemajaIndexAdapter()
        adapter.submitList(remaja)
        binding.rvReview.adapter = adapter

        if (remaja.isNotEmpty()) {
            // Hide the nullAlert view
            binding.nullAlert.visibility = View.GONE
        } else {
            // Show the nullAlert view
            binding.nullAlert.visibility = View.VISIBLE
        }

        binding.jumlahRemaja.setText("${remaja.count()} remaja terdaftar")
        binding.jumlahKader.setText("${remaja.count { it.isKader }} kader aktif")
        binding.jumlahRisiko.setText("${remaja.count { it.pemeriksaan.berisiko() && it.dataAvailable() }} remaja berisiko stunting")

        adapter.setOnClickListener(object :
            DaftarRemajaIndexAdapter.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(position: Int, model: RemajaDataItem) {
                val binding = FragmentDaftarRemajaShowBinding.inflate(layoutInflater)
                val customView = binding.root

                val user = model.user
                binding.nama.text = user.nama
                binding.nik.text = user.nik.toString()

                val tanggalLahir = user.tanggalLahir
                val birthDate = LocalDate.parse(
                    tanggalLahir,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale("id"))
                )
                binding.tanggalLahir.text =
                    birthDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("id")))

                val remajaDialog = MaterialAlertDialogBuilder(this@DaftarRemajaActivity)
                    .setView(customView)
                    .show()

                if (model.dataAvailable()) {
                    if (model.pemeriksaan.berisiko()) {
                        binding.optPemeriksaan.setCardBackgroundColor(getColor(R.color.md_theme_light_errorContainer))
                        binding.textRisiko.text = "Berisiko stunting"
                        binding.textRisiko.setTextColor(getColor(R.color.md_theme_light_error))
                        binding.textPemeriksaan.setTextColor(getColor(R.color.md_theme_light_error))
                        binding.drawPemeriksaan.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@DaftarRemajaActivity,
                                R.drawable.error_cross
                            )
                        )
                    } else {
                        binding.optPemeriksaan.setCardBackgroundColor(getColor(R.color.success_light))
                        binding.textRisiko.text = "Tidak berisiko stunting"
                        binding.textRisiko.setTextColor(getColor(R.color.success))
                        binding.textPemeriksaan.setTextColor(getColor(R.color.success))
                        binding.drawPemeriksaan.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@DaftarRemajaActivity,
                                R.drawable.done_checkmark
                            )
                        )
                    }

                    binding.optPemeriksaan.setOnClickListener {
                        remajaDialog.cancel()
                        val intent =
                            Intent(this@DaftarRemajaActivity, PemeriksaanActivity::class.java)
                        intent.putExtra("userId", model.user.id)
                        intent.putExtra("remajaId", model.id)
                        intent.putExtra("nama", model.user.nama)
                        intent.putExtra("nik", model.user.nik)
                        intent.putExtra("usia", model.user.usia())
                        startNewActivity.launch(intent)
                    }
                } else {
                    binding.optPemeriksaan.setCardBackgroundColor(getColor(R.color.md_theme_dark_inverseSurface))
                    binding.textRisiko.text = "Belum ada data"
                    binding.textPemeriksaan.text = "Tambah data"
                    binding.textRisiko.setTextColor(getColor(R.color.md_theme_light_onSurface))
                    binding.textPemeriksaan.setTextColor(getColor(R.color.md_theme_light_onSurface))
                    binding.drawPemeriksaan.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@DaftarRemajaActivity,
                            R.drawable.null_sign
                        )
                    )

                    binding.optPemeriksaan.setOnClickListener {
                        remajaDialog.cancel()
                        val intent =
                            Intent(this@DaftarRemajaActivity, PemeriksaanCreateActivity::class.java)
                        intent.putExtra("userId", model.user.id)
                        intent.putExtra("remajaId", model.id)
                        intent.putExtra("nama", model.user.nama)
                        intent.putExtra("nik", model.user.nik)
                        intent.putExtra("usia", model.user.usia())
                        startNewActivity.launch(intent)
                    }
                }

                binding.optProfil.setOnClickListener {
                    remajaDialog.cancel()
                    val intent =
                        Intent(this@DaftarRemajaActivity, DaftarRemajaEditActivity::class.java)
                    intent.putExtra("userId", model.user.id)
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
                            model.id,
                            "Bearer $token",
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
                                            Snackbar.make(
                                                view,
                                                "Akses kader berhasil dicabut",
                                                Snackbar.LENGTH_SHORT
                                            )
                                                .show()
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
                            model.id,
                            "Bearer $token",
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
                                            Snackbar.make(
                                                view,
                                                "Akses kader berhasil diberikan",
                                                Snackbar.LENGTH_SHORT
                                            )
                                                .show()
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