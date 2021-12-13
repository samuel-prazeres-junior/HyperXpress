package com.example.hyperxpress.service.repository.remote

import android.content.Context
import com.example.hyperxpress.R
import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.listener.ApiListener
import com.example.hyperxpress.service.model.retornojson.EnderecoUsuarioModel
import com.example.hyperxpress.service.model.retornojson.ListaEnderecos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnderecoRepository(private val context: Context) {
    private val mRetrofit = RetrofitHyperXpress.createService(EnderecoService::class.java, HyperXpressConstants.URLS.HYPERXPRESS)

    fun enderecoUser(id:Long, listener: ApiListener<EnderecoUsuarioModel>){

        val call: Call<EnderecoUsuarioModel> = mRetrofit.enderecoUser(id)

        call.enqueue(object : Callback<EnderecoUsuarioModel> {
            override fun onResponse(call: Call<EnderecoUsuarioModel>, response: Response<EnderecoUsuarioModel>) {
                if (response.code() != HyperXpressConstants.HTTP.SUCCESS){
                    listener.onFailure(context.getString(R.string.endereco_nao_encontrado))
                }
                else{
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<EnderecoUsuarioModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.erro_inesperado))
            }
        })
    }
}