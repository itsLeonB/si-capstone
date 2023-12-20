package com.example.posyandu

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                parentFragmentManager.popBackStack()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
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