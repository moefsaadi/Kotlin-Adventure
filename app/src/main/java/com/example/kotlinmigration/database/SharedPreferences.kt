package com.example.kotlinmigration.database

import androidx.appcompat.app.AppCompatDelegate
import com.example.kotlinmigration.app.App

class SharedPreferences() {

    var prefs = App.sharedPreferences
    val editor= prefs.edit()

    fun setInt(key:String, value:Int){

        editor.putInt(key,value)
        editor.apply()

    }

    fun getInt(key: String): Int {

        val theme = prefs.getInt(key,0)
        return theme

    }

}
