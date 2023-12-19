package com.example.posyandu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val userRole = "bidan"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.home
        bottomNav.setOnItemSelectedListener(navListener)

        // as soon as the application opens the first fragment should
        // be shown to the user in this case it is algorithm fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment()).commit()
    }

    private val navListener = NavigationBarView.OnItemSelectedListener {
        // By using switch we can easily get the
        // selected fragment by using there id
        lateinit var selectedFragment: Fragment
        when (it.itemId) {
            R.id.home -> {
                selectedFragment = HomeFragment()
            }

            R.id.konsultasi -> {
                selectedFragment = KonsultasiFragment()
            }

            R.id.posyandu -> {
                selectedFragment = PosyanduFragment()
            }

            R.id.profile -> {
                selectedFragment = ProfileFragment()
            }
        }
        // It will help to replace the
        // one fragment to other.
//        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment)
//            .commit()
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.fragment_container, selectedFragment)
            .addToBackStack(null)
            .commit()
        true
    }
}