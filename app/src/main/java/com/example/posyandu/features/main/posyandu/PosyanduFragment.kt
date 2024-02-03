package com.example.posyandu.features.main.posyandu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.posyandu.BuildConfig
import com.example.posyandu.R
import com.example.posyandu.databinding.FragmentPosyanduBinding
import com.example.posyandu.features.daftarKader.DaftarKaderActivity
import com.example.posyandu.features.daftarRemaja.DaftarRemajaActivity
import com.example.posyandu.features.daftarRemaja.RemajaDataItem
import com.example.posyandu.features.daftarRemaja.RemajaIdNama
import com.example.posyandu.features.jadwalPosyandu.JadwalPosyanduActivity
import com.example.posyandu.features.main.MainActivity
import com.example.posyandu.features.main.MainActivityViewModel
import com.example.posyandu.features.main.Posyandu
import com.example.posyandu.features.pemeriksaan.PemeriksaanActivity
import com.example.posyandu.features.pemeriksaan.PemeriksaanCreateActivity
import com.example.posyandu.features.penyuluhan.JadwalPenyuluhanActivity
import com.example.posyandu.utils.ApiConfig
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class PosyanduFragment : Fragment() {
    private lateinit var binding: FragmentPosyanduBinding
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>
    private lateinit var btmBar: BottomNavigationView
    private lateinit var prefs: SharedPreferences
    private lateinit var token: String
    private lateinit var role: String
    private var posyanduId: Int = 0
    private var bidanId: Int = 0
    private var currentRemajaId: Int = 0
    private val viewModel: MainActivityViewModel by activityViewModels()

    companion object {
        private const val TAG = "PosyanduFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                parentFragmentManager.popBackStack()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
//        if (role == "bidan") {
//            displayPosyanduDataBidan()
//        } else if (isKader) {
//            displayPosyanduDataKader()
//        } else {
//            displayPosyanduDataRemaja()
//        }
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            viewModel.refreshData()
//            if (role == "bidan") {
//                displayPosyanduDataBidan()
//            } else if (isKader) {
//                displayPosyanduDataKader()
//            } else {
//                displayPosyanduDataRemaja()
//            }
        }
    }

    private fun loadPrefs() {
        prefs = requireActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        token = prefs.getString("token", "no token")!!
        role = prefs.getString("role", "no role")!!
        posyanduId = prefs.getInt("posyanduId", 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        loadPrefs()
        btmBar = requireActivity().findViewById(R.id.bottom_navigation)

        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.refreshData()
//                    if (role == "bidan") {
//                        displayPosyanduDataBidan()
//                    } else if (isKader) {
//                        displayPosyanduDataKader()
//                    } else {
//                        displayPosyanduDataRemaja()
//                    }
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    if (snackbarMessage != null) {
                        Snackbar.make(btmBar, snackbarMessage, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }

        bidanId = prefs.getInt("bidanId", 0)
        currentRemajaId = prefs.getInt("currentRemajaId", 0)
        binding = FragmentPosyanduBinding.inflate(inflater, container, false)

        when (role) {
            "bidan" -> {
                viewModel.mainBidanData.observe(viewLifecycleOwner) {
                    displayDataBidan()
                }

                viewModel.mainRemajaData.observe(viewLifecycleOwner) {
                    displayDataBidan()
                }
            }

            "kader" -> {
                viewModel.mainData.observe(viewLifecycleOwner) {
                    displayDataKader()
                }

                viewModel.mainPemeriksaanData.observe(viewLifecycleOwner) {
                    displayDataKader()
                }

                viewModel.mainRemajaData.observe(viewLifecycleOwner) {
                    displayDataKader()
                }

                viewModel.mainKaderData.observe(viewLifecycleOwner) {
                    displayDataKader()
                }
            }

            "remaja" -> {
                viewModel.mainData.observe(viewLifecycleOwner) {
                    displayDataRemaja()
                }

                viewModel.mainPemeriksaanData.observe(viewLifecycleOwner) {
                    displayDataRemaja()
                }

                viewModel.mainKaderData.observe(viewLifecycleOwner) {
                    displayDataRemaja()
                }
            }
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayDataBidan() {
        displayPosyandu(viewModel.mainBidanData.value?.posyandu)
        displayJadwalPosyandu(
            viewModel.mainBidanData.value?.sortedPosyandu()?.firstOrNull()?.waktuMulai
        )
        displayJadwalPenyuluhan(
            viewModel.mainBidanData.value?.sortedPenyuluhan()?.firstOrNull()?.waktuMulai
        )
        displayDaftarRemaja(viewModel.mainRemajaData.value)

        binding.cardRisiko.visibility = View.GONE
        binding.cardKader.visibility = View.GONE
    }

    private fun displayDaftarRemaja(listRemaja: List<RemajaDataItem>?) {
        if (listRemaja != null) {
            val remajaCount = listRemaja.count()
            val kaderCount = listRemaja.count { it.isKader }
            val berisikoCount = listRemaja.count { it.pemeriksaan.berisiko() && it.dataAvailable() }

            binding.cardTitleRemaja.text = "$remajaCount remaja terdaftar"
            binding.angkaKader.text = kaderCount.toString()
            binding.angkaRisiko.text = berisikoCount.toString()
        } else {
            binding.cardTitleRemaja.text = "Tidak ada remaja"
            binding.angkaKader.text = "0"
            binding.angkaRisiko.text = "0"
        }
    }

    private fun displayJadwalPosyandu(waktuMulai: String?) {
        if (waktuMulai != null) {
            val tanggal = waktuMulai.substring(0, 10)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale("id"))
            val date = format.parse(tanggal)
            val hariFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))
            val hari = date?.let { hariFormat.format(it) }
            val jam = waktuMulai.substring(11, 16)

            binding.tanggalJadwalPosyandu.text = hari
            binding.jamJadwalPosyandu.text = "Pukul $jam WIB"
        } else {
            binding.tanggalJadwalPosyandu.text = "Tidak ada jadwal"
            binding.jamJadwalPosyandu.visibility = View.GONE
        }
    }

    private fun displayJadwalPenyuluhan(waktuMulai: String?) {
        if (waktuMulai != null) {
            val tanggal = waktuMulai.substring(0, 10)
            val format = SimpleDateFormat("yyyy-MM-dd", Locale("id"))
            val date = format.parse(tanggal)
            val hariFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))
            val hari = date?.let { hariFormat.format(it) }
            val jam = waktuMulai.substring(11, 16)

            binding.tanggalJadwalPenyuluhan.text = hari
            binding.jamJadwalPenyuluhan.text = "Pukul $jam WIB"
        } else {
            binding.tanggalJadwalPenyuluhan.text = "Tidak ada jadwal"
            binding.jamJadwalPenyuluhan.visibility = View.GONE
        }
    }

    private fun displayPosyandu(posyandu: Posyandu?) {
        if (posyandu != null) {
            val glideUrl = GlideUrl(
                BuildConfig.BASE_URL + "file/" + posyandu.foto,
                LazyHeaders.Builder().addHeader("Authorization", "Bearer $token").build()
            )
            Glide.with(this).load(glideUrl).into(binding.gambarPosyandu)

            binding.namaPosyandu.text = posyandu.nama
            binding.alamatPosyandu.text = posyandu.alamat
        } else {
            binding.namaPosyandu.text = "Tidak ada posyandu"
            binding.alamatPosyandu.visibility = View.GONE
        }
    }

    private fun displaySelfPemeriksaan(berisiko: Boolean?) {
        if (berisiko != null) {
            if (berisiko) {
                binding.tandaRisiko.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.error_cross
                    )
                )
                binding.textRisiko.text = "Anda berisiko melahirkan bayi stunting"
                binding.textRisiko.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.md_theme_light_error
                    )
                )
            } else {
                binding.tandaRisiko.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.done_checkmark
                    )
                )
                binding.textRisiko.text = "Anda tidak berisiko melahirkan bayi stunting"
                binding.textRisiko.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.success
                    )
                )
            }
        } else {
            binding.tandaRisiko.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity(), R.drawable.null_sign
                )
            )
            binding.textRisiko.text = "Anda tidak memiliki data pemeriksaan"
            binding.textRisiko.setTextColor(
                ContextCompat.getColor(
                    requireActivity(), R.color.md_theme_light_onSurface
                )
            )
        }
    }

    private fun displayDaftarKader(kaderCount: Int?) {
        if (kaderCount != null) {
            if (kaderCount > 0) {
                binding.cardKaderTitle.text = "$kaderCount kader terdaftar"
            } else {
                binding.cardKader.visibility = View.GONE
            }
        } else {
            binding.cardKader.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayDataRemaja() {
        displayPosyandu(viewModel.mainData.value?.remaja?.posyandu)
        displayJadwalPosyandu(
            viewModel.mainData.value?.sortedPosyandu()?.firstOrNull()?.waktuMulai
        )
        displayJadwalPenyuluhan(
            viewModel.mainData.value?.sortedPenyuluhan()?.firstOrNull()?.waktuMulai
        )
        displaySelfPemeriksaan(viewModel.mainPemeriksaanData.value?.firstOrNull()?.berisiko())
        displayDaftarKader(viewModel.mainKaderData.value?.count())

        binding.btnSettings.visibility = View.GONE
        binding.cardRemaja.visibility = View.GONE
        binding.btnInput.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayDataKader() {
        displayPosyandu(viewModel.mainData.value?.remaja?.posyandu)
        displayJadwalPosyandu(
            viewModel.mainData.value?.sortedPosyandu()?.firstOrNull()?.waktuMulai
        )
        displayJadwalPenyuluhan(
            viewModel.mainData.value?.sortedPenyuluhan()?.firstOrNull()?.waktuMulai
        )
        displayDaftarRemaja(viewModel.mainRemajaData.value)
        displaySelfPemeriksaan(viewModel.mainPemeriksaanData.value?.firstOrNull()?.berisiko())
        displayDaftarKader(viewModel.mainKaderData.value?.count())

        binding.btnSettings.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (role) {
            "bidan" -> {
                binding.btnSettings.setOnClickListener { v: View ->
                    showMenu(v, R.menu.posyandu_settings_menu)
                }

                binding.cardRemaja.setOnClickListener {
                    val intent = Intent(requireActivity(), DaftarRemajaActivity::class.java)
                    startNewActivity.launch(intent)
                }

                binding.btnInput.setOnClickListener {
                    val remajaList = viewModel.mainRemajaData.value
                    val newRemajaList = remajaList?.map { remaja ->
                        RemajaIdNama(remaja.id, remaja.user.nama)
                    }
                    val intent = Intent(requireActivity(), PemeriksaanCreateActivity::class.java)
                    intent.putExtra(
                        "remajaIdNamaList",
                        newRemajaList?.let { it1 -> ArrayList(it1) })

                    startNewActivity.launch(intent)
                }
            }

            "kader" -> {
                binding.cardRemaja.setOnClickListener {
                    val intent = Intent(requireActivity(), DaftarRemajaActivity::class.java)
                    startNewActivity.launch(intent)
                }

                binding.cardKader.setOnClickListener {
                    val intent = Intent(requireActivity(), DaftarKaderActivity::class.java)
                    startNewActivity.launch(intent)
                }

                binding.btnInput.setOnClickListener {
                    val remajaList = viewModel.mainRemajaData.value
                    var newRemajaList = remajaList?.map { remaja ->
                        RemajaIdNama(remaja.id, remaja.user.nama)
                    }

                    if (role == "kader") {
                        if (newRemajaList != null) {
                            newRemajaList = newRemajaList.filter {
                                it.id != currentRemajaId
                            }
                        }
                    }

                    val intent = Intent(requireActivity(), PemeriksaanCreateActivity::class.java)
                    intent.putExtra(
                        "remajaIdNamaList",
                        newRemajaList?.let { it1 -> ArrayList(it1) })

                    startNewActivity.launch(intent)
                }

                binding.cardRisiko.setOnClickListener {
                    val intent = Intent(requireActivity(), PemeriksaanActivity::class.java)
                    val remaja = viewModel.mainData.value?.remaja
                    val user = remaja?.user
                    intent.putExtra("nama", user?.nama)
                    user?.let { it1 -> intent.putExtra("nik", it1.nik) }
                    user?.let { it1 -> intent.putExtra("usia", it1.usia()) }
                    remaja?.let { it1 -> intent.putExtra("remajaId", it1.id) }
                    user?.let { it1 -> intent.putExtra("userId", it1.id) }
                    startNewActivity.launch(intent)
                }
            }

            "remaja" -> {
                binding.cardRisiko.setOnClickListener {
                    val intent = Intent(requireActivity(), PemeriksaanActivity::class.java)
                    val remaja = viewModel.mainData.value?.remaja
                    val user = remaja?.user
                    intent.putExtra("nama", user?.nama)
                    user?.let { it1 -> intent.putExtra("nik", it1.nik) }
                    user?.let { it1 -> intent.putExtra("usia", it1.usia()) }
                    remaja?.let { it1 -> intent.putExtra("remajaId", it1.id) }
                    user?.let { it1 -> intent.putExtra("userId", it1.id) }
                    startNewActivity.launch(intent)
                }

                binding.cardKader.setOnClickListener {
                    val intent = Intent(requireActivity(), DaftarKaderActivity::class.java)
                    startNewActivity.launch(intent)
                }
            }
        }

        binding.cardJadwalPosyandu.setOnClickListener {
            val intent = Intent(requireActivity(), JadwalPosyanduActivity::class.java)
            startNewActivity.launch(intent)
        }

        binding.cardJadwalPenyuluhan.setOnClickListener {
            val intent = Intent(requireActivity(), JadwalPenyuluhanActivity::class.java)
            startNewActivity.launch(intent)
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.pengaturan -> {
                    val intent = Intent(requireActivity(), PosyanduEditActivity::class.java)
                    startNewActivity.launch(intent)
                }

                R.id.ganti -> {
                    showPosyanduDialog()
                }
            }
            true
        }

        // Show the popup menu.
        popup.show()
    }

    private fun showPosyanduDialog() {
        val prefs = requireActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val token = prefs.getString("token", "no token")
        val bidanId = prefs.getInt("bidanId", 0)

        val client = ApiConfig.getApiService().getListPosyandu(bidanId, token = "Bearer $token")

        client.enqueue(object : Callback<ListPosyanduResponse> {
            override fun onResponse(
                call: Call<ListPosyanduResponse>, response: Response<ListPosyanduResponse>
            ) {
                if (response.isSuccessful) {
                    val posyandus = response.body()?.data

                    if (posyandus != null) {
                        val filteredList: List<PosyanduItem> = posyandus.filter { posyanduItem ->
                            !posyanduItem.active
                        }

                        if (filteredList.isNotEmpty()) {
                            val keysList: MutableList<Pair<String, Int>> = mutableListOf()
                            val valuesList: MutableList<String> = mutableListOf()

                            filteredList.forEachIndexed { _, posyanduItem ->
                                keysList.add(
                                    Pair(
                                        posyanduItem.posyandu.nama, posyanduItem.posyandu.id
                                    )
                                )
                                valuesList.add(posyanduItem.posyandu.nama)
                            }

                            val keysArray: Array<Pair<String, Int>> = keysList.toTypedArray()
                            val valuesArray: Array<String> = valuesList.toTypedArray()

                            MaterialAlertDialogBuilder(requireContext()).setTitle("Ganti Posyandu")
                                .setItems(valuesArray) { _, which ->
                                    val selectedPosyanduId = keysArray.get(which)
                                    selectedPosyanduId.let { updatePengampu(it) }
                                }.show()
                        } else {
                            // Show a Toast when filteredList is empty
                            Toast.makeText(
                                requireContext(), "Tidak ada posyandu", Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListPosyanduResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    // Add this function outside of the block where you're using MaterialAlertDialogBuilder
    private fun updatePengampu(selectedPosyanduId: Pair<String, Int>) {
        val pengampu = UpdatePengampuRequest(bidanId, true, selectedPosyanduId.second)
        val pengampuClient = ApiConfig.getApiService().updatePengampu("Bearer $token", pengampu)

        pengampuClient.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val newIntent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(newIntent)
                    requireActivity().finish()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}