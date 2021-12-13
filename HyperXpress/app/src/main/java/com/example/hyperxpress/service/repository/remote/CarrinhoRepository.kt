package com.example.hyperxpress.service.repository.remote

import android.content.Context
import com.example.hyperxpress.R
import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.listener.ApiListener
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarrinhoRepository(val context: Context){
    private val mRetrofit = RetrofitHyperXpress.createService(CarrinhoService::class.java, HyperXpressConstants.URLS.HYPERXPRESS)

    fun carrinhoUser(id:Long, listener: ApiListener<List<AnuncioModel>>){
        val call: Call<List<AnuncioModel>> = mRetrofit.carrinhoUser(id)
        call.enqueue(object : Callback<List<AnuncioModel>>{
            override fun onResponse(
                call: Call<List<AnuncioModel>>,
                response: Response<List<AnuncioModel>>
            ) {
                response.body()?.let { listener.onSuccess(it) }
            }

            override fun onFailure(call: Call<List<AnuncioModel>>, t: Throwable) {
                t.message?.let { listener.onFailure(it) }
            }

        })
    }

    fun adicionarProdutoCarrinho(idProduto:Long, idUsuario:Long, listener: ApiListener<Boolean>){
        val call:Call<Void> = mRetrofit.adicionarProdutoCarrinho(idProduto, idUsuario)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() != HyperXpressConstants.HTTP.CREATED){
                    listener.onFailure(response.message())
                }
                else{
                    listener.onSuccess(true)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

        })
    }


    fun removerItemCarrinho(idProduto: Long, idUsuario: Long, listener: ApiListener<Boolean>){

        val call: Call<Void> = mRetrofit.removerItemCarrinho(idProduto,idUsuario)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == HyperXpressConstants.HTTP.NOCONTENT){
                    listener.onSuccess(true)
                }
                else {
                    listener.onFailure(context.getString(R.string.erro_inesperado))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                listener.onFailure(context.getString(R.string.erro_inesperado))
            }

        })
    }
}