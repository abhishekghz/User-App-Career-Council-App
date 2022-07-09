package com.sunny.careercouncilapp

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.squareup.picasso.Picasso
import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

class ResumeBuilding : AppCompatActivity() {
    private lateinit var uploadbtn: Button
    private lateinit var submitbtn: Button
    private lateinit var messageres: EditText
    private lateinit var email: String
    private lateinit var abc: usr_deets
    private lateinit var introcards: carddeets
    private lateinit var name: String
    private lateinit var url: String
    private lateinit var no: String

    private lateinit var introimage: ImageView

    lateinit var c: String
    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_building)
        uploadbtn = findViewById(R.id.upres)
        submitbtn = findViewById(R.id.resform)
        messageres = findViewById(R.id.message_res)
        messageres.setSelection(0)
        val dl: DrawerLayout = findViewById(R.id.drawer_layout)
        val nav: NavigationView = findViewById(R.id.navigation_view)
        val sub: Button = findViewById(R.id.submit_message)
        val nbtn: ImageView = findViewById(R.id.navresbtn)

        introimage=findViewById(R.id.img)

        val db = FirebaseFirestore.getInstance()
        val dr= db.collection("cards").document("resume")
        dr.get().addOnSuccessListener { documentSnapshot ->
            introcards = documentSnapshot.toObject(carddeets::class.java)!!
            url= introcards.img.toString()

            Picasso.get()
                .load(url)
                .into(introimage);

        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents: ", exception)
        }




        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle = ActionBarDrawerToggle(this, dl, R.string.open, R.string.close)
        var fstore: FirebaseFirestore = FirebaseFirestore.getInstance()

        dl.addDrawerListener(toggle)
        dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        val UserId = FirebaseAuth.getInstance().currentUser?.uid ?: "null"

        val docRef = db.collection("users").document(UserId)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            abc = documentSnapshot.toObject(usr_deets::class.java)!!
            name= abc.Name.toString()
            no=abc.Mobno.toString()
            email=abc.Email.toString()
        }.addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents: ", exception)
        }

        sub.setOnClickListener {
            val a = hashMapOf(
                "uname" to name,
                "uemail" to email,
                "uphone" to no,
                "created" to Timestamp.now(),
                "mssg" to messageres.text.toString()

            )

            fstore.collection("resque").document()
                .set(a)
                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

              Toast.makeText(this,"Message sent successfully",Toast.LENGTH_LONG).show()
            fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(UTF_8))
            fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
            val key = email+" "+Timestamp.now().toDate().toString()
            val id= md5(key).toHex()
            val b = hashMapOf(
                "uname" to name,
                "uemail" to email,
                "uphone" to no,
                "created" to Timestamp.now(),
                "mssg" to messageres.text.toString(),
                "source" to "Resume Enquiry",
            "id" to id

            )

            fstore.collection("pending").document(id)
                .set(b)
                .addOnSuccessListener {

                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")

                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }



        }


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

        uploadbtn.setOnClickListener {
            val intent = Intent(this, Res_upload::class.java)
            startActivity(intent)
        }

        submitbtn.setOnClickListener {
            val intent = Intent(this, Res_form::class.java)
            startActivity(intent)
        }


    }
}