package com.example.hyperxpress.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.hyperxpress.service.listener.ApiListener
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.repository.remote.CarrinhoRepository

class CarrinhoViewModel(val context: Context) {

    private val mCarrinhoRepository = CarrinhoRepository(context)
    private val mListaCarrinho = MutableLiveData<List<AnuncioModel>>()
    var listaCarrinho: MutableLiveData<List<AnuncioModel>> = mListaCarrinho

    private val mAdicionarCarrinho = MutableLiveData<Boolean>()
    var respostaAdicionarCarriinho: MutableLiveData<Boolean> = mAdicionarCarrinho

    private val mRemoverCarrinho = MutableLiveData<Boolean>()
    var respostaRemocaoCarriinho: MutableLiveData<Boolean> = mRemoverCarrinho


    fun carrinhoUsuario(id:Long){
        mCarrinhoRepository.carrinhoUser(id, object : ApiListener<List<AnuncioModel>>{
            override fun onSuccess(model: List<AnuncioModel>) {
                mListaCarrinho.value = model
            }

            override fun onFailure(str: String) {
                mListaCarrinho.value = listOf<AnuncioModel>()
            }

        })

    }

    fun adicionarProdutoCarrinho(idProduto:Long, idUsuario:Long){
        mCarrinhoRepository.adicionarProdutoCarrinho(idProduto,idUsuario, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                mAdicionarCarrinho.value = model
            }

            override fun onFailure(str: String) {
                mAdicionarCarrinho.value = false
            }

        })
    }

    fun removerItemCarrinho(idProduto: Long, idUsuario: Long){
        mCarrinhoRepository.removerItemCarrinho(idProduto, idUsuario, object : ApiListener<Boolean> {
            override fun onSuccess(model: Boolean) {
                mRemoverCarrinho.value = true
            }

            override fun onFailure(str: String) {
                mRemoverCarrinho.value = false
            }

        })

    }
}