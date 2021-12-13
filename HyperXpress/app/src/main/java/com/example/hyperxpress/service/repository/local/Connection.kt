package com.example.hyperxpress.service.repository.local

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.example.hyperxpress.R

class Connection {

    companion object{
        private fun conection(context: Context):Boolean{
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }
        fun isInternet(context: Context):Boolean{
            val conection = conection(context)
            if (!conection){
                Toast.makeText(context.applicationContext,
                    context.getText(R.string.sem_internet), Toast.LENGTH_LONG).show()
                return false
            }
            return true
        }
    }

}