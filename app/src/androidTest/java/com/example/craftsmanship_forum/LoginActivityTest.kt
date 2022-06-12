package com.example.craftsmanship_forum

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class LoginActivityTest {
    lateinit var loginActiviy: LoginActivity

    @Before
    fun prepare() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        loginActiviy = LoginActivity()
    }

    @Test
    fun saveAccount() {
        val result = loginActiviy.saveAccount("Hello@gmail.com", "Bye")
        assertEquals(result, true)
    }

}