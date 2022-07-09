package com.sunny.careercouncilapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class Newsfeed : AppCompatActivity() {


    private lateinit var rv: RecyclerView

    private lateinit var nslist: ArrayList<News_data_class>
    private lateinit var myAdapter: NewsAdapter
    private lateinit var db: FirebaseFirestore

    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newsfeed)
        val dl : DrawerLayout = findViewById(R.id.drawer_layout)
        val nav: NavigationView = findViewById(R.id.navigation_view)

        val nbtn: ImageView = findViewById(R.id.newsnavbtn)

        rv = findViewById(R.id.nsrecv)

        rv.layoutManager = GridLayoutManager(this,1)
        rv.setHasFixedSize(false)

        nslist = arrayListOf()

        myAdapter = NewsAdapter( nslist)
        rv.adapter = myAdapter


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        toggle = ActionBarDrawerToggle(this, dl, R.string.open, R.string.close )

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

        EventchangeListener()
    }

    private fun EventchangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("news").orderBy("created",Query.Direction.DESCENDING).
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if(error!= null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }

                for (dc: DocumentChange in value?.documentChanges!!)
                {
                    if(dc.type== DocumentChange.Type.ADDED){
                        nslist.add(dc.document.toObject(News_data_class::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }
        }
        )
    }
}