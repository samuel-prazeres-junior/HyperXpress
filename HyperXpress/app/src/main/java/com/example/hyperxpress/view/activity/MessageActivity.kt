package com.example.hyperxpress.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.adapters.UltimosContactsAdapter
import com.example.hyperxpress.R
import com.example.hyperxpress.service.listener.OnitemClickListenerFirebase
import com.example.hyperxpress.service.model.entity.java.Contact
import com.example.hyperxpress.service.model.entity.java.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class MessageActivity : AppCompatActivity() {
    private  var listaContatos = mutableListOf<Contact>()
    private lateinit var rvContacts: RecyclerView
    private lateinit var btnContatos: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        rvContacts = findViewById(R.id.rv_ultimos_contatos)
        verifyAuthentication()
        fetchLatestMessage()
        btnContatos = findViewById(R.id.todos_contatos)
        btnContatos.setOnClickListener { startActivity(Intent(this, ContactsActivity::class.java)) }
    }

    override fun onRestart() {
        super.onRestart()
        listaContatos.clear()
        fetchLatestMessage()
    }
    fun inicioRecycler(conacts: List<Contact>){
        rvContacts.layoutManager = LinearLayoutManager(this)
        rvContacts.adapter = UltimosContactsAdapter(conacts, object : OnitemClickListenerFirebase<Contact>{
            override fun onItemClick(contact: Contact) {
                val intent = Intent(this@MessageActivity, ChatActivity::class.java)
                intent.putExtra(getString(R.string.cache_id), contact.uuid)
                intent.putExtra(getString(R.string.cache_username), contact.username)
                intent.putExtra(getString(R.string.cache_profile), contact.photoUrl)
                startActivity(intent)
            }
        })
    }
    fun fetchLatestMessage(){
        val uidUsuarioLogado = FirebaseAuth.getInstance().uid.toString()
        FirebaseFirestore.getInstance().collection("/last-messages")
            .document(uidUsuarioLogado)
            .collection("contacts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    val documentChanges = value?.documentChanges
                    if (!documentChanges.isNullOrEmpty()){
                        documentChanges.forEach{
                            val contact = it.document.toObject(Contact::class.java)
                            listaContatos.add(contact)
                        }
                        inicioRecycler(listaContatos)
                    }
                }
            })
    }

    fun verifyAuthentication(){
        if(FirebaseAuth.getInstance().uid == null) {
            startActivity(Intent(this, AplicacaoAposLogar::class.java))
            finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_firebase,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout ){
                FirebaseAuth.getInstance().signOut()
                verifyAuthentication()

        }
        return super.onOptionsItemSelected(item)
    }
}