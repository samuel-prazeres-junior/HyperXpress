package com.example.hyperxpress.view.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hyperxpress.R
import com.example.hyperxpress.databinding.FragmentAdicionarProdutoComFotosBinding
import com.example.hyperxpress.service.generics.Generics
import com.example.hyperxpress.service.model.entity.kotlin.ImagemBase64
import com.example.hyperxpress.service.model.entity.kotlin.Produto
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.viewmodel.ProdutoViewModel
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher

class AdicionarProdutoComFotosFragment : Fragment() {
    val REQUEST_IMAGE_CAPTURE = 1
    var qtdFotosTiradas = 1
    private lateinit var binding: FragmentAdicionarProdutoComFotosBinding
    private lateinit var mProdutoViewModel: ProdutoViewModel
    private lateinit var idProduto: String
    private var qtdFotosEnviadas = 1
    private lateinit var listaString: List<String>
    private val argumentos by navArgs<AdicionarProdutoComFotosFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdicionarProdutoComFotosBinding.inflate(layoutInflater, container, false)
        binding.iconeTirarFoto.setOnClickListener { tirarFoto() }
        binding.btnAnunciarProduto.setOnClickListener { cadastrarProduto() }
        if (container != null) {
            mProdutoViewModel = ProdutoViewModel(container.context)
            observerAdicionarProduto()
            observerAdicionarImagemProduto()
        }
        val smf = SimpleMaskFormatter("(NN)NNNNN-NNNN")
        val mtw = MaskTextWatcher(binding.editTelefone, smf)
        binding.editTelefone.addTextChangedListener(mtw)
        return binding.root
    }

    fun tirarFoto() {
        if (qtdFotosTiradas < 5) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            Generics.adicionarFotoProduto(imageBitmap, verificarImagemView())
            qtdFotosTiradas++
        }

    }

    fun verificarImagemView(): ImageView {
        lateinit var imageView: ImageView
        when (qtdFotosTiradas) {
            1 -> imageView = binding.imageViewCameraUm
            2 -> imageView = binding.imageViewCameraDois
            3 -> imageView = binding.imageViewCameraTres
            4 -> imageView = binding.imageViewCameraQuatro
        }
        return imageView

    }

    fun cadastrarProduto() {
        val stringErro = getString(R.string.minimo_caracteres_adicionar_produto)
        val stringDescricaoErro = getString(R.string.minimo_caracteres_adicionar_produto_com_foto)
        if (Generics.verificarTamanhoString(binding.editTecido, stringErro, 3) &&
            Generics.verificarTamanhoString(binding.editModelo, stringErro, 3) &&
            Generics.verificarTamanhoString(binding.editDescricaoProduto, stringDescricaoErro, 45) &&
            Generics.verificarTamanhoString(binding.editTelefone, getString(R.string.telefone_invalido), 14)
        ) {
            if (binding.imageViewCameraUm.drawable != null && binding.imageViewCameraDois.drawable != null &&
                binding.imageViewCameraTres.drawable != null && binding.imageViewCameraQuatro.drawable != null){
                val produto = atribuirValoresProduto()
                mProdutoViewModel.adicionarProduto(produto)
            }
            else{
                Toast.makeText(activity, getString(R.string.imagens_invalida), Toast.LENGTH_SHORT).show()
            }


        }

    }

    fun observerAdicionarProduto() {
        activity?.let {
            mProdutoViewModel.anunciarProduto.observe(it, { produto ->
                if (produto != null) {
                    listaString = listaString64()
                    idProduto = produto.idProduto.toString()
                   requisicaoImagens(ImagemBase64(listaString[0]), idProduto.toLong())
                   requisicaoImagens(ImagemBase64(listaString[1]), idProduto.toLong())
                   requisicaoImagens(ImagemBase64(listaString[2]), idProduto.toLong())
                   requisicaoImagens(ImagemBase64(listaString[3]), idProduto.toLong())
                }
             })
        }
    }
    fun requisicaoImagens(imagem: ImagemBase64, idProduto:Long){
        mProdutoViewModel.adicionarImagemProduto(
            imagem,
            idProduto.toLong()
        )
    }

    fun observerAdicionarImagemProduto() {
        activity?.let {
            mProdutoViewModel.adicionarImagemProduto.observe(it, {cadastroiImg ->
                if (cadastroiImg) {
                    qtdFotosEnviadas++
                    if (qtdFotosEnviadas == 5){
                        findNavController().navigate(R.id.action_nav_adicionar_fotos_produto_to_nav_home)
                    }
                }
            })
        }
    }

    fun listaString64(): List<String> {
        val listaImagens = mutableListOf<String>()
        listaImagens.add(Generics.converterImagemEmStringBase64(binding.imageViewCameraUm.drawable.toBitmap()))
        listaImagens.add(Generics.converterImagemEmStringBase64(binding.imageViewCameraDois.drawable.toBitmap()))
        listaImagens.add(Generics.converterImagemEmStringBase64(binding.imageViewCameraTres.drawable.toBitmap()))
        listaImagens.add(Generics.converterImagemEmStringBase64(binding.imageViewCameraQuatro.drawable.toBitmap()))

        return listaImagens
    }

    fun atribuirValoresProduto(): Produto {
        val idUsuario = activity?.let { SecurityPreferences.get(
            SecurityPreferences.sharePrefences(it), "id")}
        return Produto(
            0, argumentos.nome, binding.editDescricaoProduto.text.toString(), argumentos.preco.toDouble(),
            argumentos.tamanho,argumentos.genero,
            trocavel(binding.spinnerTrocavel.selectedItem.toString()),
            verificarSubCategoria(argumentos.categoria),
            idUsuario!!.toLong(),
            binding.editTecido.text.toString(),
            argumentos.marca,
            binding.editTelefone.text.toString()
        )
    }

    fun trocavel(stringTrocavel: String): Boolean = stringTrocavel == "Sim"
    fun verificarSubCategoria(subCategoria:String):Long{
        return when(subCategoria){
            "Blusa Moletom" -> 1
            "Corta Vento" -> 2
            "Terno" -> 8
            "Sobre Tudo" -> 9
            "Blusinha" -> 10
            "Camisa Regata" -> 11
            "Camisa Manga Longa" -> 12
            "Camisa Social" -> 13
            "Short Jeans" -> 10521
            "Short Futebol" -> 10523
            "Short Praia" -> 10524
            "Short Feminino" -> 10526
            "Calça Jeans" -> 20
            "Calça Moletom" -> 21
            "Calça Social" -> 24
            "Calça Legging" -> 25
            "Sapato" -> 10529
            "Tenis" -> 10528
            "Sandalia" -> 10530
            else -> 10531
        }
    }
}