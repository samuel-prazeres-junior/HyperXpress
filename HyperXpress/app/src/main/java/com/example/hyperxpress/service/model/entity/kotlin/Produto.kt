package com.example.hyperxpress.service.model.entity.kotlin

data class Produto (val idProduto:Long, val nomeProduto:String, val descricaoProduto:String, val precoProduto:Double,
                    val tamanhoProduto:String, val genero:String, val trocavel:Boolean, val subCategoriaId:Long,
                    val codigoUsuarioProdId:Long, val tecido:String, val marca:String,
                    val telefone: String)