package com.example.posyandu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class PemeriksaanEditFragment : Fragment() {
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
            R.layout.fragment_pemeriksaan_edit,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnTambah: Button = view.findViewById(R.id.btn_tambah)
        val btnDel: Button = view.findViewById(R.id.btn_del)

        btnTambah.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, PemeriksaanViewFragment())
                .addToBackStack(null)
                .commit()

            val btmBar = activity?.findViewById<BottomNavigationView>(
                R.id.bottom_navigation
            )

            Snackbar.make(
                btmBar!!,
                "Pemeriksaan berhasil disimpan",
                Snackbar.LENGTH_SHORT
            ).setAnchorView(btmBar)
                .show()
        }

        btnDel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Apakah anda yakin untuk menghapus pemeriksaan?")
                .setPositiveButton("Ya") { _, _ ->
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_container, PemeriksaanFragment())
                        .commit()

                    val btmBar =
                        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

                    Snackbar.make(btmBar!!, "Pemeriksaan berhasil dihapus", Snackbar.LENGTH_SHORT)
                        .setAnchorView(btmBar)
                        .show()
                }
                .setNegativeButton("Tidak") { _, _ ->
                }
                .show()

        }
    }
}