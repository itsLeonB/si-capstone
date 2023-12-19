package com.example.posyandu

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar

class PosyanduFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(
            this,
            object : OnBackPressedCallback(true) {
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
        return inflater.inflate(
            R.layout.fragment_posyandu,
            container,
            false
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSettings: Button = view.findViewById(R.id.btn_settings)
        val cardJadwalPosyandu: MaterialCardView = view.findViewById(R.id.card_jadwal_posyandu)
        val cardRemaja: MaterialCardView = view.findViewById(R.id.card_remaja)
        val btnInput: ExtendedFloatingActionButton = view.findViewById(R.id.btn_input)

        btnSettings.setOnClickListener { v: View ->
            showMenu(v, R.menu.posyandu_settings_menu)
        }

        cardJadwalPosyandu.setOnClickListener {
//            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.fragment_container, JadwalPosyanduFragment())
//            fragmentTransaction.addToBackStack(null) // Add transaction to back stack
//            fragmentTransaction.commit()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, JadwalPosyanduFragment())
                .addToBackStack(null)
                .commit()
        }

        cardRemaja.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, DaftarRemajaFragment())
                .addToBackStack(null)
                .commit()
        }

        btnInput.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, PemeriksaanCreateFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.pengaturan -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, PosyanduEditFragment())
                        .commit()
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
        val items = arrayOf("Posyandu Terhebat", "Posyandu Terkuat", "Posyandu Terbaik")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Ganti Posyandu")
            .setItems(items) { _, _ ->
                val fragmentTransaction =
                    requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, PosyanduFragment())
                fragmentTransaction.addToBackStack(null) // Add transaction to back stack
                fragmentTransaction.commit()

                val btmBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

                Snackbar.make(btmBar!!, "Berhasil ganti Posyandu", Snackbar.LENGTH_SHORT)
                    .setAnchorView(btmBar)
                    .show()
            }
            .show()
    }
}