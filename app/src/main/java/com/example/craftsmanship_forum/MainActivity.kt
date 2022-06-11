package com.example.craftsmanship_forum

import android.content.Intent
import android.content.SharedPreferences
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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.craftsmanship_forum.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var sharedPreferences: SharedPreferences? = null

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

        sharedPreferences = getSharedPreferences(Static.sharedPreferenceName, MODE_PRIVATE)
        if (sharedPreferences!!.contains(Static.sharedPreferenceAutoLogin)) {
            Static.autoLogin = (sharedPreferences!!.getString(Static.sharedPreferenceAutoLogin, "") == "true")
        }
        if (sharedPreferences!!.contains(Static.sharedPreferenceUseBiometrics)) {
            Static.useBiometrics = (sharedPreferences!!.getString(Static.sharedPreferenceUseBiometrics, "") == "true")
        }

        if (Static.autoLogin) {
            onAutoLogin()
        }

        if (Static.useBiometrics) {
            onBiometricsLogin()
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
        super.onResume()
        if (!LoginInfo.isLogined) {
            binding.btnAdd.visibility = View.GONE
        } else {
            binding.btnAdd.visibility = View.VISIBLE
        }

        // Maybe useful in future
        /*
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {}
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {}
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {}
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {}
        }
        */
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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

    private fun isSavedLoginInfo(): Boolean {
        return sharedPreferences!!.contains(Static.sharedPreferenceEmail) && sharedPreferences!!.contains(
            Static.sharedPreferenceEmail)
    }

    private fun onAutoLogin() {
        if (isSavedLoginInfo()) {
            val base64Email = sharedPreferences!!.getString(Static.sharedPreferenceEmail, "")
            val base64Password =
                sharedPreferences!!.getString(Static.sharedPreferencePassword, "")
            if (base64Email != "" && base64Password != "") {
                val email =
                    String(Base64.decode(base64Email, Base64.DEFAULT), charset = Charsets.UTF_8)
                val password = String(
                    Base64.decode(base64Password, Base64.DEFAULT),
                    charset = Charsets.UTF_8
                )


                var auth = FirebaseAuth.getInstance()
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        LoginInfo.isLogined = true
                        LoginInfo.email = email
                        Toast.makeText(this, "Logged in as $email", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onBiometricsLogin() {
        if (isSavedLoginInfo()) {
            var executor = ContextCompat.getMainExecutor(this)

            var biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(
                            applicationContext,
                            "Authentication error: $errString", Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(
                            applicationContext,
                            "Authentication succeeded!", Toast.LENGTH_SHORT
                        )
                            .show()
                        onAutoLogin()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(
                            applicationContext, "Authentication failed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                })

            var promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }
}