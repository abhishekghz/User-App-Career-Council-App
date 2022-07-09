package com.sunny.careercouncilapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class sub_job : AppCompatActivity() {
    private lateinit var uploadbtn: Button
    private lateinit var tp: Button
    private val PDF = 23
    lateinit var uri: Uri
    lateinit var link:String
    private var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_job)

        storageReference = FirebaseStorage.getInstance().reference
        link="Not assigned"
        uploadbtn = findViewById(R.id.ubj)
        tp = findViewById(R.id.topayj)
        uploadbtn.setOnClickListener {
            launch()

        }
        tp.setOnClickListener {
            val intent = Intent(this, Payment::class.java)
            intent.putExtra("code",6)
            intent.putExtra("cname","Job Application Circulation - Paid")
            intent.putExtra("cardtitle","Job Application Circulation - Paid!!")
            intent.putExtra("link",link)
            startActivity(intent)
        }
    }
    fun launch()
    {
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT


        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PDF && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            uri = data.data!!
            try {

                upload()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }





    }
    private fun upload() {
        if(uri != null) {


            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading")
            progressDialog.setMessage("Please wait while the document is being uploaded")
            progressDialog.show()
            val ref = storageReference?.child("Resume/" + UUID.randomUUID().toString())

            ref!!.putFile(uri!!).continueWithTask { task ->
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
                    disp(downloadUri.toString())

                } else {
                    // Handle failures
                    progressDialog.dismiss()
                    disp("Hello")
                    // ...
                }
            }


        }
        else{
            Toast.makeText(this, "Please Upload a pdf document", Toast.LENGTH_SHORT).show()
        }

    }

    fun disp(a:String)
    {
        link=a
    }
}
