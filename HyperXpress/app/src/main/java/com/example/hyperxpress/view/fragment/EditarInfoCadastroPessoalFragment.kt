package com.example.hyperxpress.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hyperxpress.R
import com.example.hyperxpress.databinding.FragmentEditarInfoCadastroPessoalBinding
import com.example.hyperxpress.service.model.retornojson.EnderecoUsuarioModel
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.viewmodel.EnderecoViewModel

class EditarInfoCadastroPessoalFragment : Fragment() {

    private lateinit var mSharePreferences: SharedPreferences
    lateinit var infoUsuario: EnderecoUsuarioModel
    private lateinit var enderecoViewModel: EnderecoViewModel
    lateinit var  binding: FragmentEditarInfoCadastroPessoalBinding
    private lateinit var idUsuario: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

            activity?.let {  mSharePreferences = SecurityPreferences.sharePrefences(it) }
            idUsuario = SecurityPreferences.get(mSharePreferences, getString(R.string.cache_id))
            activity?.let { enderecoViewModel =  EnderecoViewModel(it) }
            if (idUsuario.isNotEmpty()){
                enderecoViewModel.enderecoUsuario(idUsuario.toLong())
            }

        binding = FragmentEditarInfoCadastroPessoalBinding.inflate(layoutInflater,container, false)
        binding.btnTelaEditarEndereco.setOnClickListener { editarEndereco() }
        observer()
        return binding.root
    }

    private fun editarEndereco() {
        val action =
            com.example.hyperxpress.view.fragment.EditarInfoCadastroPessoalFragmentDirections.actionNavEditarCadastroPessoalToEditarInfoCadastroEnderecoFragment()
        action.idUsuario = idUsuario.toLong()
        action.nome = binding.inputEditarNome.text.toString()
        action.cpf = infoUsuario.codigoUsuario.cpf
        action.dataNasc = infoUsuario.codigoUsuario.dataNasc
        action.avatar = binding.inputEditarAvatar.text.toString()
        action.email = binding.inputEditarEmail.text.toString()
        action.senha = binding.inputEditarSenha.text.toString()
        action.cep = infoUsuario.cep
        action.estado = infoUsuario.estadoUf
        action.cidade = infoUsuario.cidade
        action.endereco = infoUsuario.logradouro
        action.bairro = infoUsuario.bairro
        action.numero = infoUsuario.numero
        action.complemento = infoUsuario.complemento
        findNavController().navigate(action)
    }

    fun observer(){
        if (::enderecoViewModel.isInitialized){

            enderecoViewModel.cadstroEnderecoSucess.observe(viewLifecycleOwner){
                if (it.sucess()){
                    infoUsuario = enderecoViewModel.enderecoModel

                    binding.inputEditarNome.setText(infoUsuario.codigoUsuario.avatar)
                    binding.inputEditarAvatar.setText(infoUsuario.codigoUsuario.avatar)
                    binding.inputEditarEmail.setText(infoUsuario.codigoUsuario.email)
                    binding.inputEditarSenha.setText(infoUsuario.codigoUsuario.senha)
                }
                else{
                    Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

}