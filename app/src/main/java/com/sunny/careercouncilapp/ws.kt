package com.sunny.careercouncilapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ws : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ws)
        var vw: Button = findViewById(R.id.vw)
        vw.setOnClickListener {
            val intent = Intent(this, Workshops::class.java)
            startActivity(intent)
        }
    }
}