package com.example.posyandu.features.main.home

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.example.posyandu.R
import com.example.posyandu.databinding.FragmentHomeBinding
import com.example.posyandu.features.daftarRemaja.DaftarRemajaActivity
import com.example.posyandu.features.main.posyandu.PosyanduFragment
import com.example.posyandu.utils.UserManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>
    private lateinit var btmBar: BottomNavigationView
    private val viewModel: HomeViewModel by viewModels()

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
    ): View {
        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    Snackbar.make(requireView(), snackbarMessage!!, Snackbar.LENGTH_SHORT)
                        .setAnchorView(btmBar)
                        .show()
                }
            }

        btmBar = requireActivity().findViewById(R.id.bottom_navigation)

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val cardChatTerbaru: MaterialCardView = view.findViewById(R.id.chatterbarubidan)

        binding.jadwalterdekatbidan.setOnClickListener { _: View ->
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, PosyanduFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.dashboardHomeBidan.setOnClickListener { v: View ->
            val intent = Intent(requireActivity(), DaftarRemajaActivity::class.java)
            startNewActivity.launch(intent)
        }

        val userManager = UserManager.getInstance(requireActivity().application)
        val userRole = userManager.getRole()

        // Now you can use userRole as needed
        if (userRole == "kader") {
            binding.dashboardHomeBidan.visibility = View.GONE
        } else if (userRole == "default") {
            // Display regular user UI features

            viewModel.refreshHomeData()

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
}