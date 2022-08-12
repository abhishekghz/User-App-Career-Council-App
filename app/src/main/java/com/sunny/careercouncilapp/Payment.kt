package com.sunny.careercouncilapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import androidx.core.graphics.drawable.toBitmap
import java.nio.charset.StandardCharsets
import java.security.MessageDigest


class Payment : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private lateinit var uploadImage: ImageView
    private lateinit var email: String
    private lateinit var abc: usr_deets
    private lateinit var name: String
    private lateinit var tointent: String
    private lateinit var no: String
    private lateinit var flag1: String
    private lateinit var flag2: String

    lateinit var dwuri: Uri

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var fstore: FirebaseFirestore

    private lateinit var introcards: carddeets
    private lateinit var qr: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        flag1="false"
        flag2="false"

        qr=findViewById(R.id.qr)

        val dt = FirebaseFirestore.getInstance()

        val dr= dt.collection("cards").document("qr")
        dr.get().addOnSuccessListener { documentSnapshot ->
            introcards = documentSnapshot.toObject(carddeets::class.java)!!
            val url: String= introcards.img.toString()

            Picasso.get()
                .load(url)
                .into(qr);

            downloadqr()

        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
        }

        val src: String? = intent.getStringExtra("cname")
        val ct:String?=intent.getStringExtra("cardtitle")
        val code:String?=intent.getStringExtra("code")
        val link:String?=intent.getStringExtra("link")
        val paymsg: TextView = findViewById(R.id.paymentmssg)
        if (src != null) {
            tointent=src
        }else
        {
            tointent="!!Error!!"
        }
        paymsg.setText("You have enrolled for "+ src +" program. Upload the screenshot of payment via Google Pay of the specified amount.")
        fstore = FirebaseFirestore.getInstance()

        storageReference = FirebaseStorage.getInstance().reference
        val btn_choose_image: Button = findViewById(R.id.sel_img)
        val a: Button =findViewById(R.id.upload_img)


        val UserId = FirebaseAuth.getInstance().currentUser?.uid ?: "null"
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(UserId)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            abc = documentSnapshot.toObject(usr_deets::class.java)!!
            name= abc.Name.toString()
            no=abc.Mobno.toString()
            email=abc.Email.toString()
        }.addOnFailureListener { exception ->
            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
        }


        a.setOnClickListener {

            if(filePath != null) {


                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading")
                progressDialog.setMessage("Please wait while the screenshot is being uploaded")
                progressDialog.show()
                val ref = storageReference?.child("payments/" + UUID.randomUUID().toString())

                ref!!.putFile(filePath!!).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }.addOnCompleteListener {task->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        progressDialog.dismiss()

                        if(ct!=null && src!=null && link==null)
                        {
                            disp(downloadUri.toString(),src,ct)
                        }
                        if(ct!=null && src!=null && link!=null)
                        {
                            disp2(downloadUri.toString(),src,ct,link)
                        }


                    } else {
                        // Handle failures
                        progressDialog.dismiss()
                        disp("Hello",src+" Paid","hey")
                        // ...
                    }
                }


            }
            else{
                Toast.makeText(this, "Please Upload the Screenshot", Toast.LENGTH_SHORT).show()
            }
        }


        uploadImage= findViewById(R.id.screenshot)
        btn_choose_image.setOnClickListener { launchGallery() }
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                uploadImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun disp2(a:String,b: String,c:String,link:String)
    {


        val anh = hashMapOf("img" to a,
            "title" to c,
            "link" to link,
            "uname" to name,
            "uemail" to email,
            "uphone" to no,
            "created" to Timestamp.now(),
            "mssg" to "Please verify this user's payment and process the request")


        fstore.collection(b).document()
            .set(anh)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                flag1="true"
               }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e)
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()}

        fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(
            StandardCharsets.UTF_8
        ))
        fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
        val key = email+" "+Timestamp.now().toDate().toString()
        val id= md5(key).toHex()
        val pend = hashMapOf(
            "uname" to name,
            "uemail" to email,
            "uphone" to no,
            "created" to Timestamp.now(),
            "mssg" to "Paid Request!! Please verify the payment and process the request",
            "source" to c,
            "id" to id
        )

        fstore.collection("pending").document(id)
            .set(pend)
            .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                flag2="true"}
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }


        val intent = Intent(this, Receipt::class.java)
        intent.putExtra("usr",name)
        intent.putExtra("src",tointent)
        finish()
        startActivity(intent)




    }



    fun disp(a:String,b: String,c:String) {


        val anh = hashMapOf(
            "img" to a,
            "title" to c,

            "uname" to name,
            "uemail" to email,
            "uphone" to no,
            "created" to Timestamp.now(),
            "mssg" to "Please verify this user's payment and process the request"
        )


        fstore.collection(b).document()
            .set(anh)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                flag1 = "true"
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error writing document", e)
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }

        fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(
            str.toByteArray(
                StandardCharsets.UTF_8
            )
        )

        fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
        val key = email + " " + Timestamp.now().toDate().toString()
        val id = md5(key).toHex()
        val pend = hashMapOf(
            "uname" to name,
            "uemail" to email,
            "uphone" to no,
            "created" to Timestamp.now(),
            "mssg" to "Paid Request!! Please verify the payment and process the request",
            "source" to c,
            "id" to id
        )

        fstore.collection("pending").document(id)
            .set(pend)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                flag2 = "true"
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }


        val intent = Intent(this, Receipt::class.java)

        intent.putExtra("usr", name)
        intent.putExtra("src", tointent)
        finish()
        startActivity(intent)
    }


    private fun downloadqr()
    {
        val dqr: TextView = findViewById(R.id.downloadqr)
        dqr.setText("Click here to download the QR Code")

        dqr.setOnClickListener {
            val bm = qr.drawable.toBitmap()
            mSaveMediaToStorage(bm)
        }
    }


    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
    }


}