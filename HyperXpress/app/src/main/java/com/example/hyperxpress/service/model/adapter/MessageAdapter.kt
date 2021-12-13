package com.example.hyperxpress.service.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.R
import com.example.hyperxpress.service.model.entity.java.Message

import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

private var mensagemFotoUserLogado: Boolean = false

class MessageAdapter(val mensagens: List<Message>, val fotoUsuarioLogado:String,
                     val fotoUsuarioAConversar: String):RecyclerView.Adapter<MessageAdapter.MessageHolder>() {
    private var indice = -1

    class MessageHolder(val view:View): RecyclerView.ViewHolder(view){
        fun render(mensagem:Message, fotoUsuarioLogado:String, fotoUsuarioAConversar: String){
            val tvMessage:TextView = view.findViewById(R.id.tv_message)
            val imageUsuarioChat: ImageView = view.findViewById(R.id.image_chat_user)
            if (mensagemFotoUserLogado){
                Picasso.get().load(fotoUsuarioLogado).into(imageUsuarioChat)
            }
            else {
                Picasso.get().load(fotoUsuarioAConversar).into(imageUsuarioChat)
            }
            tvMessage.text = mensagem.messagem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        indice++
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(mensagens[indice].fromId) {
            FirebaseAuth.getInstance().uid -> {
                mensagemFotoUserLogado = true
                MessageHolder(layoutInflater.inflate(R.layout.item_to_message, parent, false))
            }
            else ->{
                mensagemFotoUserLogado = false
                MessageHolder(layoutInflater.inflate(R.layout.item_from_message, parent, false))
        }
        }
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.render(mensagens[position], fotoUsuarioLogado, fotoUsuarioAConversar)
    }

    override fun getItemCount(): Int = mensagens.size
}