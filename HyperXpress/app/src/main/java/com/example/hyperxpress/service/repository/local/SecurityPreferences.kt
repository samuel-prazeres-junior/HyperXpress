package com.example.hyperxpress.service.repository.local

import android.content.Context
import android.content.SharedPreferences

class SecurityPreferences (){

    companion object{
        private lateinit var mPreferences: SharedPreferences
        private fun session(context: Context):SharedPreferences {
            if (!::mPreferences.isInitialized){
                mPreferences =  context.getSharedPreferences("taskShared", Context.MODE_PRIVATE)
            }
            return mPreferences
        }
        fun sharePrefences(context: Context):SharedPreferences{
            return session(context)
        }

        fun store(shared:SharedPreferences,key: String, value: String) {
            shared.edit().putString(key, value).apply()
        }

        fun remove(shared:SharedPreferences,key: String) {
            shared.edit().remove(key).apply()
        }

        fun get(shared:SharedPreferences,key: String): String {
            return shared.getString(key, "") ?: ""
        }
    }
}