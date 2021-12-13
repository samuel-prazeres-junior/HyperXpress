package com.example.hyperxpress.service.model.retornojson

import com.google.gson.annotations.SerializedName

class AnunciosComPageModel {

    @SerializedName("content")
    var listaAnuncios = mutableListOf<AnuncioModel>()

//    @SerializedName("pageable")
//    var pageable = listOf<String>()
//
//    @SerializedName("totalPages")
//    var totalPages = 0
//
//    @SerializedName("totalElements")
//    var totalElements = 0
//
//    @SerializedName("last")
//    var last = false
//
//    @SerializedName("size")
//    var size = 0
//
//    @SerializedName("number")
//    var number = 0
//
//    @SerializedName("pageable")
//    var sort = listOf<String>()
//
//    @SerializedName("numberOfElements")
//    var numberOfElements = 0
//
//    @SerializedName("first")
//    var first = false
//
//    @SerializedName("empty")
//    var empty = false


}