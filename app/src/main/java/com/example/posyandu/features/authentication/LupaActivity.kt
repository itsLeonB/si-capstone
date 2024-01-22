package com.example.posyandu.features.authentication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.posyandu.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

class LupaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lupa)

        val toggleButton = findViewById<MaterialButtonToggleGroup>(R.id.toggleButton)
        val defaultCheckedButtonId =
            R.id.button1 // Replace with the ID of the button you want to be checked by default

        // Set the default checked state
        toggleButton.check(defaultCheckedButtonId)

        toggleButton.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val checkedButton: MaterialButton? = group.findViewById(checkedId)

            // Uncheck all buttons except the checked one
            for (button in group.children) {
                if (button != checkedButton && button is MaterialButton) {
                    button.isChecked = false

                }
            }
        }
    }

    fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}