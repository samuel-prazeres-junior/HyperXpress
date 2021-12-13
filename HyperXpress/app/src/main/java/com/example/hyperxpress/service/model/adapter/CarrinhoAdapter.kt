package com.example.hyperxpress.service.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.R
import com.example.hyperxpress.service.constants.HyperXpressConstants
import com.example.hyperxpress.service.generics.Generics
import com.example.hyperxpress.service.listener.OnitemClickListener
import com.example.hyperxpress.service.model.retornojson.AnuncioModel
import com.example.hyperxpress.viewmodel.ProdutoViewModel
import com.squareup.picasso.Picasso

class CarrinhoAdapter (val itensCarrinho: List<AnuncioModel>, val onClickItem: OnitemClickListener): RecyclerView.Adapter<CarrinhoAdapter.CarrinhoHolder>(){
        class CarrinhoHolder(val view: View): RecyclerView.ViewHolder(view){
            val produtoViewModel = ProdutoViewModel(view.context)
            fun render(item: AnuncioModel, onClickItem: OnitemClickListener){
                val ivProduto: ImageView = view.findViewById(R.id.iv_image_produto_carrinho)
                val tvNomeProduto: TextView = view.findViewById(R.id.tv_nome_produto_carrinho)
                val tvdescricao: TextView = view.findViewById(R.id.tv_descricao_produto_vendedor)
                val tvPrecoProduto: TextView = view.findViewById(R.id.tv_preco_produto_carrinho)
                val nomeUsuario: TextView = view.findViewById(R.id.tv_nome_vendedor_produto)
                val checkbox: CheckBox = view.findViewById(R.id.cb_produto_checado)
                val idProduto:String = item.idProduto.toString()


                val estrelas:List<ImageView> = listOf(view.findViewById<ImageView>(R.id.estrela_um),
                    view.findViewById<ImageView>(R.id.estrela_dois),
                    view.findViewById<ImageView>(R.id.estrela_trez),
                    view.findViewById<ImageView>(R.id.estrela_quatro),
                    view.findViewById<ImageView>(R.id.estrela_cinco))


                Generics.estrelasVisiveis(estrelas, item.estrelas.toInt())
//                Generics.estrelasVisiveis(estrelas, 5)
                tvPrecoProduto.text = "R$${item.precoProduto}"
                tvNomeProduto.text = item.nomeProduto
                tvPrecoProduto.text = "R$${item.precoProduto}"
                nomeUsuario.text = item.usuarioProdutoAvatar
                checkbox.isChecked = true
                produtoViewModel.pegarImagemProduto(idProduto.toLong(), 1, ivProduto)

                if(!item.trocavel){
                    tvdescricao.visibility = View.VISIBLE
                    tvdescricao.text = item.descricaoProduto
                }
                if (item.usuarioProdutoAvatar.isNullOrEmpty()){
                    nomeUsuario.visibility = View.GONE
                }
                view.setOnClickListener {
                    onClickItem.onItemClick(item)
                }

            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarrinhoAdapter.CarrinhoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CarrinhoAdapter.CarrinhoHolder(
            layoutInflater.inflate(
                R.layout.carrinho,
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: CarrinhoHolder, position: Int) {
        holder.render(itensCarrinho[position], onClickItem)
    }

    override fun getItemCount(): Int = itensCarrinho.size

}