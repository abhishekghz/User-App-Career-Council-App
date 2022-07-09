package com.sunny.careercouncilapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dash.*
import me.relex.circleindicator.CircleIndicator3
import android.content.ContentValues
import android.os.Handler
import android.util.Log
import java.util.*
import android.view.animation.Animation

import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class Dash : AppCompatActivity() {
    private var cardlist = mutableListOf<Int>()
    private lateinit var myAdapter: ViewPagerAdapter
    private lateinit var dispemail: TextView
    private lateinit var handler: Handler
    private lateinit var alertmes: TextView
    lateinit var toggle : ActionBarDrawerToggle

    override fun onResume() {
        super.onResume()
        newmessage()


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)

        newmessage()

        dispemail= findViewById(R.id.dispemail)
        alertmes= findViewById(R.id.almes)
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500 //You can manage the blinking time with this parameter

        anim.startOffset = 20
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        alertmes.startAnimation(anim)

        postToList()
        val dl : DrawerLayout = findViewById(R.id.drawer_layout)
        val nav: NavigationView = findViewById(R.id.navigation_view)
        handler= Handler()
       val nbtn: ImageView= findViewById(R.id.navbtn)




        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle = ActionBarDrawerToggle(this, dl, R.string.open, R.string.close )

        dl.addDrawerListener(toggle)
        dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)



        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_list_24)
        toggle.syncState()

        nbtn.setOnClickListener{
            dl.openDrawer(nav)
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url

            val email = user.email

            dispemail.text = email

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
               val intent = Intent(this, ws::class.java)
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
               finish()
               startActivity(intent)
               true
           }
           else
           {
               true

           }

        }

        myAdapter = ViewPagerAdapter(cardlist)
        vp.adapter = myAdapter
        vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        var page=0


        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(vp)

    }



    private fun addToList(card: Int){
        cardlist.add(card)
    }

    private fun postToList()
    {
        addToList(R.drawable.chatdashcard)
        addToList(R.drawable.resdashcard)
        addToList(R.drawable.jobapplidashcard)
        addToList(R.drawable.linkedin)

        addToList(R.drawable.careerguidashcard)
        addToList(R.drawable.campusdashcard)
        addToList(R.drawable.workshopdashcard)
        addToList(R.drawable.newsdashcard)


    }

    fun newmessage()
    { val user = FirebaseAuth.getInstance().currentUser
        var email = ""
        user?.let {
            // Name, email address, and profile photo Url

            email = user.email.toString()

        }

        val firestore= FirebaseFirestore.getInstance()
        firestore.collection(email).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    val list: MutableList<String> = ArrayList()
                    for (document in task.result) {
                        list.add(document.id)
                        list.add("\n")
                    }
                    if(!list.isEmpty())
                    {var names= StringBuffer()
                        for(i in list)
                        {
                            names.append(i)
                        }
                        calldialog(email)
                        Log.d(ContentValues.TAG, list.toString())}
                    else
                    {
                        calldia()
                    }
                } else {
                    calldia()
                }
            })
    }

    fun calldialog(email: String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Message")
        builder.setMessage("You have new unread message(s) in the Chat Room.")

        builder.setPositiveButton("DISMISS"){dialog, which ->

            val firestore= FirebaseFirestore.getInstance()
            firestore.collection(email).get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                    if (task.isSuccessful) {

                        for (document in task.result) {
                            firestore.collection(email).document(document.id)
                                .delete()
                                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully deleted!") }
                                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error deleting document", e) }
                        }
                        dialog.dismiss()

                    } else {
                        Log.d(ContentValues.TAG, "Error getting documents: ", task.exception)
                    }
                })

        }
        builder.show()
    }
    fun calldia()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No new Messages")
        builder.setMessage("You have no new chat messages.")
        builder.setPositiveButton("OK"){dialog, which ->
            dialog.dismiss()
        }


    }
}