package com.example.hyperxpress.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hyperxpress.R
import com.example.hyperxpress.databinding.FragmentEditarInfoCadastroEnderecoBinding
import com.example.hyperxpress.service.model.retornojson.UsuarioModel
import com.example.hyperxpress.viewmodel.UsuarioViewModel

class EditarInfoCadastroEnderecoFragment : Fragment() {

    private lateinit var mUsuarioViewModel: UsuarioViewModel
    private lateinit var binding: FragmentEditarInfoCadastroEnderecoBinding
    private val argumentos by navArgs<com.example.hyperxpress.view.fragment.EditarInfoCadastroEnderecoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditarInfoCadastroEnderecoBinding.inflate(layoutInflater, container, false)
        activity?.let {
            mUsuarioViewModel =  UsuarioViewModel(it)
            observerAtualizarUser()
        }
        atribuirValoresEditText()
        binding.salvar.setOnClickListener { editarInfoUsuario() }
        return binding.root
    }

    fun atribuirValoresEditText(){
        binding.inputEditarCep.setText(argumentos.cep)
        binding.inputEditarEstado.setText(argumentos.estado)
        binding.inputEditarCidade.setText(argumentos.cidade)
        binding.inputEditarEndereco.setText(argumentos.endereco)
        binding.inputEditarBairro.setText(argumentos.bairro)
        binding.inputEditarNumero.setText(argumentos.numero)

    }
    fun editarInfoUsuario(){
        val user = UsuarioModel(argumentos.idUsuario.toInt(), argumentos.nome, argumentos.avatar,
            argumentos.cpf, argumentos.email, argumentos.senha, argumentos.dataNasc, emailConfirmado = true,
            binding.inputEditarEstado.text.toString(), binding.inputEditarCep.text.toString(),
            binding.inputEditarBairro.text.toString(), binding.inputEditarEndereco.text.toString(),
            binding.inputEditarNumero.text.toString(), binding.inputEditarCidade.text.toString(),
            argumentos.complemento)
            mUsuarioViewModel.atualizarUser(argumentos.idUsuario,user)
    }
    fun observerAtualizarUser(){
        activity?.let {
            mUsuarioViewModel.atualizarUser.observe(it, {mensagem ->
                Toast.makeText(activity, mensagem, Toast.LENGTH_SHORT).show()
                if (mensagem == getString(R.string.editar_cadastro_sucesso)) {
                        findNavController().navigate(R.id.action_editarInfoCadastroEnderecoFragment_to_nav_home)
                    }
            })
        }
    }


}