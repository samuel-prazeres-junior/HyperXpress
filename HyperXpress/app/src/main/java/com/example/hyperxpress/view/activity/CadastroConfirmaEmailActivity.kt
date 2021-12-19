package com.example.hyperxpress.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import com.example.hyperxpress.R
import com.example.hyperxpress.service.generics.Generics
import com.example.hyperxpress.service.model.entity.java.User
import com.example.hyperxpress.service.model.entity.kotlin.ImagemBase64
import com.example.hyperxpress.service.model.retornojson.UsuarioModel
import com.example.hyperxpress.service.repository.local.Connection
import com.example.hyperxpress.viewmodel.UsuarioViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.random.Random

class CadastroConfirmaEmailActivity : AppCompatActivity() {
    var codigo = Random.nextInt(100000, 200000)
    private lateinit var mViewModel: UsuarioViewModel
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var senha: String
    private lateinit var mSelected: String
    private lateinit var dados: Bundle
    private lateinit var gif: ImageView
    private lateinit var string64: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_confirma_email)
        mViewModel = UsuarioViewModel(this)
        gif = findViewById(R.id.iv_gif_tela_cadastro)
        dados = intent.extras!!
        string64 = dados?.getString("imagem64").toString()
        observer()
        observerCadastroImagemUser()
        username = dados?.getString(getString(R.string.cache_avatar)).toString()
        mSelected = dados.get(getString(R.string.cache_profile)).toString()
        email = dados.getString(getString(R.string.cache_email)).toString()
        senha = dados.getString(getString(R.string.cache_senha)).toString()
        enviarNotificacaoConfirmaEmail()

    }

    fun observer() {
        mViewModel.create.observe(this, {
            if (it != null) {
                mViewModel.cadastrarUserFirebase(username,email, senha, mSelected)
                gif.visibility = View.GONE
                mViewModel.cadastrarImagemUser(ImagemBase64(string64), it.id.toLong())
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.erro_inesperado),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
    fun observerCadastroImagemUser(){
        mViewModel.createImagemUser.observe(this, {
            if (it){
                startActivity(Intent(this, CadastroFinalizadoActivity::class.java))
            }
            else {
                Toast.makeText(this, getString(R.string.erro_inesperado), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun reenviarEmail(view: View) {
        if (Connection.isInternet(this)) {
            codigo = Random.nextInt(100000, 200000)
            enviarNotificacaoConfirmaEmail()
        }
    }

    fun confirmaCodigo(view: View) {
        val editCodigo: EditText = findViewById(R.id.input_codigo)
        if (editCodigo.text.toString() != "") {
            if (codigo == editCodigo.text.toString().toInt()) {

                val nome = dados?.getString(getString(R.string.cache_nome))
                val cpf = dados?.getString(getString(R.string.cache_cpf))
                val dataNasc = dados?.getString(getString(R.string.cache_data_nasc))
                val emailConfirmado = true
                val estadoUf = dados?.getString(getString(R.string.cache_estado_uf))
                val cep = dados?.getString(getString(R.string.cache_cep))
                val bairro = dados?.getString(getString(R.string.cache_bairro))
                val logradouro = dados?.getString(getString(R.string.cache_logradouro))
                val numero = dados?.getString(getString(R.string.cache_numero))
                val cidade = dados?.getString(getString(R.string.cache_cidade))
                val complemento = dados?.getString(getString(R.string.cache_complemento))

                if (Connection.isInternet(this)) {
                    val user = UsuarioModel(
                        0,
                        nome!!,
                        username!!,
                        cpf!!,
                        email!!,
                        senha!!,
                        dataNasc!!,
                        emailConfirmado,
                        estadoUf!!,
                        cep!!,
                        bairro!!,
                        logradouro!!,
                        numero!!,
                        cidade!!,
                        complemento!!
                    )
                    gif.visibility = View.VISIBLE
                    mViewModel.cadastrarUser(user)
                }

            } else {
                Toast.makeText(
                    this,
                    getString(R.string.codigo_incorreto),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            editCodigo.error = getString(R.string.digite_codigo)
        }
    }

    fun enviarNotificacaoConfirmaEmail() {
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Generics.criarNotificacao(
            getString(R.string.titulo_notificacao), getString(R.string.descricao_notificacao),
            "Seu codigo de verificação é: $codigo", notificationManager, this
        )
    }
}