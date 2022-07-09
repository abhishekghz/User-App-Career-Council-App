package com.sunny.careercouncilapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Receipt : AppCompatActivity() {
    private lateinit var rec:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        rec=findViewById(R.id.rec)

        val a: String? = intent.getStringExtra("usr")
        val b: String?= intent.getStringExtra("src")
        if (a != null&& b!=null) {
            disp(a,b)
        }



    }
    fun disp(a:String,b:String)
    {
        rec.setText("Dear "+a+",\n\n"+"Thank You for choosing The Career Council. Assisting you is our priority.\n\nWe have received the proof of payment that you have uploaded. Our records show that you have enrolled for the "+b+" program\nWe are processing this request and will get back to you shortly.\n\nWrite to us at: \ninfo@thecareercouncil.com to know more about us and the different services we offer.\n\n\nTeam Career Council")
    }
}