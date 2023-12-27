package com.example.posyandu

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.posyandu.databinding.FragmentPosyanduBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class PosyanduFragment : Fragment() {
    private var selectedPosyandu: String? = null
    private lateinit var binding: FragmentPosyanduBinding
    private lateinit var startNewActivity: ActivityResultLauncher<Intent>
    private lateinit var btmBar: BottomNavigationView
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
    ): View {
        btmBar = requireActivity().findViewById(R.id.bottom_navigation)

        startNewActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val snackbarMessage = result.data?.getStringExtra("snackbar_message")
                    Snackbar.make(requireView(), snackbarMessage!!, Snackbar.LENGTH_SHORT)
                        .setAnchorView(btmBar)
                        .show()
                }
            }

        selectedPosyandu = activity?.intent?.getStringExtra("selectedPosyandu")
        binding = FragmentPosyanduBinding.inflate(inflater, container, false)
        if (selectedPosyandu != null) {
            binding.namaPosyandu.text = selectedPosyandu
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSettings.setOnClickListener { v: View ->
            showMenu(v, R.menu.posyandu_settings_menu)
        }

        binding.cardJadwalPosyandu.setOnClickListener {
            val intent = Intent(requireActivity(), JadwalPosyanduActivity::class.java)
            startNewActivity.launch(intent)
        }

        binding.cardJadwalPenyuluhan.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, JadwalPenyuluhanFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.cardRemaja.setOnClickListener {
            val intent = Intent(requireActivity(), DaftarRemajaActivity::class.java)
            startNewActivity.launch(intent)
        }

        binding.btnInput.setOnClickListener {
            val intent = Intent(requireActivity(), PemeriksaanCreateActivity::class.java)
            startNewActivity.launch(intent)
        }

    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.pengaturan -> {
                    val intent = Intent(requireActivity(), PosyanduEditActivity::class.java)
                    startNewActivity.launch(intent)
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
            .setItems(items) { _, which ->
                val selectedPosyandu = items[which]
                val newIntent = Intent(requireActivity(), MainActivity::class.java)

                // Pass the selectedPosyandu as an extra to the new MainActivity
                newIntent.putExtra("selectedPosyandu", selectedPosyandu)

                startActivity(newIntent)

                // If needed, you can finish the current activity
                requireActivity().finish()
            }
            .show()
    }
}