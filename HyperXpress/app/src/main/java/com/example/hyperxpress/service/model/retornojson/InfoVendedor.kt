package com.example.hyperxpress.service.model.retornojson

data class InfoVendedor(val totalProdutosVendendo:Int, val totalPedidos:Int, val finalizados:Int,
                        val estrelas:String,val usuarioProdutoAvatar:String ,val produtos:List<AnuncioModel>)