package com.example.posyandu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_bidan)


        // Your initialization code goes here

        // Example: Accessing a view from the layout
        // val myTextView = findViewById<TextView>(R.id.my_text_view)
        // myTextView.text = "Hello, Kotlin!"

        // Example: Setting up a click listener for a button
        // val myButton = findViewById<Button>(R.id.my_button)
        // myButton.setOnClickListener {
        //     // Handle button click
        // }
    }

    // Add other lifecycle methods and functions as needed

}
