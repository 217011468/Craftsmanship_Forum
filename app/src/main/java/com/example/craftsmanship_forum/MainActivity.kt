package com.example.craftsmanship_forum

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.craftsmanship_forum.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.btnAdd.setOnClickListener { view ->
            onBtnAddClickListener(view)
        }

        val sharedPreferences = getSharedPreferences(Static.sharedPreferenceName, MODE_PRIVATE)
        if (sharedPreferences!!.contains(Static.sharedPreferenceAutoLogin)) {
            Static.autoLogin = (sharedPreferences!!.getString(Static.sharedPreferenceAutoLogin, "") == "true")
        }
        if (sharedPreferences!!.contains(Static.sharedPreferenceUseBiometrics)) {
            Static.useBiometrics = (sharedPreferences!!.getString(Static.sharedPreferenceUseBiometrics, "") == "true")
        }

        if (sharedPreferences!!.contains(Static.sharedPreferenceEmail) && sharedPreferences!!.contains(Static.sharedPreferenceEmail)) {
            val base64Email = sharedPreferences!!.getString(Static.sharedPreferenceEmail, "")
            val base64Password = sharedPreferences!!.getString(Static.sharedPreferencePassword, "")
            if (base64Email != "" && base64Password != "") {
                val email = String(Base64.decode(base64Email, Base64.DEFAULT), charset = Charsets.UTF_8)
                val password = String(Base64.decode(base64Password, Base64.DEFAULT), charset = Charsets.UTF_8)


                var auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        LoginInfo.isLogined = true
                        LoginInfo.email = email
                        Toast.makeText( this, "Logged in as $email", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (LoginInfo.isLogined) {
            menu?.findItem(R.id.action_login_out)?.title = "Logout"
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        if (!LoginInfo.isLogined) {
            binding.btnAdd.visibility = View.GONE
        } else {
            binding.btnAdd.visibility = View.VISIBLE
        }
        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_login_out -> {
                if (LoginInfo.isLogined) {
                    LoginInfo.isLogined = false
                    LoginInfo.email = ""
                    item.title = "Login"
                    Toast.makeText( this, "Logged out", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun onBtnAddClickListener(view: View) {
        val intent = Intent(this, AddPostActivity::class.java)
        startActivity(intent)
    }
}