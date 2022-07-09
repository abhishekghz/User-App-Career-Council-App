package com.sunny.careercouncilapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class Res_upload : AppCompatActivity() {
    private lateinit var uploadbtn: Button
    private lateinit var tp: Button
    private val PDF = 0
    lateinit var uri: Uri
    lateinit var link: String
    private var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_upload)
        storageReference = FirebaseStorage.getInstance().reference
        val ch1 : RadioButton = findViewById(R.id.ch1)
        val ch : RadioButton = findViewById(R.id.ch)
        link="Not assigned"

        uploadbtn = findViewById(R.id.ub)
        tp = findViewById(R.id.topayment)
        uploadbtn.setOnClickListener {
            launch()

        }
        tp.setOnClickListener {
            if(ch1.isChecked)
            {
                val intent = Intent(this, Payment::class.java)
                intent.putExtra("code",2)
                intent.putExtra("cname","Resume Upload File- Paid")
                intent.putExtra("cardtitle","Resume Building from File\nResume Type: Infographic (INR 3500)\n PAID!!")
                intent.putExtra("link",link)
                startActivity(intent)
            }
            else if(ch.isChecked)
            {
                val intent = Intent(this, Payment::class.java)
                intent.putExtra("code",3)
                intent.putExtra("cname","Resume Upload File- Paid")
                intent.putExtra("cardtitle","Resume Building from File\nResume Type: Normal PDF/Word (INR 3000)\n PAID!!")
                intent.putExtra("link",link)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this,"Please select the type of Resume",Toast.LENGTH_LONG).show()
            }

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
        val uriTxt = findViewById<View>(R.id.uriTxt) as TextView
        if (requestCode == PDF && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            uri = data.data!!
            try {
                uriTxt.text = uri.toString()
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
    fun disp(a:String){
        link = a

    }
}


