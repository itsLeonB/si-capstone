package com.example.posyandu.features.main.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.posyandu.features.main.posyandu.PosyanduFragment
import com.example.posyandu.R
import com.google.android.material.card.MaterialCardView

class HomeFragmentBidan : Fragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardJadwalTerdekat: MaterialCardView = view.findViewById(R.id.jadwalterdekatbidan)
        val cardDashboard: MaterialCardView = view.findViewById(R.id.dashboardHomeBidan)
//        val cardChatTerbaru: MaterialCardView = view.findViewById(R.id.chatterbarubidan)

        cardJadwalTerdekat.setOnClickListener { v: View ->
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, PosyanduFragment())
                .addToBackStack(null)
                .commit()
        }

//        cardDashboard.setOnClickListener { v: View ->
//            requireActivity().supportFragmentManager
//                .beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .replace(R.id.container, DaftarRemajaFragment())
//                .addToBackStack(null)
//                .commit()
//        }

//        cardChatTerbaru.setOnClickListener { v: View ->
//            requireActivity().supportFragmentManager
//                .beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .replace(R.id.fragment_container, ChatFragment())
//                .addToBackStack(null)
//                .commit()
//        }


    }

}