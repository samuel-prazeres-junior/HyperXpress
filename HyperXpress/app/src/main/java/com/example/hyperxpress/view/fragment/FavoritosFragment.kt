package com.example.hyperxpress.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.databinding.FragmentFavoritosBinding
import com.example.hyperxpress.service.listener.OnitemClickListener
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.model.adapter.FavoritosAdapter
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.viewmodel.ProdutoViewModel

class FavoritosFragment : Fragment() {
    lateinit var binding:FragmentFavoritosBinding
    lateinit var mProdutosViewModel:ProdutoViewModel
    lateinit var idUsuario:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosBinding.inflate(layoutInflater, container, false)
        if (container != null) {
            mProdutosViewModel = ProdutoViewModel(container.context)
            idUsuario = SecurityPreferences.get(SecurityPreferences.sharePrefences(container.context), "id")
            mProdutosViewModel.produtosFavoritados(idUsuario.toLong())
            observer()
            observerDesfavoritado()
        }
        return binding.root
    }

    fun inicioRecycler(produtosFavoritados: List<AnuncioModel>) {
        val recyclerView: RecyclerView = binding.rvFavoritos
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = FavoritosAdapter(produtosFavoritados, object : OnitemClickListener {
            override fun onItemClick(anuncio: AnuncioModel) {
                mProdutosViewModel.desfavoritarProduto(idUsuario.toLong(),anuncio.idProduto.toLong())
            }

        })
        recyclerView.adapter = adapter
    }

    fun observerDesfavoritado(){
        activity?.let {
            mProdutosViewModel.itemDesfavoritado.observe(it, {
                mProdutosViewModel.produtosFavoritados(idUsuario.toLong())
            })
        }
    }
    fun observer(){
        activity?.let {activity ->
            mProdutosViewModel.favoritos.observe(activity,{listaProdutosFavoritados ->
                inicioRecycler(listaProdutosFavoritados)
            })
        }
    }
}