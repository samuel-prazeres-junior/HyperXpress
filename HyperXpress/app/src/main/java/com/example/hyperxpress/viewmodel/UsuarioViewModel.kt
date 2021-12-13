package com.example.hyperxpress.viewmodel

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hyperxpress.R
import com.example.hyperxpress.service.generics.Generics
import com.example.hyperxpress.service.listener.ApiListener
import com.example.hyperxpress.service.listener.ValidationListener
import com.example.hyperxpress.service.model.adapter.UsuarioAdapter
import com.example.hyperxpress.service.model.entity.java.User
import com.example.hyperxpress.service.model.entity.kotlin.EnderecoModel
import com.example.hyperxpress.service.model.entity.kotlin.ImagemBase64
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.model.retornojson.InfoVendedor
import com.example.hyperxpress.service.model.retornojson.UsuarioModel
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.service.repository.remote.UserRepository
import com.example.hyperxpress.view.activity.MessageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class UsuarioViewModel(val application: Context) {
    private val mSharePreferences = SecurityPreferences.sharePrefences(application)
    private val mUserRepository = UserRepository(application)

    private val mCreate = MutableLiveData<UsuarioModel?>()
    var create: LiveData<UsuarioModel?> = mCreate

    private val mCreateImagemUser = MutableLiveData<Boolean>()
    var createImagemUser: LiveData<Boolean> = mCreateImagemUser

    private val mBuscaCep = MutableLiveData<ValidationListener>()
    var buscaCep: LiveData<ValidationListener> = mBuscaCep
    private val mAtualizarUser = MutableLiveData<String>()
    var atualizarUser: LiveData<String> = mAtualizarUser

    private val mInfoVendedor = MutableLiveData<InfoVendedor?>()
    var infoVendedor: LiveData<InfoVendedor?> = mInfoVendedor

    fun cadastrarUser(user: UsuarioModel) {
        mUserRepository.createUser(user, object : ApiListener<UsuarioModel> {
            override fun onSuccess(model: UsuarioModel) {
                mCreate.value = model

            }

            override fun onFailure(str: String) {
                mCreate.value = null
            }

        })
    }

    fun cadastrarImagemUser(imagem: ImagemBase64, idUsuario: Long) {
        mUserRepository.cadastrarImagemUser(imagem, idUsuario, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                mCreateImagemUser.value = model
            }

            override fun onFailure(str: String) {
                mCreateImagemUser.value = false
            }
        })
    }

    fun buscarCep(cep: String) {
        mUserRepository.buscarCep(cep, object : ApiListener<EnderecoModel> {
            override fun onSuccess(model: EnderecoModel) {
                SecurityPreferences.store(
                    mSharePreferences,
                    application.getString(R.string.estado),
                    model.uf
                )
                SecurityPreferences.store(
                    mSharePreferences,
                    application.getString(R.string.cidade),
                    model.cidade
                )
                SecurityPreferences.store(
                    mSharePreferences,
                    application.getString(R.string.endereco),
                    model.logradouro
                )
                SecurityPreferences.store(
                    mSharePreferences,
                    application.getString(R.string.bairro),
                    model.bairro
                )

                if (model.uf == "" && model.bairro == "" && model.logradouro == ""
                    && model.cidade == "" && model.logradouro == ""
                ) {
                    mBuscaCep.value =
                        ValidationListener(application.getString(R.string.cep_nao_encontrado))
                } else {
                    mBuscaCep.value = ValidationListener()
                }
            }

            override fun onFailure(str: String) {
                mBuscaCep.value = ValidationListener(str)
            }
        })
    }

    fun atualizarUser(idUsuario: Long, user: UsuarioModel) {
        mUserRepository.atualizarUser(idUsuario, user, object : ApiListener<String> {
            override fun onSuccess(model: String) {
                mAtualizarUser.value = model
            }

            override fun onFailure(str: String) {
                mAtualizarUser.value = str
            }
        })
    }

    fun cadastrarUserFirebase(username: String, email: String, senha: String, mSelected: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.user?.let { it1 ->
                        Log.e("Cadastrou User", it1.uid)
                        saveUserInFirebase(mSelected, username)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("Cadastrou User", it.message.toString())

            }
    }

    fun saveUserInFirebase(mSelected: String, username: String) {
        val filename = UUID.randomUUID().toString()
        val store: StorageReference =
            FirebaseStorage.getInstance().getReference("/images/${filename}")
        store.putFile(mSelected.toUri()) // subindo a imagem no storage
            .addOnCompleteListener {
                store.downloadUrl.addOnSuccessListener { urlImage ->
                    val uid = FirebaseAuth.getInstance().uid
                    val urlFoto = urlImage.toString()
                    val user = uid?.let { it1 -> User(it1, username, urlFoto) }
                    if (user != null) {
                        FirebaseFirestore.getInstance().collection("users")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                Log.e("cadastro Firebase", "cadastrou")

                            }
                            .addOnFailureListener {
                                it.message?.let { it1 -> Log.i("Teste", it1) }
                            }
                    }
                }

            }
            .addOnFailureListener {
                it.message?.let { it1 -> Log.i("teste", it1) }
            }
    }

    fun cadastrarInfoPessoais(
        foto:ImageView,
        nome: EditText,
        avatar: EditText,
        cpf: EditText,
        email: EditText,
        dataNasc: EditText,
        senha: EditText,
        confSenha: EditText,
    ): Boolean {
        fun nomeMinimoCaracteres(nome: String): Boolean = (nome.length >= 3)
        fun emailValido(email: String): Boolean =
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

        fun dataNascValida(data: String): Boolean = (data.length == 10)
        fun avatarMinimoCaracterValidado(avatar: String): Boolean = (avatar.length > 4)
        fun cpfOnzeNumerosDigitados(cpf: String): Boolean = (cpf.length == 11)
        fun senhaMinimoCaracterDigitado(senha: String): Boolean = (senha.length > 7)
        fun senhaIgualConfSenha(senha: String, confSenha: String): Boolean = (senha == confSenha)
        val listaVerificarCamposAdequados = mutableListOf<Boolean>()
        if (foto.drawable == null) {
            listaVerificarCamposAdequados.add(false)
            Toast.makeText(application, application.getString(R.string.erro_foto), Toast.LENGTH_SHORT).show()
        }
        if (!nomeMinimoCaracteres(nome.text.toString())) {
            nome.error = application.getString(R.string.nome_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (!avatarMinimoCaracterValidado(avatar.text.toString())) {
            avatar.error = application.getString(R.string.avatar_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (!cpfOnzeNumerosDigitados(cpf.text.toString())) {
            cpf.error = application.getString(R.string.cpf_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (!emailValido(email.text.toString())) {
            email.error = application.getString(R.string.email_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (!dataNascValida(dataNasc.text.toString())) {
            dataNasc.error = application.getString(R.string.data_nasc_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (!senhaMinimoCaracterDigitado(senha.text.toString())) {
            senha.error = application.getString(R.string.senha_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (!senhaIgualConfSenha(senha.text.toString(), confSenha.text.toString())) {
            confSenha.error = application.getString(R.string.data_confirmacao_senha_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (listaVerificarCamposAdequados.size != 0) {
            return false
        }
        return true
    }

    fun cadastrarInfoEndereco(
        estado: EditText,
        cidade: EditText,
        bairro: EditText,
        numero: EditText
    ): Boolean {
        fun estadoValido(estado: String): Boolean = (estado != "")
        fun cidadeValido(cidade: String): Boolean = (cidade != "")
        fun bairroValido(bairro: String): Boolean = (bairro != "")
        fun numeroValido(numero: String): Boolean = (numero != "")

        val listaVerificarCamposAdequados = mutableListOf<Boolean>()


        if (!estadoValido(estado.text.toString())) {
            estado.error = application.getString(R.string.estado_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (!cidadeValido(cidade.text.toString())) {
            cidade.error = application.getString(R.string.cidade_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (!bairroValido(bairro.text.toString())) {
            bairro.error = application.getString(R.string.bairro_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (!numeroValido(numero.text.toString())) {
            numero.error = application.getString(R.string.numero_invalido)
            listaVerificarCamposAdequados.add(false)
        }
        if (listaVerificarCamposAdequados.size != 0) {
            return false
        }
        return true
    }

    fun abrirCalendario(context: Context, listener: DatePickerDialog.OnDateSetListener) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(context, listener, year, month, day).show()
    }

    fun pegarDataCalendario(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return Generics.formatarData("dd-MM-yyyy", calendar.time)
    }

    fun buscarInfoVendedor(idUsuario: Long) {
        mUserRepository.buscarInfoVendedor(idUsuario, object : ApiListener<InfoVendedor> {
            override fun onSuccess(model: InfoVendedor) {
                mInfoVendedor.value = model
            }

            override fun onFailure(str: String) {
                mInfoVendedor.value = null
            }

        })
    }

    fun pegarImagemUsuario(idProduto: Long, imageView: ImageView) {
        mUserRepository.imagemUsuario(idProduto, object : ApiListener<ImagemBase64> {
            override fun onSuccess(model: ImagemBase64) {
                val array64 = Base64.decode(model.imagem, 0)
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(array64, 0, array64.size))
            }

            override fun onFailure(str: String) {
                imageView.setImageResource(R.drawable.icone_usuario)
            }

        })
    }

}