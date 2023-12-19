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
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class DaftarRemajaFragment : Fragment() {
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
            R.layout.fragment_daftar_remaja,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kader: MaterialCardView = view.findViewById(R.id.kader)
        val remaja: MaterialCardView = view.findViewById(R.id.remaja)
        val btnTambah: Button = view.findViewById(R.id.btn_tambah)

        kader.setOnClickListener {
            val customView = LayoutInflater
                .from(requireContext())
                .inflate(R.layout.fragment_daftar_remaja_show_kader, null)

            val optProfil: MaterialCardView = customView.findViewById(R.id.opt_profil)
            val optPemeriksaan: MaterialCardView = customView.findViewById(R.id.opt_pemeriksaan)
            val optKader: MaterialCardView = customView.findViewById(R.id.opt_kader)

            val remajaDialog = MaterialAlertDialogBuilder(requireContext())
                .setView(customView)
                .show()

            optProfil.setOnClickListener {
                remajaDialog.cancel()
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, DaftarRemajaEditFragment())
                    .addToBackStack(null)
                    .commit()
            }

            optPemeriksaan.setOnClickListener {
                remajaDialog.cancel()
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, PemeriksaanFragment())
                    .addToBackStack(null)
                    .commit()
            }

            optKader.setOnClickListener {
                remajaDialog.cancel()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Anda yakin untuk mencabut akses kader?")
                    .setPositiveButton("Ya") { _, _ ->
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container, DaftarRemajaFragment())
                            .commit()

                        val btmBar =
                            activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

                        Snackbar.make(
                            btmBar!!,
                            "Akses kader berhasil dicabut",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAnchorView(btmBar)
                            .show()
                    }
                    .setNegativeButton("Tidak") { _, _ ->
                        remajaDialog.show()
                    }
                    .show()
            }
        }

        remaja.setOnClickListener {
            val customView = LayoutInflater
                .from(requireContext())
                .inflate(R.layout.fragment_daftar_remaja_show, null)

            val optProfil: MaterialCardView = customView.findViewById(R.id.opt_profil)
            val optPemeriksaan: MaterialCardView = customView.findViewById(R.id.opt_pemeriksaan)
            val optKader: MaterialCardView = customView.findViewById(R.id.opt_kader)

            val remajaDialog = MaterialAlertDialogBuilder(requireContext())
                .setView(customView)
                .show()

            optProfil.setOnClickListener {
                remajaDialog.cancel()
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, DaftarRemajaEditFragment())
                    .addToBackStack(null)
                    .commit()
            }

            optPemeriksaan.setOnClickListener {
                remajaDialog.cancel()
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, PemeriksaanFragment())
                    .addToBackStack(null)
                    .commit()
            }

            optKader.setOnClickListener {
                remajaDialog.cancel()
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Apakah anda yakin untuk menjadikan kader?")
                    .setPositiveButton("Ya") { _, _ ->
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container, DaftarRemajaFragment())
                            .commit()

                        val btmBar =
                            activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)

                        Snackbar.make(
                            btmBar!!,
                            "Akses kader berhasil diberikan",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAnchorView(btmBar)
                            .show()
                    }
                    .setNegativeButton("Tidak") { _, _ ->
                        remajaDialog.show()
                    }
                    .show()
            }
        }

        btnTambah.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, DaftarRemajaCreateFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}