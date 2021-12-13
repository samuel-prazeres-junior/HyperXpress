package com.example.hyperxpress.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.hyperxpress.R
import com.example.hyperxpress.service.model.entity.kotlin.EnderecoModel
import com.example.hyperxpress.service.repository.local.Connection
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.viewmodel.UsuarioViewModel
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher

class CadastroEnderecoActivity : AppCompatActivity() {
    private lateinit var enderecoAdapter: EnderecoModel
    private lateinit var usuarioEndereco: UsuarioViewModel
    private lateinit var mSharePreferences: SharedPreferences
    private lateinit var editCep: EditText
    private lateinit var editEstado: EditText
    private lateinit var editCidade: EditText
    private lateinit var editEndereco: EditText
    private lateinit var editBairro: EditText
    private lateinit var editNumero: EditText
    private lateinit var btnProximo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_endereco)
        mSharePreferences = SecurityPreferences.sharePrefences(applicationContext)
        usuarioEndereco = UsuarioViewModel(application)
        enderecoAdapter = EnderecoModel()
        btnProximo = findViewById(R.id.btn_tela_endereco_proximo)
        btnProximo.isEnabled = false
        inicializarEditsTexts()
        observer()
        val smf = SimpleMaskFormatter("NNNNN-NNN")
        val mtw = MaskTextWatcher(editCep, smf)
        editCep.addTextChangedListener(mtw)
    }

    fun cadastroEndereco(view: View) {

        val dados = intent.extras
        val nome = dados?.getString("nome")
        val avatar = dados?.getString("avatar")
        val cpf = dados?.getString("cpf")
        val email = dados?.getString("email")
        val dataNasc = dados?.getString("dataNasc")
        val senha = dados?.getString("senha")
        val mSelected = dados?.get("profileurl")
        val string64 = dados?.get("imagem64")

        val cadastroEnderecoSucesso =
            usuarioEndereco.cadastrarInfoEndereco(
                editEstado,
                editCidade,
                editEndereco,
                editBairro
            )

        if (cadastroEnderecoSucesso) {
            if (Connection.isInternet(this)){
                val dadosCadastroEndereco = Intent(this, CadastroConfirmaEmailActivity::class.java)

                dadosCadastroEndereco.putExtra(getString(R.string.cache_nome), nome)
                dadosCadastroEndereco.putExtra(getString(R.string.cache_avatar), avatar)
                dadosCadastroEndereco.putExtra(getString(R.string.cache_cpf), cpf)
                dadosCadastroEndereco.putExtra(getString(R.string.cache_email), email)
                dadosCadastroEndereco.putExtra(getString(R.string.cache_data_nasc), dataNasc)
                dadosCadastroEndereco.putExtra(getString(R.string.cache_senha), senha)
                dadosCadastroEndereco.putExtra(getString(R.string.cache_cep), editCep.text.toString())
                dadosCadastroEndereco.putExtra(getString(R.string.cache_estado_uf), editEstado.text.toString())
                dadosCadastroEndereco.putExtra(getString(R.string.cache_cidade), editCidade.text.toString())
                dadosCadastroEndereco.putExtra(getString(R.string.cache_endereco), editEndereco.text.toString())
                dadosCadastroEndereco.putExtra(getString(R.string.cache_bairro), editBairro.text.toString())
                dadosCadastroEndereco.putExtra(getString(R.string.cache_numero), editNumero.text.toString())
                dadosCadastroEndereco.putExtra(getString(R.string.cache_profile), mSelected.toString())
                dadosCadastroEndereco.putExtra("imagem64", string64.toString())
                dadosCadastroEndereco.putExtra("googleId", "1234")
                dadosCadastroEndereco.putExtra(getString(R.string.cache_logradouro), "Cortis")
                dadosCadastroEndereco.putExtra(getString(R.string.cache_complemento), "Casa 2")

                startActivity(dadosCadastroEndereco)
            }
        }
    }

    fun voltar(view: View) {
        startActivity(Intent(this, CadastroPessoalActivity::class.java))
    }

    fun buscarCep(view: View) {
        if (Connection.isInternet(this)){
            usuarioEndereco.buscarCep(editCep.text.toString())
        }
    }

    fun observer() {
        usuarioEndereco.buscaCep.observe(this) {
            if (it.sucess()) {
                atribuirValorEditsTextsEndereco()
                destruiSharepreferencesEndereco()
                btnProximo.isEnabled = true
            } else {
                btnProximo.isEnabled = false
                val message = it.failure()
                Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun inicializarEditsTexts() {
        editCep = findViewById(R.id.input_cep)
        editEstado = findViewById(R.id.input_estado)
        editCidade = findViewById(R.id.input_cidade)
        editEndereco = findViewById(R.id.input_endereco)
        editBairro = findViewById(R.id.input_bairro)
        editNumero = findViewById(R.id.input_numero)
    }

    fun atribuirValorEditsTextsEndereco() {
        editEstado.setText(SecurityPreferences.get(mSharePreferences, getString(R.string.estado)))
        editCidade.setText(SecurityPreferences.get(mSharePreferences, getString(R.string.cidade)))
        editEndereco.setText(SecurityPreferences.get(mSharePreferences, getString(R.string.endereco)))
        editBairro.setText(SecurityPreferences.get(mSharePreferences, getString(R.string.bairro)))
    }

    fun destruiSharepreferencesEndereco() {
        SecurityPreferences.remove(mSharePreferences, getString(R.string.estado))
        SecurityPreferences.remove(mSharePreferences, getString(R.string.cidade))
        SecurityPreferences.remove(mSharePreferences, getString(R.string.endereco))
        SecurityPreferences.remove(mSharePreferences, getString(R.string.bairro))
    }
}