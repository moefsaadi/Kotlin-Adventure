package com.example.kotlinmigration.database

import androidx.appcompat.app.AppCompatDelegate
import com.example.kotlinmigration.app.App

class SharedPreferences() {

    var prefs = App.sharedPreferences

    fun setInt(key:String, value:Int){

        prefs = App.sharedPreferences
        val editor= prefs.edit()
        editor.putInt(key,value)
        editor.apply()

    }

    fun getInt(key: String) {

        prefs = App.sharedPreferences
        val theme = prefs.getInt(key,0)
        AppCompatDelegate.setDefaultNightMode(theme)

    }

}
