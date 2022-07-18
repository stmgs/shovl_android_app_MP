package com.shovl.shovl_android.utilities

import android.content.Context
import android.content.SharedPreferences

class PreferenceMangager(context: Context) {
    private var sharedPreferences: SharedPreferences = context
        .getSharedPreferences(ShovlConstants.KEY_PERFERENCE_NAME, Context.MODE_PRIVATE)

    fun putBoolean(key : String, value : Boolean){
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    fun getBoolean(key : String) : Boolean{
        return sharedPreferences.getBoolean(key, false)
    }

    fun putString(key : String, value : String){
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun getString(key : String) : String{
        return sharedPreferences.getString(key, null).toString()
    }

    fun putInt(key : String, value : Int){
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(key,value)
        editor.apply()
    }

    fun getInt(key : String) : Int{
        return sharedPreferences.getInt(key, 0)
    }

    fun clear(){
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }






}