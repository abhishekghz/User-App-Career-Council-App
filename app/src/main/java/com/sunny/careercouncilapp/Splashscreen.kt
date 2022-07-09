package com.sunny.careercouncilapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.google.firebase.auth.*

class Splashscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val mUser = FirebaseAuth.getInstance().currentUser
            if (mUser!=null)
            {
                val intent = Intent(this, Dash::class.java)
                startActivity(intent)
            }
            else
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1600)
    }
}