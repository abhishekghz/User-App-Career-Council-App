package com.sunny.careercouncilapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class campustocorporate : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var introcards: carddeets
    private lateinit var introimage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campustocorporate)
        val dl : DrawerLayout = findViewById(R.id.drawer_layout)
        val nav: NavigationView = findViewById(R.id.navigation_view)

        val nbtn: ImageView = findViewById(R.id.ctcnavbtn)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle = ActionBarDrawerToggle(this, dl, R.string.open, R.string.close )

        introimage=findViewById(R.id.imgcampus)

        val db = FirebaseFirestore.getInstance()
        val btn : Button = findViewById(R.id.ctcpaybtn)

        btn.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            intent.putExtra("code",8)
            intent.putExtra("cname","Campus to Corporate - Paid")
            intent.putExtra("cardtitle","Campus to Corporate - Paid!!")
            startActivity(intent)
        }


        val dr= db.collection("cards").document("campus")
        dr.get().addOnSuccessListener { documentSnapshot ->
            introcards = documentSnapshot.toObject(carddeets::class.java)!!
            val url: String= introcards.img.toString()

            Picasso.get()
                .load(url)
                .into(introimage);

        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
        }



        dl.addDrawerListener(toggle)
        dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        nav.setNavigationItemSelectedListener {

            if(it.itemId == R.id.chatnav)
            {
                val intent = Intent(this, ChatwithCounselors::class.java)
                startActivity(intent)
                true
            }
            else if(it.itemId==R.id.resbnav)
            {
                val intent = Intent(this, ResumeBuilding::class.java)
                startActivity(intent)
                true
            }
            else if(it.itemId==R.id.jobapplinav)
            {
                val intent = Intent(this, jobapplication::class.java)
                startActivity(intent)
                true
            }
            else if(it.itemId==R.id.opbnav)
            {
                val intent = Intent(this, onlineprofile::class.java)
                startActivity(intent)
                true
            }
            else if(it.itemId==R.id.workshopnav)
            {
                val intent = Intent(this, Workshops::class.java)
                startActivity(intent)
                true
            }
            else if(it.itemId==R.id.careerguinav)
            {
                val intent = Intent(this, Customized_Career_Guidance::class.java)
                startActivity(intent)
                true
            }
            else if(it.itemId==R.id.ctcguinav)
            {
                val intent = Intent(this, campustocorporate::class.java)
                startActivity(intent)
                true
            }
            else if(it.itemId==R.id.newsnav)
            {
                val intent = Intent(this, Newsfeed::class.java)
                startActivity(intent)
                true
            }
            else if(it.itemId==R.id.logout)
            {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            else
            {
                true

            }

        }



        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_list_24)
        toggle.syncState()

        nbtn.setOnClickListener{
            dl.openDrawer(nav)
        }
    }
}