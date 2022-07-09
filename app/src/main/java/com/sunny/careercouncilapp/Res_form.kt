package com.sunny.careercouncilapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_res_form.*
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

class Res_form : AppCompatActivity() {
    private lateinit var sb: Button
    private lateinit var email: String

    private lateinit var data: StringBuilder
    private lateinit var fstore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fstore = FirebaseFirestore.getInstance()
        data= StringBuilder()

        setContentView(R.layout.activity_res_form)
        val a: EditText = findViewById(R.id.rfname)
        val b: EditText = findViewById(R.id.rfid)
        val c: EditText = findViewById(R.id.rfemail)
        val d: EditText = findViewById(R.id.rflbay)
        val e: EditText = findViewById(R.id.rfjobpos)
        val f: EditText = findViewById(R.id.rfwe)
        val g: EditText = findViewById(R.id.rfeq1)
        val h: EditText = findViewById(R.id.rfeq2)
        val i: EditText = findViewById(R.id.rfeq3)
        val j: EditText = findViewById(R.id.rfoeq)
        val k: EditText = findViewById(R.id.rfproject)
        val l: EditText = findViewById(R.id.rfcerti)
        val m: EditText = findViewById(R.id.rfref)
        val n: EditText = findViewById(R.id.rfint)
        val o: EditText = findViewById(R.id.rflang)
        


        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url

            email = user?.email.toString()



        }
        val rg: RadioGroup = findViewById(R.id.rg)
        val cho1: RadioButton = findViewById(R.id.cho1)
        val cho2: RadioButton = findViewById(R.id.cho2)
        sb = findViewById(R.id.sbquiz)
        sb.setOnClickListener {
            data.append("Name: ")
            data.append(a.text.toString())
            data.append("\n\n")
            data.append("Email: ")
            data.append(b.text.toString())
            data.append("\n\n")
            data.append("Contact No.: ")
            data.append(c.text.toString())
            data.append("\n\n")
            data.append("About Yourself: ")
            data.append(d.text.toString())
            data.append("\n\n")
            data.append("Most Recent Job Position: ")
            data.append(e.text.toString())
            data.append("\n\n")
            data.append("Work Experience: ")
            data.append(f.text.toString())
            data.append("\n\n")
            data.append("Educational Qualification 1: ")
            data.append(g.text.toString())
            data.append("\n\n")
            data.append("Educational Qualification 2: ")
            data.append(h.text.toString())
            data.append("\n\n")
            data.append("Educational Qualification 3: ")
            data.append(i.text.toString())
            data.append("\n\n")
            data.append("Other Qualifications/ Licences")
            data.append(j.text.toString())
            data.append("\n\n")
            data.append("Notable Accomplishments/ Projects: ")
            data.append(k.text.toString())
            data.append("\n\n")
            data.append("Certifications: ")
            data.append(l.text.toString())
            data.append("\n\n")
            data.append("References: ")
            data.append(m.text.toString())
            data.append("\n\n")
            data.append("Interests: ")
            data.append(n.text.toString())
            data.append("\n\n")
            data.append("Languages Known: ")
            data.append(o.text.toString())
            data.append("\n\n")
            val sel = rg.checkedRadioButtonId
            fun md5(str: String): ByteArray = MessageDigest.getInstance("MD5").digest(str.toByteArray(
                StandardCharsets.UTF_8
            ))
            fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }
            val key = email+" "+ Timestamp.now().toDate().toString()
            val id= md5(key).toHex()
            val pend = hashMapOf(
                "data" to data.toString()

            )

            fstore.collection("ResForm").document(id)
                .set(pend)
                .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

            if(cho1.isChecked)
            {
                val intent = Intent(this, Payment::class.java)
                intent.putExtra("code",4)
                intent.putExtra("cname","Resume Upload Form- Paid")
                intent.putExtra("cardtitle","Resume Building from Form\nResume Type: Infographic (INR 3500)\n PAID!!")
                intent.putExtra("link",id)
                startActivity(intent)
            }
            else if(cho2.isChecked)
            {
                val intent = Intent(this, Payment::class.java)
                intent.putExtra("code",5)
                intent.putExtra("cname","Resume Upload Form- Paid")
                intent.putExtra("cardtitle","Resume Building from Form\nResume Type: Normal PDF/Word (INR 3000)\n PAID!!")
                intent.putExtra("link",id)
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this,"Please select the type of Resume", Toast.LENGTH_LONG).show()
            }


        }
    }
}