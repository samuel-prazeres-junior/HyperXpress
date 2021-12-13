package com.example.hyperxpress.view.fragment

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.R
import com.example.hyperxpress.databinding.FragmentCarrinhoBinding
import com.example.hyperxpress.service.listener.OnitemClickListener
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.model.adapter.CarrinhoAdapter
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.viewmodel.CarrinhoViewModel

class CarrinhoFragment : Fragment() {

    private lateinit var binding:FragmentCarrinhoBinding
    private lateinit var mCarrinhoViewModel:CarrinhoViewModel
    private lateinit var idUsuario:String
    private lateinit var mSharePreferences:SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarrinhoBinding.inflate(layoutInflater, container, false)
        if (container != null) {
            mCarrinhoViewModel = CarrinhoViewModel(binding.root.context)
            mSharePreferences = SecurityPreferences.sharePrefences(container.context)
            idUsuario = SecurityPreferences.get(mSharePreferences, "id")
            mCarrinhoViewModel.carrinhoUsuario(idUsuario.toLong())
        }
        observer()
        observerRemocao()
        return binding.root
    }

    fun inicioRecycler(carrinho: List<AnuncioModel>) {
        val recyclerView: RecyclerView = binding.rvCarrinho
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CarrinhoAdapter(carrinho, object : OnitemClickListener {
            override fun onItemClick(anuncio: AnuncioModel) {
                AlertDialog.Builder(context)
                    .setTitle("Remover Item")
                    .setMessage("Deseja mesmo remover o item")
                    .setPositiveButton("Sim") { dialog, which ->
                        mCarrinhoViewModel.removerItemCarrinho(anuncio.idProduto.toLong(), idUsuario.toLong())
                    }
                    .setNeutralButton("Não", null)
                    .show()
            }


        })
        recyclerView.adapter = adapter
    }

    fun observer(){
        activity?.let {
            mCarrinhoViewModel.listaCarrinho.observe(it,{
                inicioRecycler(it)
                binding.tvValorTotalCarrinho.text = totalCarringo(it).toString()
                atualizarValorTotalCarrinho()
            })
        }
    }
    fun observerRemocao(){
        activity?.let {
            mCarrinhoViewModel.respostaRemocaoCarriinho.observe(it, {
                if (it){
                    Toast.makeText(context, context?.getString(R.string.item_removido), Toast.LENGTH_SHORT).show()
                    mCarrinhoViewModel.carrinhoUsuario(idUsuario.toLong())
                }
            })
        }
    }

    fun atualizarValorTotalCarrinho(){}
    fun totalCarringo(itens:List<AnuncioModel>):Double{
        var total = 0.0
        for (itemCarrinho in itens){
            total += itemCarrinho.precoProduto
        }
        return total
    }
}