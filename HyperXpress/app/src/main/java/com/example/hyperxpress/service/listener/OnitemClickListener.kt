package com.example.hyperxpress.service.listener

import com.example.hyperxpress.service.model.retornojson.AnuncioModel

interface OnitemClickListener {
    fun  onItemClick(anuncio: AnuncioModel)
}