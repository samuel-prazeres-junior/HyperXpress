package com.example.hyperxpress.service.repository.remote

import android.content.Context
import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.listener.ApiListener
import com.example.hyperxpress.service.model.entity.kotlin.ImagemBase64
import com.example.hyperxpress.service.model.entity.kotlin.Produto
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.model.retornojson.AnunciosComPageModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdutoRepository(val context: Context) {
    private val mRetrofit = RetrofitHyperXpress.createService(
        ProdutoService::class.java,
        HyperXpressConstants.URLS.HYPERXPRESS
    )

    fun anuncios(listener: ApiListener<List<AnuncioModel>>) {
        val call: Call<AnunciosComPageModel> = mRetrofit.anuncios()
        call.enqueue(object : Callback<AnunciosComPageModel> {
            override fun onResponse(
                call: Call<AnunciosComPageModel>,
                response: Response<AnunciosComPageModel>
            ) {
                if (response.code() != HyperXpressConstants.HTTP.SUCCESS) {
                    listener.onFailure(response.message())
                } else {
                    response.body()?.let { listener.onSuccess(it.listaAnuncios) }
                }
            }

            override fun onFailure(call: Call<AnunciosComPageModel>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

        })
    }

    fun adicionarProduto(produto: Produto, listener: ApiListener<Produto>) {
        val call: Call<Produto> = mRetrofit.adicionarProduto(produto)
        call.enqueue(object : Callback<Produto> {
            override fun onResponse(call: Call<Produto>, response: Response<Produto>) {
                if (response.code() != HyperXpressConstants.HTTP.CREATED) {
                    listener.onFailure(response.message())
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<Produto>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

        })
    }

    fun imagemProduto(idProduto: Long, imagem: Long, listener: ApiListener<ImagemBase64>) {
        val call: Call<ImagemBase64> = mRetrofit.imagemProduto(idProduto, imagem)
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

    fun adicionarImagemProduto(
        imagem: ImagemBase64,
        idProduto: Long,
        listener: ApiListener<Boolean>
    ) {
        val call: Call<Void> = mRetrofit.adicionarImagemProduto(imagem, idProduto)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() != HyperXpressConstants.HTTP.SUCCESS) {
                    listener.onFailure(response.message())
                } else {
                    listener.onSuccess(true)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

        })
    }

    fun favoritarProduto(idUsuario: Long, idProduto: Long, listener: ApiListener<AnuncioModel>) {
        val call: Call<AnuncioModel> = mRetrofit.favoritarProduto(idUsuario, idProduto)
        call.enqueue(object : Callback<AnuncioModel> {
            override fun onResponse(call: Call<AnuncioModel>, response: Response<AnuncioModel>) {
                if (response.code() != HyperXpressConstants.HTTP.CREATED) {
                    listener.onFailure(response.message())
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

            override fun onFailure(call: Call<AnuncioModel>, t: Throwable) {
                t.message?.let { listener.onFailure(it) }
            }

        })
    }

    fun favoritosUser(id: Long, listener: ApiListener<List<AnuncioModel>>) {
        val call: Call<List<AnuncioModel>> = mRetrofit.favoritosUser(id)
        call.enqueue(object : Callback<List<AnuncioModel>> {
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

    fun desfavoritarProduto(idUsuario: Long, idProduto: Long, listener: ApiListener<Boolean>) {
        val call: Call<Void> = mRetrofit.desfavoritarProduto(idUsuario, idProduto)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() != HyperXpressConstants.HTTP.NOCONTENT) {
                    listener.onFailure(response.message())
                } else {
                    listener.onSuccess(true)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                listener.onFailure(t.message.toString())
            }

        })
    }

}