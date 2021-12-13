package com.example.hyperxpress.service.model.retornojson

import com.example.hyperxpress.service.model.adapter.UsuarioAdapter
import com.google.gson.annotations.SerializedName

data class EnderecoUsuarioModel(val estadoUf:String , val cep:String, val bairro:String, val logradouro:String, val numero:String, val cidade:String,val complemento:String ,val codigoUsuario:UsuarioAdapter)