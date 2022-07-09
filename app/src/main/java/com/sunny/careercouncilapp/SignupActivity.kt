package com.sunny.careercouncilapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var etConfPass: EditText
    lateinit var fname: EditText
    lateinit var mobno: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSignUp: Button

    lateinit var UserId: String
    lateinit var tvRedirectLogin: TextView
    private lateinit var fstore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        etEmail = findViewById(R.id.etSEmailAddress)
        etConfPass = findViewById(R.id.etSConfPassword)
        etPass = findViewById(R.id.etSPassword)
        fname = findViewById(R.id.Name)
        mobno = findViewById(R.id.mobno)
        btnSignUp = findViewById(R.id.btnSSigned)
        tvRedirectLogin = findViewById(R.id.tvRedirectLogin)
        fstore = FirebaseFirestore.getInstance()
        // Initialising auth object
        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            signUpUser()
        }

        // switching from signUp Activity to Login Activity
        tvRedirectLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signUpUser() {
        val email = etEmail.text.toString().trim()
        val pass = etPass.text.toString().trim()
        val confirmPassword = etConfPass.text.toString().trim()
        val full_name= fname.text.toString().trim()
        val mob_no= mobno.text.toString().trim()


        // check pass
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Oops! Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }
        if(full_name.isBlank())
        {
            Toast.makeText(this, "Oops! Name Cannot be left Blank", Toast.LENGTH_SHORT).show()
            return
        }
        if(mob_no.isBlank())
        {
            Toast.makeText(this, "Oops! Mobile Number Cannot be left Blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this, "Oops! Password Mismatch", Toast.LENGTH_SHORT)
                .show()
            return
        }
        // If all credential are correct
        // We call createUserWithEmailAndPassword
        // using auth object and pass the
        // email and pass in it.
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {


                UserId = FirebaseAuth.getInstance().currentUser?.uid ?: "null"

                val usr = hashMapOf(
                    "Name" to full_name,
                    "Email" to email,
                    "Mobno" to mob_no,
                "id" to UserId
                )
                fstore.collection("users").document(UserId)
                    .set(usr)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }


                Toast.makeText(this, "Successful Registration", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Dash::class.java)
                finish()
                startActivity(intent)
            } else {
                Toast.makeText(this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }
    }
}