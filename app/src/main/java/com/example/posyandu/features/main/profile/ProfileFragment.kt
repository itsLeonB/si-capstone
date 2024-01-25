package com.example.posyandu.features.main.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.posyandu.BuildConfig
import com.example.posyandu.R
import com.example.posyandu.databinding.FragmentPosyanduBinding
import com.example.posyandu.databinding.FragmentProfileBinding
import com.example.posyandu.features.main.MainActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileFragment : Fragment() {

    private lateinit var editNama: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editNIK: TextInputEditText
    private lateinit var editTTL: TextInputEditText
    private lateinit var editAlamat: TextInputEditText
    private lateinit var editProvinsi: AutoCompleteTextView
    private lateinit var editKota: AutoCompleteTextView
    private lateinit var editKecamatan: AutoCompleteTextView
    private lateinit var editKelurahan: AutoCompleteTextView
    private lateinit var editKodePos: TextInputEditText
    private lateinit var editRT: TextInputEditText
    private lateinit var editRW: TextInputEditText
    private lateinit var editNotelp: TextInputEditText
    private lateinit var BtnSaveChanges: MaterialButton
    private lateinit var BtnEditProfile: MaterialButton
    private var isEditMode = false
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding

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
    private fun refreshProfileData() {
        val nama = viewModel.profileData.value!!.nama
        val email = viewModel.profileData.value!!.email
        val nik = viewModel.profileData.value!!.nik.toString()
        val ttl = viewModel.profileData.value!!.tanggalLahir
        val alamat = viewModel.profileData.value!!.alamat
        val provinsi = viewModel.profileData.value!!.provinsi
        val kota = viewModel.profileData.value!!.kota
        val kecamatan = viewModel.profileData.value!!.kecamatan
        val kelurahan = viewModel.profileData.value!!.kelurahan
        val kodepos = viewModel.profileData.value!!.kodePos.toString()
        val rt = viewModel.profileData.value!!.rt.toString()
        val rw = viewModel.profileData.value!!.rw.toString()
        val notelp = viewModel.profileData.value!!.telepon.toString()

        binding.editNama.setText(nama)
        binding.editEmail.setText(email)
        binding.editNIK.setText(nik)
        binding.editTTL.setText(ttl)
        binding.editAlamat.setText(alamat)
        binding.editProvinsi.setText(provinsi)
        binding.editKota.setText(kota)
        binding.editKecamatan.setText(kecamatan)
        binding.editKelurahan.setText(kelurahan)
        binding.editKodePos.setText(kodepos)
        binding.editRT.setText(rt)
        binding.editRW.setText(rw)
        binding.editNotelp.setText(notelp)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel.profileData.observe(viewLifecycleOwner) {
            refreshProfileData()
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editNama = view.findViewById(R.id.editNama)
        editEmail = view.findViewById(R.id.editEmail)
        editNIK = view.findViewById(R.id.editNIK)
        editTTL = view.findViewById(R.id.editTTL)
        editAlamat = view.findViewById(R.id.editAlamat)
        editProvinsi = view.findViewById(R.id.editProvinsi)
        editKota = view.findViewById(R.id.editKota)
        editKecamatan = view.findViewById(R.id.editKecamatan)
        editKelurahan = view.findViewById(R.id.editKelurahan)
        editKodePos = view.findViewById(R.id.editKodePos)
        editRT = view.findViewById(R.id.editRT)
        editRW = view.findViewById(R.id.editRW)
        editNotelp = view.findViewById(R.id.editNotelp)
        BtnEditProfile = view.findViewById(R.id.buttonedit)
        BtnSaveChanges = view.findViewById(R.id.buttonSaveChanges)

        BtnEditProfile.setOnClickListener {
            toggleEditMode()
        }

        BtnSaveChanges.setOnClickListener {
            toggleEditMode()

            val updatedProfile = PutUpdateProfileRequest(
                nama = editNama.text.toString(),
                email = editEmail.text.toString(),
                alamat = editAlamat.text.toString(),
                provinsi = editProvinsi.text.toString(),
                kota = editKota.text.toString(),
                kecamatan = editKecamatan.text.toString(),
                kelurahan = editKelurahan.text.toString(),
                rt = (editRT.text.toString()).toInt(),
                rw = (editRW.text.toString()).toInt(),
                username = viewModel.profileData.value!!.username,
                kodePos = viewModel.profileData.value!!.kodePos,
                foto = viewModel.profileData.value!!.foto,
                telepon = editNotelp.text.toString()
            )

            viewModel.updateProfile(updatedProfile)

            val btmBar = activity?.findViewById<BottomNavigationView>(
                R.id.bottom_navigation
            )



            Snackbar.make(
                btmBar!!,
                "Profil berhasil diperbarui.",
                Snackbar.LENGTH_SHORT
            ).setAnchorView(btmBar)
                .show()
        }
    }



    private fun toggleEditMode() {
        isEditMode = !isEditMode

        if (isEditMode) {
            // Switch to "Cancel Edit" mode
            BtnEditProfile.text = "Cancel Edit"
            BtnEditProfile.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.md_theme_light_error
                )
            )
            BtnSaveChanges.visibility = View.VISIBLE

            // Enable EditText views
            editAlamat.isEnabled = true
            editProvinsi.isEnabled = true
            editKota.isEnabled = true
            editKecamatan.isEnabled = true
            editKelurahan.isEnabled = true
            editKodePos.isEnabled = true
            editRT.isEnabled = true
            editRW.isEnabled = true
            editNotelp.isEnabled = true

            // Enable other EditText views as needed
        } else {
            // Switch back to "Edit Profile" mode
            BtnEditProfile.text = "Edit Profile"
            BtnEditProfile.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.md_theme_light_primary
                )
            )
            BtnSaveChanges.visibility = View.GONE

            // Disable EditText views
            editAlamat.isEnabled = false
            editProvinsi.isEnabled = false
            editKota.isEnabled = false
            editKecamatan.isEnabled = false
            editKelurahan.isEnabled = false
            editKodePos.isEnabled = false
            editRT.isEnabled = false
            editRW.isEnabled = false
            editNotelp.isEnabled = false
            // Disable other EditText views as needed
        }
    }
}