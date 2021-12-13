package com.example.hyperxpress.service.repository.remote

import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.model.entity.kotlin.EnderecoModel
import com.example.hyperxpress.service.model.retornojson.UsuarioModel
import com.example.hyperxpress.service.model.adapter.UsuarioAdapter
import com.example.hyperxpress.service.model.entity.kotlin.ImagemBase64
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.model.retornojson.InfoVendedor
import com.example.hyperxpress.service.model.retornojson.LoginModel
import retrofit2.Call
import retrofit2.http.*

interface UserService {
    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @POST("usuarios/login")
    fun login (@Body login: LoginModel): Call<UsuarioAdapter>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @POST("usuarios")
    fun cadastroUser(@Body user: UsuarioModel): Call<UsuarioModel>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @POST("usuarios/imagem/{idUsuario}")
    fun cadastroImagemUser(@Body imagem: ImagemBase64, @Path("idUsuario")idUsuario:Long): Call<Void>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @GET("{cep}/json/")
    fun buscarEndereco(@Path("cep") cep:String): Call<EnderecoModel>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @GET("views/product-screen/product-vendedor-view/{idUsuario}")
    fun buscarInfoVendedor(@Path("idUsuario") idUsuario:Long):Call<InfoVendedor>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @PUT("usuarios/{idUsuario}")
    fun atualizarUser(@Path("idUsuario")idUsuario:Long, @Body user: UsuarioModel): Call<Void>

    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @GET("usuarios/imagem/{id}/")
    fun imagemUsuario(@Path("id") idProduto: Long): Call<ImagemBase64>

}