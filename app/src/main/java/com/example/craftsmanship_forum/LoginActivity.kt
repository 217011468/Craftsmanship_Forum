package com.example.craftsmanship_forum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
    }

    fun login(view: View){
        val editTextEmailAddress = findViewById<EditText>(R.id.editTextEmailAddress)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val email = editTextEmailAddress.text.toString()
        val password = editTextPassword.text.toString()

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                LoginInfo.isLogined = true
                LoginInfo.email = email
                var sharedPreferences = getSharedPreferences(Static.sharedPreferenceName, MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("Email", Base64.encodeToString(email.toByteArray(charset("UTF-8")), Base64.DEFAULT))
                editor.putString("Password", Base64.encodeToString(password.toByteArray(charset("UTF-8")), Base64.DEFAULT))
                editor.commit()
                Toast.makeText( this, "Logged in as $email", Toast.LENGTH_SHORT).show()
                finish()

            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    fun goToRegister(view: View) {
        val intent= Intent(this,RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}