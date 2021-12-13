package com.example.hyperxpress.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.R
import com.example.hyperxpress.databinding.FragmentHomeBinding
import com.example.hyperxpress.service.listener.OnitemClickListener
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.model.adapter.AnuncioAdapter
import com.example.hyperxpress.viewmodel.ProdutoViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mProdutoViewModel:ProdutoViewModel
    lateinit var anuncios: List<AnuncioModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        if (container != null) {
            mProdutoViewModel = ProdutoViewModel(container.context)
            mProdutoViewModel.anunciosHome()
            observer()
        }
        return binding.root
    }


    fun inicioRecycler(anuncios: List<AnuncioModel>) {
        val recyclerView: RecyclerView = binding.rvHome
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = AnuncioAdapter(anuncios, object : OnitemClickListener{
            override fun onItemClick(anuncio: AnuncioModel) {
                val action = HomeFragmentDirections.actionNavHomeToProdutoEspecificoFragment()
                action.idProduto = anuncio.idProduto
                action.idVendedor = anuncio.usuarioProdutoId
                action.nivelDestaqueVendedor = anuncio.estrelas.toInt()
                action.nomeProduto = anuncio.nomeProduto
                action.nomeVendedor = anuncio.usuarioProdutoAvatar
                action.precoProduto = anuncio.precoProduto.toFloat()
                action.trocavel = anuncio.trocavel
                action.marca = anuncio.marca
                action.tamanho = anuncio.tamanho
                action.descricaoProduto = anuncio.descricaoProduto
                findNavController().navigate(action)
            }


        })
        recyclerView.adapter = adapter
    }

    fun irTelaFiltros(){
        findNavController().navigate(R.id.action_nav_home_to_buscaProdutoFragment)
    }

    fun observer() {
        activity?.let {
            mProdutoViewModel.anuncios.observe(it) { anuncio ->
                if (anuncio.isNotEmpty()) {
                    anuncios = anuncio
                    inicioRecycler(anuncios)
                }
            }
        }
    }

}