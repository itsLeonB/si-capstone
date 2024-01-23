package com.example.posyandu.features.main.posyandu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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
import com.example.posyandu.DaftarRemajaActivity
import com.example.posyandu.JadwalPenyuluhanFragment
import com.example.posyandu.R
import com.example.posyandu.databinding.FragmentPosyanduBinding
import com.example.posyandu.features.jadwalPosyandu.JadwalPosyanduActivity
import com.example.posyandu.features.main.MainActivity
import com.example.posyandu.features.main.MainActivityViewModel
import com.example.posyandu.features.pemeriksaan.PemeriksaanCreateActivity
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        btmBar = requireActivity().findViewById(R.id.bottom_navigation)

        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                viewModel.refreshData()
                if (result.resultCode == Activity.RESULT_OK) {
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    Snackbar.make(requireView(), snackbarMessage!!, Snackbar.LENGTH_SHORT)
                        .setAnchorView(btmBar)
                        .show()
                }
            }

        binding = FragmentPosyanduBinding.inflate(inflater, container, false)

        viewModel.mainBidanData.observe(viewLifecycleOwner) {
            displayPosyanduData()
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayPosyanduData() {
        val waktuPosyandu = viewModel.mainBidanData.value!!.jadwalPosyandu.first().waktuMulai
        val tanggalPosyandu = waktuPosyandu.substring(0, 10)

        val format = SimpleDateFormat("yyyy-MM-dd", Locale("id"))
        val date = format.parse(tanggalPosyandu)

        val hariFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))
        val hari = hariFormat.format(date!!)

        val jamPosyandu = waktuPosyandu.substring(11, 16)

        val remaja = viewModel.mainRemajaData.value!!
        val remajaCount = remaja.count()
        val kaderCount = remaja.count { it.isKader }
        val berisikoCount = remaja.count { it.berisiko }

        val linkFoto = viewModel.mainBidanData.value!!.posyandu.foto

        binding.namaPosyandu.text = viewModel.mainBidanData.value!!.posyandu.nama
        binding.alamatPosyandu.text = viewModel.mainBidanData.value!!.posyandu.alamat
        binding.tanggalJadwalPosyandu.text = hari
        binding.jamJadwalPosyandu.text = "Pukul $jamPosyandu WIB"
        binding.cardTitleRemaja.text = "$remajaCount remaja terdaftar"
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
            val intent = Intent(requireActivity(), PemeriksaanCreateActivity::class.java)
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

        val client = ApiConfig.getApiService().getListPosyandu(1, token = "Bearer $token")

        client.enqueue(object : Callback<ListPosyanduResponse> {
            override fun onResponse(
                call: Call<ListPosyanduResponse>,
                response: Response<ListPosyanduResponse>
            ) {
                if (response.isSuccessful) {
                    val posyandus = response.body()?.data

                    val filteredList: List<PosyanduItem> = posyandus!!.filter { posyanduItem ->
                        !posyanduItem.active
                    }

                    // First array: keys (posyanduData nama and posyanduData id)
                    val keysArray: Array<Pair<String, Int>> = filteredList.map { posyanduItem ->
                        Pair(posyanduItem.posyandu.nama, posyanduItem.posyandu.id)
                    }.toTypedArray()

                    // Second array: values (posyanduData nama)
                    val valuesArray: Array<String> = filteredList.map { posyanduItem ->
                        posyanduItem.posyandu.nama
                    }.toTypedArray()

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Ganti Posyandu")
                        .setItems(valuesArray) { _, which ->
                            val selectedPosyanduId = keysArray[which]

                            val pengampu = UpdatePengampuRequest(
                                1,
                                true,
                                selectedPosyanduId.second
                            )

                            val pengampuClient = ApiConfig.getApiService().updatePengampu(
                                token = "Bearer $token",
                                pengampu
                            )

                            pengampuClient.enqueue(object : Callback<Void> {
                                override fun onResponse(
                                    call: Call<Void>,
                                    response: Response<Void>
                                ) {
                                    if (response.isSuccessful) {
                                        val newIntent =
                                            Intent(requireActivity(), MainActivity::class.java)
                                        startActivity(newIntent)
                                        requireActivity().finish()
                                    } else {
                                        Log.e(TAG, "onFailure: ${response.message()}")
                                    }
                                }

                                override fun onFailure(
                                    call: Call<Void>,
                                    t: Throwable
                                ) {
                                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                                }
                            }
                            )
                        }
                        .show()
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
}