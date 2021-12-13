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
import com.example.hyperxpress.service.model.entity.java.User

import com.squareup.picasso.Picasso

class ContactsAdapter(val contacts: List<User>, val onItemClickListener: OnitemClickListenerFirebase<User>):RecyclerView.Adapter<ContactsAdapter.ContactsHolder>() {
    class ContactsHolder(val view:View): RecyclerView.ViewHolder(view){
        fun render(contact:User, onItemClickListener: OnitemClickListenerFirebase<User>){
            val ivUsuario: ImageView = view.findViewById(R.id.contacts_image_user)
            val tvUsuario: TextView = view.findViewById(R.id.tv_user)

            Picasso.get().load(contact.profileurl).into(ivUsuario)
            tvUsuario.text = contact.username

            view.setOnClickListener { onItemClickListener.onItemClick(contact) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ContactsHolder(layoutInflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: ContactsHolder, position: Int) {
        holder.render(contacts[position], onItemClickListener)
    }

    override fun getItemCount(): Int = contacts.size
}