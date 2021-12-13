package com.example.firebase.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.R
import com.example.hyperxpress.service.listener.OnitemClickListenerFirebase
import com.example.hyperxpress.service.model.entity.java.Contact

import com.squareup.picasso.Picasso

class UltimosContactsAdapter(val contacts: List<Contact>, val onItemClickListener: OnitemClickListenerFirebase<Contact>):RecyclerView.Adapter<UltimosContactsAdapter.ContactsHolder>() {
    class ContactsHolder(val view:View): RecyclerView.ViewHolder(view){
        fun render(contact:Contact, onItemClickListener: OnitemClickListenerFirebase<Contact>){
            val ivUsuario: ImageView = view.findViewById(R.id.contacts_image_user)
            val tvUsuario: TextView = view.findViewById(R.id.tv_nome_user)
            val tvMessage: TextView = view.findViewById(R.id.tv_ultima_message)

            Picasso.get().load(contact.photoUrl).into(ivUsuario)
            tvUsuario.text = contact.username
            tvMessage.text = contact.latestMessage

            view.setOnClickListener { onItemClickListener.onItemClick(contact) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ContactsHolder(layoutInflater.inflate(R.layout.item_user_message, parent, false))
    }

    override fun onBindViewHolder(holder: ContactsHolder, position: Int) {
        holder.render(contacts[position], onItemClickListener)
    }

    override fun getItemCount(): Int = contacts.size
}