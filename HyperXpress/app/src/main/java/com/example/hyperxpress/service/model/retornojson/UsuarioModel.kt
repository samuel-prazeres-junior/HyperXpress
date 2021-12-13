package com.example.hyperxpress.service.model.retornojson

data class UsuarioModel(
    val id:Int,val nome: String, val avatar: String, val cpf: String,
    val email: String, val senha: String, val dataNasc:String,
    val emailConfirmado: Boolean, val estadoUf:String,
    val cep:String, val bairro:String, val logradouro:String,
    val numero:String, val cidade:String, val complemento:String

)