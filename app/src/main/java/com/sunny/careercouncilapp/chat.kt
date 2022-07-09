package com.sunny.careercouncilapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Response

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class chat : AppCompatActivity() {
    private lateinit var sendbtn: ImageView
    private lateinit var sendmessage: EditText

    private lateinit var rv: RecyclerView
    private lateinit var messagelist: ArrayList<Message>
    private lateinit var myAdapter: MessageAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var email: String
    private lateinit var abc: usr_deets

    private lateinit var msg: String
    private lateinit var name: String

    private lateinit var no: String

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAZxzPKPM:APA91bHuYRUhELS_4utoa-lZeROXKU0BX1XVe4Y_pgd6Hsu-eYjDkhG0uSQISddSe5fWVWy2U4zC1BBmY1EVokWa7eW6WFsRLO-YcZ0iVnHCieGce8ALUqMVNuYE7GnrBo4PuwEu0it2"
    private val contentType = "application/json"

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        sendbtn = findViewById(R.id.send_btn)
        sendmessage = findViewById(R.id.message)

        var fstore: FirebaseFirestore = FirebaseFirestore.getInstance()

        val uid=FirebaseAuth.getInstance().currentUser?.uid ?: "null"

        val UserId = FirebaseAuth.getInstance().currentUser?.uid ?: "null"
        val dba = FirebaseFirestore.getInstance()
        val docRef = dba.collection("users").document(UserId)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            abc = documentSnapshot.toObject(usr_deets::class.java)!!
            name= abc.Name.toString()
            no=abc.Mobno.toString()
            email=abc.Email.toString()
            initialize(uid)

        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
        }





        rv = findViewById(R.id.mssg_rec)

        rv.layoutManager = GridLayoutManager(this,1)

        rv.setHasFixedSize(false)

        messagelist = arrayListOf()

        myAdapter = MessageAdapter(messagelist)
        rv.adapter = myAdapter



        sendbtn.setOnClickListener {

            val a = hashMapOf(
                "message" to sendmessage.text.toString(),
                "senderId" to uid,
            "time" to Timestamp.now()

            )
            val pms = hashMapOf(
                "name" to name

            )
            val docname=name+" ("+email+")"
            fstore.collection("pendingmessages").document(docname).set(pms)
                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }


            fstore.collection(uid).document()
                .set(a)
                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
            sendmessage.setText("")

            fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(
                StandardCharsets.UTF_8
            ))
            fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
            val key = email+" "+Timestamp.now().toDate().toString()
            val id= md5(key).toHex()
            val b = hashMapOf(
                "uname" to name,
                "uemail" to email,
                "uphone" to no,
                "created" to Timestamp.now(),
                "mssg" to "New Chat Message from this user",
                "source" to "CHAT (In App)",
                "id" to id

            )

            fstore.collection("pending").document(id)
                .set(b)
                .addOnSuccessListener {

                    Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")

                }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

            if (msg.isNotEmpty()) {
                val topic = "/topics/admin" //topic has to match what the receiver subscribed to

                val notification = JSONObject()
                val notifcationBody = JSONObject()

                try {
                    notifcationBody.put("title", "Chat")
                    notifcationBody.put("message", msg)   //Enter your notification message
                    notification.put("to", topic)
                    notification.put("data", notifcationBody)
                    Log.e("TAG", "try")
                } catch (e: JSONException) {
                    Log.e("TAG", "onCreate: " + e.message)
                }

                sendNotification(notification)
            }



        }
        EventchangeListener(uid)
    }
    private fun initialize(uid:String)
    {
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+uid)

        msg="New Message from "+name

    }
    private fun EventchangeListener(uid: String) {
        db = FirebaseFirestore.getInstance()
        db.collection(uid).orderBy("time",Query.Direction.ASCENDING).
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
                        messagelist.add(dc.document.toObject(Message::class.java))
                    }
                }
                if(myAdapter.itemCount!=0) {
                    val pos = myAdapter.itemCount - 1
                    rv.smoothScrollToPosition(pos)
                }
                myAdapter.notifyDataSetChanged()
            }
        }
        )
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(
            FCM_API,
            notification,
            Response.Listener<JSONObject> { response ->
                Log.i("TAG", "onResponse: $response")

            },
            Response.ErrorListener {
                Toast.makeText(this, "Request error", Toast.LENGTH_LONG).show()
                Log.i("TAG", "onErrorResponse: Didn't work")
            }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }
}