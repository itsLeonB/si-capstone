package com.example.posyandu.features.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.posyandu.ApiClient
import com.example.posyandu.LoginRequest
import com.example.posyandu.LoginResponse
import com.example.posyandu.R
import com.example.posyandu.features.main.MainActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity() : AppCompatActivity(), Parcelable {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: MaterialButton

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views here
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            loginUser(username, password)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginActivity> {
        override fun createFromParcel(parcel: Parcel): LoginActivity {
            return LoginActivity(parcel)
        }

        override fun newArray(size: Int): Array<LoginActivity?> {
            return arrayOfNulls(size)
        }
    }

    private fun loginUser(username: String, password: String) {
        val requestModel = LoginRequest(username, password)
        val call = ApiClient.apiService.loginUser(requestModel)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val token = responseBody?.token
                    val sharedPreferences =
                        getSharedPreferences("Preferences", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("token", token)
                        apply()
                    }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Handle unsuccessful login
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed. Please check credentials.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network errors or other failures
                Toast.makeText(
                    this@LoginActivity,
                    "Login failed. Please check your internet connection.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
