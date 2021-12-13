package com.example.hyperxpress.service.repository.remote

import android.content.Context
import com.example.hyperxpress.R
import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.listener.ApiListener
import com.example.hyperxpress.service.model.entity.kotlin.EnderecoModel
import com.example.hyperxpress.service.model.retornojson.UsuarioModel
import com.example.hyperxpress.service.model.adapter.UsuarioAdapter
import com.example.hyperxpress.service.model.entity.kotlin.ImagemBase64
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.model.retornojson.InfoVendedor
import com.example.hyperxpress.service.model.retornojson.LoginModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(val context: Context){

   private val mRetrofit = RetrofitHyperXpress.createService(UserService::class.java, HyperXpressConstants.URLS.HYPERXPRESS)
   private val mRetrofitEndereco =  RetrofitHyperXpress.createService(UserService::class.java, HyperXpressConstants.URLS.VIACEP)
    fun login(email:String, password:String, listener: ApiListener<UsuarioAdapter>){ // metodo responsavel por fazer a requisição na api

        val login = LoginModel(email, password)
        val call: Call<UsuarioAdapter> = mRetrofit.login(login)

        call.enqueue(object : Callback<UsuarioAdapter>{
            override fun onResponse(call: Call<UsuarioAdapter>, response: Response<UsuarioAdapter>) {
                if (response.code() != HyperXpressConstants.HTTP.SUCCESS){
                    listener.onFailure(context.getString(R.string.login_falhou))
                }
                else{
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
            override fun onFailure(call: Call<UsuarioAdapter>, t: Throwable) {
                listener.onFailure(context.getString(R.string.erro_inesperado))
            }
        })
    }

    fun createUser(user: UsuarioModel, listener:ApiListener<UsuarioModel>){
        val call:Call<UsuarioModel> = mRetrofit.cadastroUser(user)
        call.enqueue(object :Callback<UsuarioModel>{
            override fun onResponse(call: Call<UsuarioModel>, response: Response<UsuarioModel>) {
                if (response.code() != HyperXpressConstants.HTTP.CREATED){
                    listener.onFailure(response.message())
                }
                else{
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<UsuarioModel>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

        })

    }

    fun atualizarUser(idUsuario:Long, user: UsuarioModel, listener: ApiListener<String>) {
        val call:Call<Void> = mRetrofit.atualizarUser(idUsuario, user);
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() != HyperXpressConstants.HTTP.SUCCESS){
                    listener.onFailure(response.message())
                }
                else {
                    listener.onSuccess(context.getString(R.string.editar_cadastro_sucesso))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.message?.let { listener.onFailure(it) }
            }

        })

    }

        fun buscarCep(cep:String, listener: ApiListener<EnderecoModel>){
        val call: Call<EnderecoModel> = mRetrofitEndereco.buscarEndereco(cep)
        call.enqueue(object : Callback<EnderecoModel>{
            override fun onResponse(
                call: Call<EnderecoModel>,
                response: Response<EnderecoModel>
            ) {
                if (response.code() != HyperXpressConstants.HTTP.SUCCESS){
                    listener.onFailure(response.message())
                }
                else{

                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<EnderecoModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.erro_inesperado))
            }

        })
    }

    fun buscarInfoVendedor(idUsuario:Long, listener: ApiListener<InfoVendedor>){
        val call:Call<InfoVendedor> =  mRetrofit.buscarInfoVendedor(idUsuario)
        call.enqueue(object : Callback<InfoVendedor> {
            override fun onResponse(
                call: Call<InfoVendedor>,
                response: Response<InfoVendedor>
            ) {
                if (response.code() != HyperXpressConstants.HTTP.SUCCESS){
                    listener.onFailure(response.message())
                }
                else{
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<InfoVendedor>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

        })

    }

    fun cadastrarImagemUser(imagem:ImagemBase64, idUsuario: Long, listener: ApiListener<Boolean>){
        val call:Call<Void> = mRetrofit.cadastroImagemUser(imagem, idUsuario)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() != HyperXpressConstants.HTTP.CREATED){
                    listener.onFailure(response.message())
                }
                else {
                    listener.onSuccess(true)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.message?.let { listener.onFailure(it) }
            }
        })
    }

    fun imagemUsuario(idUsuario: Long, listener: ApiListener<ImagemBase64>) {
        val call: Call<ImagemBase64> = mRetrofit.imagemUsuario(idUsuario)
        call.enqueue(object : Callback<ImagemBase64> {
            override fun onResponse(call: Call<ImagemBase64>, response: Response<ImagemBase64>) {
                if (response.code() != HyperXpressConstants.HTTP.SUCCESS) {
                    listener.onFailure(response.message())
                } else {
                    if (response.body()?.imagem.isNullOrEmpty()) {
                        listener.onFailure(response.message())
                    } else {
                        response.body()?.let { listener.onSuccess(it) }
                    }
                }
            }

            override fun onFailure(call: Call<ImagemBase64>, t: Throwable) {
                t.message?.let { listener.onFailure(it) }
            }

        })
    }
}