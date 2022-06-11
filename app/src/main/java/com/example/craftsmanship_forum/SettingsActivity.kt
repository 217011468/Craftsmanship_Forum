package com.example.craftsmanship_forum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.CheckBox
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val checkBoxBiometrics = findViewById<CheckBox>(R.id.checkBoxBiometrics)
        val checkBoxAutoLogin = findViewById<CheckBox>(R.id.checkBoxAutoLogin)
        if (Static.useBiometrics) {
            checkBoxBiometrics.setChecked(true)
        }
        if (Static.autoLogin) {
            checkBoxAutoLogin.setChecked(true)
        }


        checkBoxBiometrics.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (Static.autoLogin) {
                checkBoxAutoLogin.setChecked(false)
            }

            Static.useBiometrics = isChecked
            var sharedPreferences = getSharedPreferences(Static.sharedPreferenceName, MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString(Static.sharedPreferenceUseBiometrics, if (isChecked) "true" else "false")
            editor.commit()
        }
        checkBoxAutoLogin.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (Static.useBiometrics) {
                checkBoxAutoLogin.setChecked(false)
                Toast.makeText( this, "Cannot use auto login when enabled biometrics", Toast.LENGTH_SHORT).show()
                return@setOnCheckedChangeListener
            }

            Static.autoLogin = isChecked
            var sharedPreferences = getSharedPreferences(Static.sharedPreferenceName, MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString(Static.sharedPreferenceAutoLogin, if (isChecked) "true" else "false")
            editor.commit()
        }
    }
}