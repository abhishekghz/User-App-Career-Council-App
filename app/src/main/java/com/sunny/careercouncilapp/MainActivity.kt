package com.sunny.careercouncilapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var tvRedirectSignUp: TextView
    private lateinit var forgotpwd: TextView
    lateinit var etEmail: EditText
    private lateinit var etPass: TextInputEditText
    lateinit var btnLogin: Button


    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvRedirectSignUp = findViewById(R.id.tvRedirectSignUp)
        btnLogin = findViewById(R.id.btnLogin)
        etEmail = findViewById(R.id.etEmailAddress)
        etPass = findViewById(R.id.etPassword)
        forgotpwd = findViewById(R.id.forgotpwd)

        // initialising Firebase auth object
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            login()
        }
        forgotpwd.setOnClickListener {

            val intent = Intent(this, ForgotPassword::class.java)

            startActivity(intent)
        }
        tvRedirectSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            finish()
            startActivity(intent)
            // using finish() to end the activity

        }
    }

    private fun login() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        // calling signInWithEmailAndPassword(email, pass)
        // function using Firebase auth object
        // On successful response Display a Toast
        if(email!="" && pass!="") {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
                if (it.isSuccessful) {

                    val intent = Intent(this, Dash::class.java)
                    finish()
                    startActivity(intent)
                } else
                    Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this, "Email ID and Password cannot be left blank", Toast.LENGTH_SHORT).show()
        }
    }
}