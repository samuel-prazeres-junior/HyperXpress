package com.example.hyperxpress.viewmodel

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hyperxpress.R
import com.example.hyperxpress.service.listener.ApiListener
import com.example.hyperxpress.service.model.entity.kotlin.ImagemBase64
import com.example.hyperxpress.service.model.entity.kotlin.Produto
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.repository.remote.ProdutoRepository

class ProdutoViewModel(val context: Context): ViewModel(){

    private val produtoRepository = ProdutoRepository(context)
    private val mAnuncios = MutableLiveData<List<AnuncioModel>>()
    var anuncios: LiveData<List<AnuncioModel>> = mAnuncios

    private val mFavoritos = MutableLiveData<List<AnuncioModel>>()
    var favoritos: LiveData<List<AnuncioModel>> = mFavoritos

    private val mFavoritar = MutableLiveData<Boolean>()
    var itemCadastrado: LiveData<Boolean> = mFavoritar

    private val mDesfavoritar = MutableLiveData<Boolean>()
    var itemDesfavoritado: LiveData<Boolean> = mDesfavoritar

    private val mAnunciarProduto = MutableLiveData<Produto?>()
    var anunciarProduto: LiveData<Produto?> = mAnunciarProduto

    private var mAdicionarImagemProduto = MutableLiveData<Boolean>()
    var adicionarImagemProduto: LiveData<Boolean> = mAdicionarImagemProduto

    fun anunciosHome(){
        produtoRepository.anuncios(object : ApiListener<List<AnuncioModel>>{
            override fun onSuccess(model: List<AnuncioModel>) {
                mAnuncios.value = model
            }

            override fun onFailure(str: String) {
                mAnuncios.value = listOf<AnuncioModel>()
            }

        })
    }

    fun adicionarProduto(produto: Produto){
        produtoRepository.adicionarProduto(produto, object : ApiListener<Produto> {
            override fun onSuccess(model: Produto) {
                mAnunciarProduto.value = model
            }

            override fun onFailure(str: String) {
                mAnunciarProduto.value = null
            }

        })
    }

    fun adicionarImagemProduto(imagem: ImagemBase64, idProduto: Long){
        produtoRepository.adicionarImagemProduto(imagem, idProduto, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                mAdicionarImagemProduto.value = true
            }

            override fun onFailure(str: String) {
                mAdicionarImagemProduto.value = false
            }

        })
    }

    fun pegarImagemProduto(idUsuario:Long, imagem:Long, imageView:ImageView){
        produtoRepository.imagemProduto(idUsuario, imagem ,object : ApiListener<ImagemBase64> {
            override fun onSuccess(model: ImagemBase64) {
                val array64 = Base64.decode(model.imagem,0)
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(array64,0, array64.size))
            }

            override fun onFailure(str: String) {
                imageView.setImageResource(R.drawable.icone_usuario)
            }

        })
    }

    fun favoritarProduto(idUsuario:Long, idProduto:Long){
        produtoRepository.favoritarProduto(idUsuario, idProduto, object : ApiListener<AnuncioModel>{
            override fun onSuccess(model: AnuncioModel) {
                mFavoritar.value = true
            }

            override fun onFailure(str: String) {
                mFavoritar.value = false
            }

        })
    }

    fun produtosFavoritados(idUsuario:Long){
        produtoRepository.favoritosUser(idUsuario, object : ApiListener<List<AnuncioModel>>{
            override fun onSuccess(model: List<AnuncioModel>) {
                mFavoritos.value = model
            }

            override fun onFailure(str: String) {
                mFavoritos.value = listOf()
            }

        })
    }

    fun desfavoritarProduto(idUsuario:Long, idProduto:Long){
        produtoRepository.desfavoritarProduto(idUsuario, idProduto, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                mDesfavoritar.value = true
            }

            override fun onFailure(str: String) {
                mDesfavoritar.value = false
            }

        })
    }
}