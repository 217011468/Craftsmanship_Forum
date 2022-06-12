package com.example.craftsmanship_forum

class Static {
    companion object {
        val logTag: String = "Craftsmanship_Forum"
        val sharedPreferenceName: String = "CraftsmanshipForumShare"
        val sharedPreferenceEmail: String = "Email"
        val sharedPreferencePassword: String = "Password"
        val sharedPreferenceUseBiometrics: String = "UseBiometrics"
        val sharedPreferenceAutoLogin: String = "AutoLogin"


        var viewPostObjectId: String? = null
        var useBiometrics: Boolean = false
        var autoLogin: Boolean = true
        var mainActivityFragment: Int = 1
    }
}