package com.example.hyperxpress.view.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.example.hyperxpress.R
import com.example.hyperxpress.service.generics.Generics
import com.example.hyperxpress.viewmodel.UsuarioViewModel
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher
import java.lang.Exception
import java.text.SimpleDateFormat

class CadastroPessoalActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var usuario: UsuarioViewModel
    private lateinit var editNome: EditText
    private lateinit var editAvatar: EditText
    private lateinit var editCpf: EditText
    private lateinit var editEmail: EditText
    private lateinit var editDataNasc: EditText
    private lateinit var editSenha: EditText
    private lateinit var telefone: EditText
    private lateinit var editConfSenha: EditText
    private lateinit var mSelected:Uri
    private lateinit var btnFoto: Button
    private lateinit var mImageFoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_pessoal)
        instanciarEditText()
        btnFoto = findViewById(R.id.btn_selected_foto)
        usuario = UsuarioViewModel(application)
        mImageFoto = findViewById(R.id.image_foto)
        btnFoto.setOnClickListener { selectedFoto() }
        val smf = SimpleMaskFormatter("(NN)NNNNN-NNNN")
        val mtw = MaskTextWatcher(telefone, smf)
        telefone.addTextChangedListener(mtw)

    }
    fun selectedFoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (data != null) {
                mSelected = data.data!!
                var bit: Bitmap? = null
                try {
                    bit = MediaStore.Images.Media.getBitmap(contentResolver, mSelected)
                    mImageFoto.setImageBitmap(bit)
                    btnFoto.alpha = 0F
                }catch (e: Exception){}
            }
        }
    }

    fun cadastroPessoal(view: View) {

        val cadastroPessoalComSucesso =
            usuario.cadastrarInfoPessoais(
                mImageFoto,
                editNome,
                editAvatar,
                editCpf,
                editEmail,
                editDataNasc,
                editSenha,
                editConfSenha
            )
            if (cadastroPessoalComSucesso){
                val dataconvertida = editDataNasc.text.toString().split("-")
                val data = "${dataconvertida[2]}-${dataconvertida[1]}-${dataconvertida[0]}"
                val string64 = Generics.converterImagemEmStringBase64(mImageFoto.drawable.toBitmap())

//            val dataformate = SimpleDateFormat("yyyy/MM/dd")
//            val data = dataformate.format(editDataNasc.text.toString())
                val detalhesCadastro = Intent(this, CadastroEnderecoActivity::class.java)
                detalhesCadastro.putExtra(getString(R.string.cache_nome), editNome.text.toString())
                detalhesCadastro.putExtra(getString(R.string.cache_avatar), editAvatar.text.toString())
                detalhesCadastro.putExtra(getString(R.string.cache_cpf), editCpf.text.toString())
                detalhesCadastro.putExtra(getString(R.string.cache_email), editEmail.text.toString())
                detalhesCadastro.putExtra(getString(R.string.cache_data_nasc), data)
                detalhesCadastro.putExtra(getString(R.string.cache_profile), mSelected)
                detalhesCadastro.putExtra(getString(R.string.cache_senha), editSenha.text.toString())
                detalhesCadastro.putExtra("imagem64",string64)
                startActivity(detalhesCadastro)
            }

    }

    fun instanciarEditText() {
        editNome = findViewById(R.id.inputNome)
        editAvatar = findViewById(R.id.input_avatar)
        editCpf = findViewById(R.id.input_cpf)
        editEmail = findViewById(R.id.input_email)
        editDataNasc = findViewById(R.id.input_data_nascimento)
        telefone = findViewById(R.id.input_celular)
        editSenha = findViewById(R.id.input_senha)
        editConfSenha = findViewById(R.id.input_confirmar_senha)
    }

    fun calendario(view: View) {
        usuario.abrirCalendario(this, this)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        editDataNasc.setText(usuario.pegarDataCalendario(year, month, day));
    }

    fun visualizarSenha(view: View) {
        when (view.id) {
            R.id.imagem_senha -> Generics.visualizarEsconderSenha(editSenha)
            R.id.imagem_conf_senha -> Generics.visualizarEsconderSenha(editConfSenha)
        }
    }

}