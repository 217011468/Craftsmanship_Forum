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
                saveAccount(email, password)
                Toast.makeText( this, "Logged in as $email", Toast.LENGTH_SHORT).show()
                finish()

            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }


    fun toBase64(msg: String): String {
        return Base64.encodeToString(msg.toByteArray(charset("UTF-8")), Base64.DEFAULT)
    }

    fun saveAccount(email: String, password: String): Boolean {
        var sharedPreferences = getSharedPreferences(Static.sharedPreferenceName, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Static.sharedPreferenceEmail, toBase64(email))
        editor.putString(Static.sharedPreferencePassword, toBase64(password))
        editor.commit()
        return true
    }

    fun goToRegister(view: View) {
        val intent = Intent(this,RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}