package com.example.hyperxpress.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.hyperxpress.R
import com.example.hyperxpress.service.generics.Generics
import com.example.hyperxpress.service.repository.local.Connection
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.viewmodel.LoginViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var mViewModel: LoginViewModel
    private lateinit var edtPassword:EditText
    private lateinit var edtEmail:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = LoginViewModel(this)
        edtEmail = findViewById(R.id.input_email_tela_login)
        edtPassword = findViewById(R.id.input_senha_tela_login)
        observerLogin()
        mViewModel.verifyLoggedUser()
        observerUsuarioLogado()
    }
    fun irCadastro(view: View) {
        if (Connection.isInternet(this)){
            startActivity(Intent(this, CadastroPessoalActivity::class.java))
        }
    }
    fun login(view: View) {
//        if (Connection.isInternet(this)){
//            val gif: ImageView = findViewById(R.id.iv_gif)
//            gif.visibility = view.visibility
//            Glide.with(this)
//                .load(R.mipmap.gifcarregamento) // aqui é teu gif
//                .asGif()
//                .into(gif)
//            mViewModel.doLogin(edtEmail.text.toString(), edtPassword.text.toString(),gif)
//        }

    }

    fun esqueciSenha(view: View) {
        if (Connection.isInternet(this)){
            Toast.makeText(
                this,
                getString(R.string.redefinir_senha),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun observerLogin(){
        mViewModel.login.observe(this){
            if (it != null){
                startActivity(Intent(this, AplicacaoAposLogar::class.java))
                finish()
            }
            else{
                Toast.makeText(
                    this,
                    getString(R.string.erro_inesperado),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun observerUsuarioLogado(){
        mViewModel.loggedUser.observe(this) {
            if (it) {
                startActivity(Intent(this, AplicacaoAposLogar::class.java))
                finish()
            }
        }
    }

    fun visualizarSenhaLogin(view: android.view.View) {
        Generics.visualizarEsconderSenha(edtPassword)
    }
}