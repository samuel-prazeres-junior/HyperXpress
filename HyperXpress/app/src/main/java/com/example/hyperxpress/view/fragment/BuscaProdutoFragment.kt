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
import com.example.hyperxpress.service.listener.OnitemClickListener
import com.example.hyperxpress.service.model.adapter.AnuncioAdapter
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.viewmodel.ProdutoViewModel

class BuscaProdutoFragment : Fragment() {
    private lateinit var rvFiltro:RecyclerView

    private lateinit var mProdutoViewModel: ProdutoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_busca_produto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFiltro = view.findViewById(R.id.rv_filtros)
        mProdutoViewModel = ProdutoViewModel(view.context)
        mProdutoViewModel.anunciosHome()
        observer()
    }

    fun inicioRecycler(anuncios: List<AnuncioModel>) {

        rvFiltro.layoutManager = LinearLayoutManager(context)
        val adapter = AnuncioAdapter(anuncios, object : OnitemClickListener {
            override fun onItemClick(anuncio: AnuncioModel) {
                val action = BuscaProdutoFragmentDirections.actionBuscaProdutoFragmentToProdutoEspecificoFragment()
                action.idProduto = anuncio.idProduto
                action.idVendedor = anuncio.usuarioProdutoId
                action.nivelDestaqueVendedor = anuncio.estrelas.toInt()
                action.nomeProduto = anuncio.nomeProduto
                action.nomeVendedor = anuncio.usuarioProdutoAvatar
                action.precoProduto = anuncio.precoProduto.toFloat()
                action.trocavel = anuncio.trocavel
                findNavController().navigate(action)
            }


        })
        rvFiltro.adapter = adapter
    }
    fun observer() {
        activity?.let {
            mProdutoViewModel.anuncios.observe(it) { anuncio ->
                if (anuncio.isNotEmpty()) {
                    inicioRecycler(anuncio)
                }
            }
        }
    }
}