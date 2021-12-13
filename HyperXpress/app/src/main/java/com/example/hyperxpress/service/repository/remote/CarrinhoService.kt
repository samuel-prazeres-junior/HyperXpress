package com.example.hyperxpress.service.repository.remote

import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import retrofit2.Call
import retrofit2.http.*

interface CarrinhoService {
    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @GET("carrinhos/{idUsuario}")
    fun carrinhoUser (@Path("idUsuario") idUsuario:Long): Call<List<AnuncioModel>>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @POST("carrinhos/{idProduto}/{idUsuario}")
    fun adicionarProdutoCarrinho(@Path("idProduto") idProduto:Long, @Path("idUsuario") idUsuario:Long): Call<Void>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @DELETE("carrinhos/{idProduto}/{idUsuario}")
    fun removerItemCarrinho(@Path("idProduto") idProduto:Long, @Path("idUsuario") idUsuario:Long): Call<Void>
}