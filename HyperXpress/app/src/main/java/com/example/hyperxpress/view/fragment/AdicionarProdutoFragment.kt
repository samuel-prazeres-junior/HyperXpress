package com.example.hyperxpress.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hyperxpress.R
import com.example.hyperxpress.databinding.FragmentAdicionarProdutoBinding
import com.example.hyperxpress.service.generics.Generics

class AdicionarProdutoFragment : Fragment() {

    lateinit var binding: FragmentAdicionarProdutoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdicionarProdutoBinding.inflate(layoutInflater, container, false)
        binding.spinnerCategoria
        binding.btnTelaAdicionarFotos.setOnClickListener { tirarFotoProduto() }
        return binding.root
    }

    private fun tirarFotoProduto() {

        val stringErro = getString(R.string.minimo_caracteres_adicionar_produto)

        if (Generics.verificarTamanhoString(binding.editAdicionarNomeProduto,stringErro, 3) &&
            Generics.verificarTamanhoString(binding.editAdicionarMarcaProduto, stringErro, 3) &&
            verificarPrecoPositivo(binding.editAdicionarPrecoProduto)){

            val action = AdicionarProdutoFragmentDirections.actionNavAdicioarProdutoToNavAdicionarFotosProduto()
            action.nome = binding.editAdicionarNomeProduto.text.toString()
            action.marca = binding.editAdicionarMarcaProduto.text.toString()
            action.preco = binding.editAdicionarPrecoProduto.text.toString()
            action.tamanho = binding.spinnerTamanhoProduto.selectedItem.toString()
            action.categoria = binding.spinnerCategoria.selectedItem.toString()
            action.genero = binding.spinnerGenero.selectedItem.toString()

            findNavController().navigate(action)
        }


    }
    fun verificarPrecoPositivo(editTex:EditText): Boolean{
        if (editTex.text.isNotEmpty()){
            return editTex.text.toString().toDouble() > 0.0
        }
        else{
            editTex.error = "Valor Inválido"
            return false
        }
    }

}