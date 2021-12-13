package com.example.hyperxpress.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.hyperxpress.service.listener.ApiListener
import com.example.hyperxpress.service.listener.ValidationListener
import com.example.hyperxpress.service.model.retornojson.EnderecoUsuarioModel
import com.example.hyperxpress.service.model.retornojson.ListaEnderecos
import com.example.hyperxpress.service.repository.remote.EnderecoRepository

class EnderecoViewModel(context: Context){
    private val mEnderecoRepository = EnderecoRepository(context)
    private val mCadastroEnderecoSucesso = MutableLiveData<ValidationListener>()
    var cadstroEnderecoSucess:MutableLiveData<ValidationListener> = mCadastroEnderecoSucesso
    lateinit var enderecoModel: EnderecoUsuarioModel


    fun enderecoUsuario(id:Long){
        mEnderecoRepository.enderecoUser(id, object : ApiListener<EnderecoUsuarioModel>{
            override fun onSuccess(model: EnderecoUsuarioModel) {
                enderecoModel = model
                mCadastroEnderecoSucesso.value = ValidationListener()
            }

            override fun onFailure(str: String) {
                mCadastroEnderecoSucesso.value = ValidationListener(str)
            }

        })

    }
}