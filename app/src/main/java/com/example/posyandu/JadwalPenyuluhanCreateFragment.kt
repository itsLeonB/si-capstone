package com.example.posyandu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class JadwalPenyuluhanCreateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_jadwal_penyuluhan_create,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // in here you can do logic when backPress is clicked
                    parentFragmentManager.popBackStack()
                }
            }
        )
        val btmBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val btnTambah: FloatingActionButton = view.findViewById(R.id.btn_tambah)

        btnTambah.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, JadwalPenyuluhanViewFragment())
                .addToBackStack(null)
                .commit()
            btmBar?.let { Snackbar.make(it, "Jadwal berhasil dibuat", Snackbar.LENGTH_SHORT)
                .setAnchorView(btmBar)
                .show()}
        }

    }
}