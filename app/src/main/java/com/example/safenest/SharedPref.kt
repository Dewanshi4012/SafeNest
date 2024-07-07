package com.example.safenest

import android.content.Context
import android.content.SharedPreferences

object SharedPref {
    private const val NAME = "SafeNestSharedPref"
    private const val MODE = Context.MODE_PRIVATE
    lateinit var preferences:SharedPreferences
    private lateinit var name:String
    fun init(context: Context){
        preferences = context.getSharedPreferences(NAME, MODE)
    }
    fun putBoolean(key:String, value: Boolean){
        preferences.edit().putBoolean(key, value).commit()
    }
    fun getBoolean(key: String):Boolean{
        return preferences.getBoolean(key,false)
    }

    fun putString(key:String, name: String){
        preferences.edit().putString(key, name).commit()
    }
    fun getString(key: String):String?{
        return preferences.getString(key, name)
    }
}