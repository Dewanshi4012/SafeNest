package com.example.safenest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreen :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SharedPref.init(this)
        val isUserLoggedIn = SharedPref.getBoolean(PrefContants.IS_USER_LOGGED_IN)

        if(isUserLoggedIn){
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }
}