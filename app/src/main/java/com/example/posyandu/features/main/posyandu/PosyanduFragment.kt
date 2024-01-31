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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.posyandu.BuildConfig
import com.example.posyandu.R
import com.example.posyandu.databinding.FragmentPosyanduBinding
import com.example.posyandu.features.daftarRemaja.DaftarRemajaActivity
import com.example.posyandu.features.daftarRemaja.RemajaIdNama
import com.example.posyandu.features.jadwalPosyandu.JadwalPosyanduActivity
import com.example.posyandu.features.main.MainActivity
import com.example.posyandu.features.main.MainActivityViewModel
import com.example.posyandu.features.pemeriksaan.PemeriksaanCreateActivity
import com.example.posyandu.features.penyuluhan.JadwalPenyuluhanFragment
import com.example.posyandu.utils.ApiConfig
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.properties.Delegates

class PosyanduFragment : Fragment() {
    private lateinit var binding: FragmentPosyanduBinding
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>
    private lateinit var btmBar: BottomNavigationView
    private lateinit var prefs: SharedPreferences
    private lateinit var token: String
    private var bidanId by Delegates.notNull<Int>()
    private val viewModel: MainActivityViewModel by activityViewModels()

    companion object {
        private const val TAG = "PosyanduFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // in here you can do logic when backPress is clicked
                    parentFragmentManager.popBackStack()
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        // Refresh data when returning to the fragment
        viewModel.refreshData()
        displayPosyanduData()
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            // Handle the result as needed
            viewModel.refreshData()
            displayPosyanduData()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        btmBar = requireActivity().findViewById(R.id.bottom_navigation)

        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.refreshData()
                    displayPosyanduData()
                }
            }

        binding = FragmentPosyanduBinding.inflate(inflater, container, false)

        viewModel.mainBidanData.observe(viewLifecycleOwner) {
            displayPosyanduData()
        }

        viewModel.mainRemajaData.observe(viewLifecycleOwner) {
            displayPosyanduData()
        }

        prefs = requireActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        token = prefs.getString("token", "no token")!!
        bidanId = prefs.getInt("bidanId", 0)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayPosyanduData() {
        val waktuPosyandu =
            viewModel.mainBidanData.value?.sortedPosyandu()?.firstOrNull()?.waktuMulai
        val tanggalPosyandu = waktuPosyandu?.substring(0, 10)

        val format = SimpleDateFormat("yyyy-MM-dd", Locale("id"))
        val date = tanggalPosyandu?.let { format.parse(it) }

        val hariFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))
        val hari = date?.let { hariFormat.format(it) }

        val jamPosyandu = waktuPosyandu?.substring(11, 16)

        val remaja = viewModel.mainRemajaData.value
        val remajaCount = remaja?.count()
        val kaderCount = remaja?.count { it.isKader }
        val berisikoCount = remaja?.count { it.pemeriksaan.berisiko() && it.dataAvailable() }

        val linkFoto = viewModel.mainBidanData.value?.posyandu?.foto

        binding.namaPosyandu.text = viewModel.mainBidanData.value?.posyandu?.nama ?: "null"
        binding.alamatPosyandu.text = viewModel.mainBidanData.value?.posyandu?.alamat ?: "null"
        binding.tanggalJadwalPosyandu.text = hari ?: "Tidak ada jadwal"
        binding.jamJadwalPosyandu.text =
            jamPosyandu?.let { "Pukul $jamPosyandu WIB" } ?: "Tidak ada jadwal"
        binding.cardTitleRemaja.text =
            remajaCount?.let { "$remajaCount remaja terdaftar" } ?: "Tidak ada jadwal"
        binding.angkaKader.text = kaderCount.toString()
        binding.angkaRisiko.text = berisikoCount.toString()

        Glide.with(this).load(BuildConfig.BASE_URL + linkFoto)
            .into(binding.gambarPosyandu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSettings.setOnClickListener { v: View ->
            showMenu(v, R.menu.posyandu_settings_menu)
        }

        binding.cardJadwalPosyandu.setOnClickListener {
            val intent = Intent(requireActivity(), JadwalPosyanduActivity::class.java)
            startNewActivity.launch(intent)
        }

        binding.cardJadwalPenyuluhan.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, JadwalPenyuluhanFragment())
                .addToBackStack(null)
                .commit()
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
            intent.putExtra("remajaIdNamaList", newRemajaList?.let { it1 -> ArrayList(it1) })

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
                call: Call<ListPosyanduResponse>,
                response: Response<ListPosyanduResponse>
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
                                        posyanduItem.posyandu.nama,
                                        posyanduItem.posyandu.id
                                    )
                                )
                                valuesList.add(posyanduItem.posyandu.nama)
                            }

                            val keysArray: Array<Pair<String, Int>> = keysList.toTypedArray()
                            val valuesArray: Array<String> = valuesList.toTypedArray()

                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Ganti Posyandu")
                                .setItems(valuesArray) { _, which ->
                                    val selectedPosyanduId = keysArray.get(which)
                                    selectedPosyanduId.let { updatePengampu(it) }
                                }
                                .show()
                        } else {
                            // Show a Toast when filteredList is empty
                            Toast.makeText(
                                requireContext(),
                                "Tidak ada posyandu",
                                Toast.LENGTH_SHORT
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
        }
        )
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