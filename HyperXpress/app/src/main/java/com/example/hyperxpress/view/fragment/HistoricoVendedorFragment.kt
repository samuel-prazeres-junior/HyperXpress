package com.example.hyperxpress.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.R
import com.example.hyperxpress.databinding.FragmentHistoricoVendedorBinding
import com.example.hyperxpress.service.listener.OnitemClickListener
import com.example.hyperxpress.service.model.adapter.CarrinhoAdapter
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.service.repository.remote.UserRepository
import com.example.hyperxpress.viewmodel.UsuarioViewModel

class HistoricoVendedorFragment : Fragment() {

    private lateinit var mUserRepository: UsuarioViewModel
    private lateinit var idUsuario:String
    lateinit var binding: FragmentHistoricoVendedorBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoricoVendedorBinding.inflate(layoutInflater, container, false)
        if (container != null) {
            mUserRepository = UsuarioViewModel(container.context)
        }
        activity?.let {idUsuario=  SecurityPreferences.get( SecurityPreferences.sharePrefences(it) , getString(R.string.cache_id)) }
        mUserRepository.buscarInfoVendedor(idUsuario.toLong())
        observerInfoVendedor()
        binding.tvHistoricoVendedorAdicionarProduto.setOnClickListener { adicionarProduto() }
        return binding.root
    }

    fun observerInfoVendedor() {
        activity?.let {
            mUserRepository.infoVendedor.observe(it, { infoVendedor ->
                if (infoVendedor != null) {
                    binding.tvHistoricoQtdPedidos.text = infoVendedor.totalPedidos.toString()
                    binding.tvHistoricoQtdPedidosFinalizados.text =
                        infoVendedor.finalizados.toString()

                    binding.tvHistoricoQtdProdutos.text =
                        infoVendedor.totalProdutosVendendo.toString()
                    inicioRecycler(infoVendedor.produtos)
                }
            })
        }
    }

    fun adicionarProduto() {
        findNavController().navigate(R.id.action_nav_historico_vendedor_to_nav_adicioar_produto)
    }

    fun inicioRecycler(produtos: List<AnuncioModel>) {
        val recyclerView: RecyclerView = binding.rvHistoricoVendedor
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CarrinhoAdapter(produtos, object : OnitemClickListener {
            override fun onItemClick(anuncio: AnuncioModel) {
            }
        })
        recyclerView.adapter = adapter
    }
}