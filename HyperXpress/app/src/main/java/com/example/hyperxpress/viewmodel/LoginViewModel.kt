package com.example.hyperxpress.viewmodel

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hyperxpress.R
import com.example.hyperxpress.service.listener.ApiListener
import com.example.hyperxpress.service.listener.ValidationListener
import com.example.hyperxpress.service.model.adapter.UsuarioAdapter
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.service.repository.remote.UserRepository

class LoginViewModel (val application: Context){

    private val mUserRepository = UserRepository(application)
    private val mSharePreferences = SecurityPreferences.sharePrefences(application)
    private val mLogin = MutableLiveData<UsuarioAdapter?>()
    var login: LiveData<UsuarioAdapter?> = mLogin
    private val mLoggeUser = MutableLiveData<Boolean>()
    var loggedUser: LiveData<Boolean> = mLoggeUser


    fun doLogin(email:String, password:String, imageView: ImageView){ // metodo responsavel pela logica do login
        mUserRepository.login(email, password, object : ApiListener<UsuarioAdapter>{
            override fun onSuccess(model: UsuarioAdapter) {
                SecurityPreferences.store(mSharePreferences,application.getString(R.string.cache_id), model.id.toString())
                SecurityPreferences.store(mSharePreferences,application.getString(R.string.cache_avatar), model.avatar)
                SecurityPreferences.store(mSharePreferences,application.getString(R.string.cache_email), model.email)
                SecurityPreferences.store(mSharePreferences,application.getString(R.string.cache_senha), model.senha)
                mLogin.value = model
                imageView.visibility = View.GONE
            }
            override fun onFailure(str: String) {
                mLogin.value = null
                imageView.visibility = View.GONE
            }

        })
    }

    fun verifyLoggedUser(){
        val id = SecurityPreferences.get(mSharePreferences,application.getString(R.string.cache_id))
        val avatar = SecurityPreferences.get(mSharePreferences,application.getString(R.string.cache_avatar))
        val email = SecurityPreferences.get(mSharePreferences,application.getString(R.string.cache_email))
        mLoggeUser.value = (id != "" && avatar != "" && email != "")
    }
}