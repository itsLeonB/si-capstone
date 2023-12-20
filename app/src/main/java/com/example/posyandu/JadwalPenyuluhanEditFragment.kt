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
import com.google.android.material.snackbar.Snackbar

class JadwalPenyuluhanEditFragment : Fragment() {
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
            R.layout.fragment_jadwal_penyuluhan_edit,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val simpan: Button = view.findViewById(R.id.btn_simpan)
        val del: Button = view.findViewById(R.id.btn_del)
        val btmBar = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)


        simpan.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, JadwalPenyuluhanViewFragment())
                .commit()
            Snackbar.make(btmBar!!, "Jadwal berhasil dibuat", Snackbar.LENGTH_SHORT)
                .setAnchorView(btmBar)
                .show()
        }
        del.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, JadwalPenyuluhanFragment())
                .commit()
            Snackbar.make(btmBar!!, "Jadwal berhasil dihapus", Snackbar.LENGTH_SHORT)
                .setAnchorView(btmBar)
                .show()
        }
    }
}