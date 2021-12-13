package com.example.hyperxpress.service.repository.remote

import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.model.retornojson.EnderecoUsuarioModel
import com.example.hyperxpress.service.model.retornojson.ListaEnderecos
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface EnderecoService {
    @Headers(HyperXpressConstants.HEADER.APPLICATIONJSON)
    @GET("enderecos/{idUsuario}")
    fun enderecoUser (@Path("idUsuario")idUsuario:Long): Call<EnderecoUsuarioModel>
}