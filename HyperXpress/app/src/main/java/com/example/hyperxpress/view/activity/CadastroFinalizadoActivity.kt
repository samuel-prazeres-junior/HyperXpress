package com.example.hyperxpress.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import com.example.hyperxpress.R

class CadastroFinalizadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_finalizado)

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },2000)
    }
}