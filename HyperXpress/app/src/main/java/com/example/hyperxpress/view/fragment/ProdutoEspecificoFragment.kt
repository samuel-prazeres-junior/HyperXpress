package com.example.hyperxpress.view.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.hyperxpress.R
import com.example.hyperxpress.databinding.FragmentAdicionarProdutoBinding
import com.example.hyperxpress.databinding.FragmentProdutoEspecificoBinding
import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.viewmodel.CarrinhoViewModel
import com.example.hyperxpress.viewmodel.ProdutoViewModel
import com.squareup.picasso.Picasso

class ProdutoEspecificoFragment : Fragment() {
    private lateinit var binding: FragmentProdutoEspecificoBinding
    private lateinit var mProdutoViewModel: ProdutoViewModel
    private lateinit var mCarrinhoViewModel: CarrinhoViewModel
    val argumentos by navArgs<ProdutoEspecificoFragmentArgs>()
    private lateinit var idUsuario: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProdutoEspecificoBinding.inflate(layoutInflater, container, false)
        if (container != null) {
            mProdutoViewModel = ProdutoViewModel(container.context)
            mCarrinhoViewModel = CarrinhoViewModel(container.context)
            idUsuario =
                SecurityPreferences.get(SecurityPreferences.sharePrefences(container.context), "id")
        }
        atribuirValoresComponentesLayout()
        binding.ivFavoritoPrudutoEspecifico.setOnClickListener { favoritarProduto() }
        binding.ivCarrinhoProdutoEspecifico.setOnClickListener { adicionarProdutoCarrinho() }
        observerFavoritarProduto()
        observerProdutoAdicionadoCarrinho()

        binding.ivImagemProdutoEspecifico1.setOnClickListener { mudarImagem(binding.ivImagemProdutoEspecifico1.drawable) }
        binding.ivImagemProdutoEspecifico2.setOnClickListener { mudarImagem(binding.ivImagemProdutoEspecifico2.drawable) }
        binding.ivImagemProdutoEspecifico3.setOnClickListener { mudarImagem(binding.ivImagemProdutoEspecifico3.drawable) }
        binding.ivImagemProdutoEspecifico4.setOnClickListener { mudarImagem(binding.ivImagemProdutoEspecifico4.drawable) }
        return binding.root
    }

    fun mudarImagem(imagem:Drawable){
        binding.ivImagemProdutoEspecifico.setImageDrawable(imagem)
    }
    fun atribuirValoresComponentesLayout() {
        mProdutoViewModel.pegarImagemProduto(argumentos.idProduto.toLong(), 1, binding.ivImagemProdutoEspecifico)
        mProdutoViewModel.pegarImagemProduto(argumentos.idProduto.toLong(), 1, binding.ivImagemProdutoEspecifico1)
        mProdutoViewModel.pegarImagemProduto(argumentos.idProduto.toLong(), 2, binding.ivImagemProdutoEspecifico2)
        mProdutoViewModel.pegarImagemProduto(argumentos.idProduto.toLong(), 3, binding.ivImagemProdutoEspecifico3)
        mProdutoViewModel.pegarImagemProduto(argumentos.idProduto.toLong(), 4, binding.ivImagemProdutoEspecifico4)
        produtoIsTrocavel()
        binding.tvNomeProdutoEspecifico.text = argumentos.nomeProduto
        binding.tvPrecoProdutoEspecifico.text = argumentos.precoProduto.toString()
        binding.tvValorMarcaProdutoEspecifico.text = argumentos.marca
        binding.tvValorTamanhoProdutoEspecifico.text = argumentos.tamanho
        binding.tvValorDescricaoProdutoEspecifico.text = argumentos.descricaoProduto

    }

    fun produtoIsTrocavel() {
        if (argumentos.trocavel) {
            binding.ivProdutoTrocavelProdutoEspecifico.visibility = View.VISIBLE
        }
    }

    fun adicionarProdutoCarrinho() {
        mCarrinhoViewModel.adicionarProdutoCarrinho(
            argumentos.idProduto.toLong(),
            idUsuario.toLong()
        )
    }

    fun observerProdutoAdicionadoCarrinho(){
        activity?.let { mCarrinhoViewModel.respostaAdicionarCarriinho.observe(it, {
            if (it){
                Toast.makeText(context,
                    context?.getString(R.string.produto_adicionado_carrinho), Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context, context?.getString(R.string.erro_inesperado), Toast.LENGTH_SHORT).show()
            }
        }) }
    }

    fun favoritarProduto() {
        binding.ivFavoritoPrudutoEspecifico.setImageResource(R.drawable.icone_produto_favoritado)
        mProdutoViewModel.favoritarProduto(idUsuario.toLong(), argumentos.idProduto.toLong())
    }

    fun observerFavoritarProduto(){
        activity?.let {
            mProdutoViewModel.itemCadastrado.observe(it, {
                if (!it){
                    binding.ivFavoritoPrudutoEspecifico.setImageResource(R.drawable.icone_favorito)
                    Toast.makeText(context, R.string.erro_inesperado, Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

}