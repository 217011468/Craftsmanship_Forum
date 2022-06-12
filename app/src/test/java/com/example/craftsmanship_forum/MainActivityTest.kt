package com.example.craftsmanship_forum

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AppCompatActivity {
    var sharedPreferences: SharedPreferences? = null
    var context: Context? = null


    @Before
    fun prepare() {
        sharedPreferences = mock(SharedPreferences::class.java)
        context = mock(Context::class.java)
    }

    @Test
    fun onAutoLogin() {
        `when`(context!!.getSharedPreferences(Static.sharedPreferenceName,
            AppCompatActivity.MODE_PRIVATE
        )).thenReturn(sharedPreferences).thenReturn(sharedPreferences)
        `when`(sharedPreferences!!.getString(anyString(), anyString())).thenReturn(
            anyString())
    }

    @Test
    fun isSavedLoginInfo() {
        //val sharedPreferences = mock(SharedPreferences::class.java)
        //`when`(sharedPreferences!!.contains(anyString())).thenReturn(anyBoolean())
    }
}