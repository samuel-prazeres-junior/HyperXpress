package com.example.hyperxpress.service.model.retornojson
data class AnuncioModel(val idProduto:Int, val nomeProduto:String, val precoProduto:Double,
                        val trocavel:Boolean, val descricaoProduto:String, val usuarioProdutoId:Int,
                        val usuarioProdutoAvatar:String, val estrelas:Double, val marca:String,
                        val tamanho:String)
