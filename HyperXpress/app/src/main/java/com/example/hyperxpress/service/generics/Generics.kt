package com.example.hyperxpress.service.generics

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.Base64
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.app.NotificationCompat
import com.example.hyperxpress.R
import com.example.hyperxpress.view.activity.MainActivity
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class Generics private constructor() {

    companion object {
        fun criarNotificacao(
            titulo: String,
            descricao: String,
            corpo: String,
            notifacaoManager: NotificationManager,
            activity: Context
        ) {

            val notificationChannel: NotificationChannel
            val buildder: NotificationCompat.Builder
            val channelId = "com.angeloplacebo.notificationapp"

            val notificationManagerFunc: NotificationManager = notifacaoManager
            val id: Int = Random.nextInt(1, 100)

            val intent: Intent = Intent(activity, MainActivity::class.java)
            val pedningIntent = PendingIntent.getActivity(activity, 0, intent, 0)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel =
                    NotificationChannel(
                        channelId,
                        descricao,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )

                notificationManagerFunc.createNotificationChannel(notificationChannel)

                buildder = NotificationCompat.Builder(activity, channelId)
                    .setSmallIcon(R.drawable.icone_notificacao)
                    .setContentTitle(titulo)
                    .setContentText(corpo)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pedningIntent)
            } else {
                buildder = NotificationCompat.Builder(activity)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(titulo)
                    .setContentText(corpo)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pedningIntent)
            }
            notificationManagerFunc.notify(id, buildder.build())
        }


        fun spinner(spinner: Spinner, activity: Context, listaString: Int) {

//Crie um ArrayAdapter usando a matriz de string e um layout giratório padrão
            activity?.let {
                ArrayAdapter.createFromResource(
                    it,
                    listaString,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    //Especifique o layout a ser usado quando a lista de opções aparecer
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Aplique o adaptador ao spinner
                    spinner.adapter = adapter
                }
            }
        }

        fun adicionarFotoProduto(foto: Bitmap, imagemView: ImageView) {
            imagemView.setImageBitmap(foto)
            imagemView.visibility = View.VISIBLE
        }

        fun formatarData(formato:String, data:Date):String{
            val smf = SimpleDateFormat(formato)
            return smf.format(data)
        }

        fun visualizarEsconderSenha(editText: EditText){
            if (editText.inputType == 145) editText.inputType = 129 else  editText.inputType = 145

        }

        fun estrelasVisiveis(estrelas:List<ImageView>, nivelDestaque:Int){
            for (i in 0 until nivelDestaque){
                estrelas[i].visibility = View.VISIBLE
            }
        }

        fun converterImagemEmStringBase64(bitmap: Bitmap):String{
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val arrayByte = stream.toByteArray()
            return Base64.encodeToString(arrayByte, Base64.DEFAULT)

        }

        fun verificarTamanhoString(edit:EditText, stringErro:String, tamanhoVerificar:Int): Boolean{

            return if (edit.text.toString().length >= tamanhoVerificar){
                true
            } else{
                edit.error = stringErro
                false
            }
        }
    }


}