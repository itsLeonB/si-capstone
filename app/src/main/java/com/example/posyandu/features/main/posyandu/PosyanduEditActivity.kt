package com.example.posyandu.features.main.posyandu

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.posyandu.databinding.ActivityPosyanduEditBinding
import com.example.posyandu.utils.ApiConfig
import com.example.posyandu.utils.file.FileUploadResponse
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PosyanduEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPosyanduEditBinding
    private lateinit var viewModel: PosyanduEditViewModel

    companion object {
        private const val TAG = "PosyanduEditActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPosyanduEditBinding.inflate(layoutInflater)
        val view = binding.root
        supportActionBar?.hide()

        viewModel = ViewModelProvider(this)[PosyanduEditViewModel::class.java]
        viewModel.posyanduData.observe(this) {
            populateFields(it)
        }

        val pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri = result.data?.data
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            uploadImage(imageUri)
                        }
                    }
                } else {
                    Toast.makeText(this, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (permissions[android.Manifest.permission.READ_MEDIA_IMAGES] == true) {
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickImageLauncher.launch(intent)
                    } else {
                        Toast.makeText(
                            this@PosyanduEditActivity,
                            "Ijinkan penggunaan storage",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Check if the user selected "Don't ask again" and show a dialog to navigate to app settings
                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES)) {
                            showSettingsDialog()
                        }
                    }
                } else {
                    if (permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                        val intent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickImageLauncher.launch(intent)
                    } else {
                        Toast.makeText(
                            this@PosyanduEditActivity,
                            "Ijinkan penggunaan storage",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Check if the user selected "Don't ask again" and show a dialog to navigate to app settings
                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showSettingsDialog()
                        }
                    }
                }
            }

        binding.imgEdit.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES))
            } else {
                requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }

        binding.btnSimpan.setOnClickListener {
            saveData()
        }

        setContentView(view)
    }

    private fun showSettingsDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Ijinkan akses ke storage melalui Pengaturan Aplikasi.")
            .setCancelable(false)
            .setPositiveButton("Buka Pengaturan") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun createImageFile(): Pair<File, Boolean> {
        // Implement your logic to generate a unique file name
        val fileName = "posyandu_image_${System.currentTimeMillis()}.jpg"

        // Get the external cache directory or use another appropriate directory
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: return File(filesDir, fileName) to false

        val file = File(storageDir, fileName)

        return file to file.createNewFile()
    }

    private suspend fun uploadImage(imageUri: Uri?) {
        if (imageUri != null) {
            val inputStream = contentResolver.openInputStream(imageUri)

            if (inputStream != null) {
                var (file, isFileCreated) = createImageFile()

                if (isFileCreated) {
                    file.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }

                    val compressedFile = Compressor.compress(this, file)
                    file = compressedFile

                    val requestFile =
                        compressedFile.asRequestBody("image/*".toMediaTypeOrNull())
                    val body =
                        requestFile.let {
                            MultipartBody.Part.createFormData(
                                "file",
                                file.name,
                                it
                            )
                        }

                    val typeRequestBody = "image".toRequestBody("text/plain".toMediaTypeOrNull())

                    val prefs = getSharedPreferences("Preferences", MODE_PRIVATE)
                    val token = prefs.getString("token", "")
                    val client = body.let {
                        ApiConfig.getApiService()
                            .uploadFile(file = it, type = typeRequestBody, token = "Bearer $token")
                    }
                    client.enqueue(object : Callback<FileUploadResponse> {
                        override fun onResponse(
                            call: Call<FileUploadResponse>,
                            response: Response<FileUploadResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@PosyanduEditActivity,
                                    "Image uploaded",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val path = response.body()?.data?.path
                                binding.imgEdit.setText(path)
                                inputStream.close()
                            } else {
                                Toast.makeText(
                                    this@PosyanduEditActivity,
                                    "Image upload failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(TAG, "onFailure: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                            Toast.makeText(
                                this@PosyanduEditActivity,
                                "Image upload failed",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(TAG, "onFailure: ${t.message.toString()}")
                        }
                    })
                } else {
                    // Handle the case where the InputStream is null
                    Toast.makeText(
                        this@PosyanduEditActivity,
                        "Failed to open image file",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Handle the case where the imageUri is null
                Toast.makeText(this@PosyanduEditActivity, "Image URI is null", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun saveData() {
        val prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")

        val posyandu = UpdatePosyanduRequest(
            nama = binding.namaEdit.text.toString(),
            alamat = binding.alamatEdit.text.toString(),
            provinsi = binding.provinsiEdit.text.toString(),
            kota = binding.kotaEdit.text.toString(),
            kecamatan = binding.kecamatanEdit.text.toString(),
            kelurahan = binding.kelurahanEdit.text.toString(),
            kodePos = binding.kodeposEdit.text.toString().toInt(),
            rt = binding.rtEdit.text.toString().toInt(),
            rw = binding.rwEdit.text.toString().toInt(),
            foto = binding.imgEdit.text.toString()
        )

        val client = ApiConfig.getApiService()
            .updatePosyandu(
                viewModel.posyanduData.value!!.id,
                token = "Bearer $token",
                posyandu
            )

        client.enqueue(object : Callback<UpdatePosyanduResponse> {
            override fun onResponse(
                call: Call<UpdatePosyanduResponse>,
                response: Response<UpdatePosyanduResponse>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent()
                    intent.putExtra("snackbar_message", "Posyandu berhasil diperbarui")
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UpdatePosyanduResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        }
        )
    }

    private fun populateFields(posyandu: Posyandu) {
        binding.namaEdit.setText(posyandu.nama)
        binding.alamatEdit.setText(posyandu.alamat)
        binding.provinsiEdit.setText(posyandu.provinsi)
        binding.kotaEdit.setText(posyandu.kota)
        binding.kecamatanEdit.setText(posyandu.kecamatan)
        binding.kelurahanEdit.setText(posyandu.kelurahan)
        binding.kodeposEdit.setText(posyandu.kodePos.toString())
        binding.rtEdit.setText(posyandu.rt.toString())
        binding.rwEdit.setText(posyandu.rw.toString())
    }
}