package com.example.hyperxpress.service.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.R
import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.listener.OnitemClickListener
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.viewmodel.ProdutoViewModel
import com.squareup.picasso.Picasso

class FavoritosAdapter(val produtosFavoritos: List<AnuncioModel>, val onClickItem: OnitemClickListener):RecyclerView.Adapter<FavoritosAdapter.FavoritosHolder>() {
    class FavoritosHolder(val view:View):RecyclerView.ViewHolder(view){
        val produtoViewModel = ProdutoViewModel(view.context)
        fun render(produtoFavoritado:AnuncioModel, onClickItem: OnitemClickListener){
            val ivProduto: ImageView = view.findViewById(R.id.iv_image_produto_favoritos)
            val ivImageFavorito:ImageView = view.findViewById(R.id.iv_image_produto_favoritado)
            val tvNomeProduto: TextView = view.findViewById(R.id.tv_nome_produto_favorito)
            val tvPrecoProduto: TextView = view.findViewById(R.id.tv_preco_produto_favorito)
            val tvNomeVendedor: TextView = view.findViewById(R.id.tv_nome_vendedor_favorito)

            tvNomeProduto.text = produtoFavoritado.nomeProduto
            tvPrecoProduto.text = produtoFavoritado.precoProduto.toString()
            tvNomeVendedor.text = produtoFavoritado.usuarioProdutoAvatar
            produtoViewModel.pegarImagemProduto(produtoFavoritado.idProduto.toLong(), 1, ivProduto)

            ivImageFavorito.setOnClickListener {
                onClickItem.onItemClick(produtoFavoritado)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritosHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FavoritosHolder(layoutInflater.inflate(R.layout.favorito,parent,false))
    }

    override fun onBindViewHolder(holder: FavoritosHolder, position: Int) {
        holder.render(produtosFavoritos[position], onClickItem)
    }

    override fun getItemCount():Int = produtosFavoritos.size

}