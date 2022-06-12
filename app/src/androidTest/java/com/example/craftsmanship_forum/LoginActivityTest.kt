package com.example.craftsmanship_forum

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    lateinit var targetContext: Context
    lateinit var loginActivity: LoginActivity

    @Before
    fun prepare() {
        targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        loginActivity = LoginActivity()
    }
}