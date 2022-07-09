package com.sunny.careercouncilapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Big_five_quiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_five_quiz)
        val sbquiz: Button = findViewById(R.id.sbquiz)
        sbquiz.setOnClickListener {
            val intent = Intent(this, Quiz::class.java)
            startActivity(intent) }
    }
}