package com.example.hyperxpress.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.adapters.ContactsAdapter
import com.example.hyperxpress.R
import com.example.hyperxpress.service.listener.OnitemClickListenerFirebase
import com.example.hyperxpress.service.model.entity.java.Contact
import com.example.hyperxpress.service.model.entity.java.User
import com.google.firebase.firestore.*

class ContactsActivity : AppCompatActivity() {
    private lateinit var rvContacts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        rvContacts = findViewById(R.id.rv_contacts)
        fechUsers()
    }
    fun fechUsers(){
        FirebaseFirestore.getInstance().collection("/users")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        error.message?.let { Log.e("Teste", it) }
                    }
                    else {
                        val listaUser = mutableListOf<User>()
                        val docs:List<DocumentSnapshot> = value!!.documents
                        docs.forEach {
                            val user: User? = it.toObject(User::class.java)
                            user?.let { it1 -> listaUser.add(it1) }
                        }
                        inicioRecycler(listaUser)
                    }
                }
            })
    }
    fun inicioRecycler(contacts: List<User>){
        rvContacts.layoutManager = LinearLayoutManager(this)
        val adapter = ContactsAdapter(contacts, object : OnitemClickListenerFirebase<User> {
            override fun onItemClick(contact: User) {
                val intent = Intent(this@ContactsActivity, ChatActivity::class.java)
                intent.putExtra("uid", contact.uuid)
                intent.putExtra("username", contact.username)
                intent.putExtra("profileurl", contact.profileurl)
                startActivity(intent)
            }


        })
        rvContacts.adapter = adapter
    }
}