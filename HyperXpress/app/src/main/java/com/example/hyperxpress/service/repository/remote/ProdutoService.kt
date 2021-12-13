package com.example.hyperxpress.service.repository.remote

import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.model.entity.kotlin.ImagemBase64
import com.example.hyperxpress.service.model.entity.kotlin.Produto
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.model.retornojson.AnunciosComPageModel
import retrofit2.Call
import retrofit2.http.*


interface ProdutoService {

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @GET("views/product-screen/product-list-view?page=0&size=100&statusProduto=ATIVO")
    fun anuncios (): Call<AnunciosComPageModel>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @POST("produtos")
    fun adicionarProduto(@Body produto: Produto): Call<Produto>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @PUT("produtos/imagem/{idProduto}")
    fun adicionarImagemProduto(@Body imagem: ImagemBase64, @Path("idProduto") idProduto:Long): Call<Void>
    
    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @GET("favoritos/{idUsuario}")
    fun favoritosUser (@Path("idUsuario") idUsuario:Long): Call<List<AnuncioModel>>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @POST("favoritos/{idUsuario}/{idProduto}")
    fun favoritarProduto(@Path("idUsuario")idUsuario:Long,
                         @Path("idProduto")idProduto:Long): Call<AnuncioModel>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @DELETE("favoritos/{idUsuario}/{idProduto}")
    fun desfavoritarProduto(@Path("idUsuario")idUsuario:Long, @Path("idProduto")idProduto:Long): Call<Void>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @GET("produtos/imagem/{id}/{imagemEspecifica}")
    fun imagemProduto(@Path("id") idProduto: Long, @Path("imagemEspecifica") imagem:Long): Call<ImagemBase64>


}