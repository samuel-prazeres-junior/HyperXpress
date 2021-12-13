package com.example.hyperxpress.service.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.R
import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.generics.Generics
import com.example.hyperxpress.service.listener.OnitemClickListener
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.viewmodel.ProdutoViewModel
import com.example.hyperxpress.viewmodel.UsuarioViewModel
import com.squareup.picasso.Picasso

class AnuncioAdapter(val anuncios: List<AnuncioModel>, val onClickItem: OnitemClickListener) :
    RecyclerView.Adapter<AnuncioAdapter.AnuncioHolder>() {
    class AnuncioHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun render(anuncio: AnuncioModel, onClickItem: OnitemClickListener) {
            val mProdutoViewModel = ProdutoViewModel(view.context)
            val mUsuarioViewModel = UsuarioViewModel(view.context)

            val ivUsuario: ImageView = view.findViewById(R.id.icone_user)
            val ivProduto: ImageView = view.findViewById(R.id.iv_imagem_produto)
            val ivTrocavel: ImageView = view.findViewById(R.id.iv_produto_trocavel)
            val tvNomeProduto: TextView = view.findViewById(R.id.tv_home_nome_produto)
            val tvPrecoProduto: TextView = view.findViewById(R.id.tv_home_preco_produto)
            val nomeUsuario: TextView = view.findViewById(R.id.nome_user)

            val estrelas: List<ImageView> = listOf(
                view.findViewById<ImageView>(R.id.estrela_um),
                view.findViewById<ImageView>(R.id.estrela_dois),
                view.findViewById<ImageView>(R.id.estrela_trez),
                view.findViewById<ImageView>(R.id.estrela_quatro),
                view.findViewById<ImageView>(R.id.estrela_cinco)
            )

            tvNomeProduto.text = anuncio.nomeProduto
            tvPrecoProduto.text = "R$${anuncio.precoProduto}"
            nomeUsuario.text = anuncio.usuarioProdutoAvatar
            Generics.estrelasVisiveis(estrelas, anuncio.estrelas.toInt())

            mProdutoViewModel.pegarImagemProduto(anuncio.idProduto.toLong(),1, ivProduto)
            mUsuarioViewModel.pegarImagemUsuario(anuncio.usuarioProdutoId.toLong(), ivUsuario)



            if (ivUsuario.drawable == null) {
                ivUsuario.setImageResource(R.drawable.icone_usuario)
            }
            if (anuncio.trocavel) {
                ivTrocavel.visibility = view.visibility
            }
            view.setOnClickListener {
                onClickItem.onItemClick(anuncio)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnuncioHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AnuncioHolder(layoutInflater.inflate(R.layout.anuncio_home, parent, false))
    }

    override fun onBindViewHolder(holder: AnuncioHolder, position: Int) {
        holder.render(anuncios[position], onClickItem)
    }

    override fun getItemCount(): Int = anuncios.size
}