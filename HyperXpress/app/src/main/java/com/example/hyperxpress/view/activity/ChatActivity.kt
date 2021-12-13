package com.example.hyperxpress.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hyperxpress.R
import com.example.hyperxpress.service.model.adapter.MessageAdapter
import com.example.hyperxpress.service.model.entity.java.Contact
import com.example.hyperxpress.service.model.entity.java.Message
import com.example.hyperxpress.service.model.entity.java.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class ChatActivity : AppCompatActivity() {
    private lateinit var rvMensagem: RecyclerView
    private lateinit var editMessage: EditText
    private lateinit var idUsuarioRecebeMensagem: String
    private lateinit var usernameUsuarioRecebeMensagem: String
    private lateinit var urlFotoUsuarioRecebeMensagem: String
    private lateinit var usuarioLogado: User
    private val listaMensagens = mutableListOf<Message>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //    private lateinit var listaMensagens: MutableLiveData<Message>


//        listaMensagens = MutableLiveData()
        idUsuarioRecebeMensagem = intent.extras?.getString("id").toString()
        usernameUsuarioRecebeMensagem = intent.extras?.getString("username").toString()
        urlFotoUsuarioRecebeMensagem = intent.extras?.getString("profileurl").toString()
        supportActionBar?.title = usernameUsuarioRecebeMensagem
        rvMensagem = findViewById(R.id.rv_chat)
        editMessage = findViewById(R.id.edit_chat)
        val btnChat: Button = findViewById(R.id.button_enviar)
        btnChat.setOnClickListener { sendMessage() }

        FirebaseFirestore.getInstance().collection("/users")
            .document(FirebaseAuth.getInstance().uid.toString())
            .get()
            .addOnSuccessListener {
                usuarioLogado = it.toObject(User::class.java)!!
                fetchMessages()
            }

    }


    fun fetchMessages() {
        if (usuarioLogado != null) {
            val fromId = usuarioLogado.uuid
            val toId = idUsuarioRecebeMensagem

            FirebaseFirestore.getInstance().collection("/conversations")
                .document(fromId)
                .collection(toId)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        val documentCahnges = value?.documentChanges
                        if (!documentCahnges.isNullOrEmpty()) {
                            documentCahnges.forEach {
                                if (it.type == DocumentChange.Type.ADDED) {
                                    val mensagem = it.document.toObject(Message::class.java)
                                    listaMensagens.add(mensagem)
                                }
                            }
                        }
                        inicioRecycler(listaMensagens)

                    }
                })


        }
    }

    fun inicioRecycler(mensagens: List<Message>) {
        val layoutManager = LinearLayoutManager(this)
//        layoutManager.reverseLayout = true  // invertendo a ordem de exibição das mensagens
//        layoutManager.stackFromEnd = true  //  da ultima para a primeira
        rvMensagem.layoutManager = layoutManager
        rvMensagem.adapter = MessageAdapter(mensagens, usuarioLogado.profileurl, urlFotoUsuarioRecebeMensagem)
    }

    fun sendMessage() {
        val txtMensagem = editMessage.text.toString()
        editMessage.setText("")
        val fromId = FirebaseAuth.getInstance().uid
        val toId = idUsuarioRecebeMensagem
        val timestamp: Long = System.currentTimeMillis()
        val mensagem = fromId?.let { Message(txtMensagem, timestamp, it, toId) }
        if (mensagem != null && !mensagem.messagem.isEmpty()) {
            if (fromId != null && toId != null) {
                FirebaseFirestore.getInstance().collection("/conversations")
                    .document(fromId)
                    .collection(toId)
                    .add(mensagem)
                    .addOnSuccessListener {
                        Log.d("Teste", it.id)
                        val contact = Contact(
                            toId,
                            usernameUsuarioRecebeMensagem,
                            mensagem.messagem,
                            mensagem.timestamp,
                            urlFotoUsuarioRecebeMensagem
                        )
                        FirebaseFirestore.getInstance().collection("/last-messages")
                            .document(fromId)
                            .collection("contacts")
                            .document(toId)
                            .set(contact)
                    }
                    .addOnFailureListener {
                        it.message?.let { it1 -> Log.e("Teste", it1) }
                    }


                // daqui para baixo é ta replicando a msg para quem ta recebendo
                FirebaseFirestore.getInstance().collection("/conversations")
                    .document(toId)
                    .collection(fromId)
                    .add(mensagem)
                    .addOnSuccessListener {
                        Log.d("Teste", it.id)
                        val contact = Contact(
                            usuarioLogado.uuid,
                            usuarioLogado.username,
                            mensagem.messagem,
                            mensagem.timestamp,
                            usuarioLogado.profileurl
                        )
                        FirebaseFirestore.getInstance().collection("/last-messages")
                            .document(toId)
                            .collection("contacts")
                            .document(fromId)
                            .set(contact)
                    }
                    .addOnFailureListener {
                        it.message?.let { it1 -> Log.e("Teste", it1) }
                    }


            }
        }
    }
}