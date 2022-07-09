package com.sunny.careercouncilapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    lateinit var btnsub: Button
    lateinit var etemailf: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        btnsub = findViewById(R.id.submit)
        etemailf = findViewById(R.id.emailforgot)

        btnsub.setOnClickListener {
            val email= etemailf.text.toString().trim{it <= ' '}
            if (email.isEmpty())
            {
                Toast.makeText(applicationContext,"Please Enter Email Address",Toast.LENGTH_SHORT).show()
            }
            else
            {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->

                    if(task.isSuccessful)
                    {
                        Toast.makeText(applicationContext,"A reset Email has been sent to your Email ID",Toast.LENGTH_SHORT).show()
                        finish()

                    }
                    else
                    {
                        Toast.makeText(applicationContext,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()

                    }
                }
            }
        }


    }
}