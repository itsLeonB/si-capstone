package com.example.posyandu

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class DaftarRemajaEditFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_daftar_remaja_edit,
            container,
            false
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSimpan: Button = view.findViewById(R.id.btn_simpan)
        val btnDel: Button = view.findViewById(R.id.btn_del)

        btnSimpan.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, DaftarRemajaFragment())
                .commit()

            val btmBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

            Snackbar.make(btmBar!!, "Remaja berhasil diperbarui", Snackbar.LENGTH_SHORT)
                .setAnchorView(btmBar)
                .show()
        }

        btnDel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Apakah anda yakin untuk menghapus remaja?")
                .setPositiveButton("Ya") { _, _ ->
                    // Try to find an existing instance of DaftarRemajaFragment
                    val existingFragment = requireActivity().supportFragmentManager
                        .findFragmentByTag("DaftarRemajaFragment") as? DaftarRemajaFragment

                    // Use the existing instance if found, otherwise create a new one
                    val fragmentToUse = existingFragment ?: DaftarRemajaFragment()

                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_container, fragmentToUse, "DaftarRemajaFragment")
                        .commit()

                    val btmBar =
                        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

                    Snackbar.make(btmBar!!, "Remaja berhasil dihapus", Snackbar.LENGTH_SHORT)
                        .setAnchorView(btmBar)
                        .show()
                }
                .setNegativeButton("Tidak") { _, _ ->
                }
                .show()
        }
    }
}