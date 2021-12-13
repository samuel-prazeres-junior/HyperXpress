package com.example.hyperxpress.view.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.hyperxpress.R
import com.example.hyperxpress.databinding.ActivityAplicacaoAposLogarBinding
import com.example.hyperxpress.service.repository.local.SecurityPreferences
import com.example.hyperxpress.viewmodel.LoginViewModel
import com.example.hyperxpress.viewmodel.UsuarioViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class AplicacaoAposLogar : AppCompatActivity() {

    private lateinit var switch: Switch
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAplicacaoAposLogarBinding
    private lateinit var mSharePreferences: SharedPreferences
    private lateinit var mViewModel: LoginViewModel
    private lateinit var emailUser:String
    private lateinit var senhaUser:String
    private lateinit var username:String
    private lateinit var idUsuario:String
    private lateinit var mUsuarioViewModel:UsuarioViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUsuarioViewModel = UsuarioViewModel(this)
//        verificarCache()
        mSharePreferences = SecurityPreferences.sharePrefences(applicationContext)
        mViewModel = LoginViewModel(application)
        binding = ActivityAplicacaoAposLogarBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        avatarUsuario()

        setSupportActionBar(binding.appBarAplicacaoAposLogar.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_aplicacao_apos_logar)

        binding.appBarAplicacaoAposLogar.fab.setOnClickListener { view ->
            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailUser,senhaUser)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        startActivity(Intent(this, MessageActivity::class.java))
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_editar_cadastro_pessoal,
                R.id.nav_favoritos,
                R.id.nav_carrinho,
                R.id.nav_historico_vendedor,
                R.id.nav_suporte,
                R.id.nav_adicioar_produto,
                R.id.nav_sair
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val header = navView.getHeaderView(0)
        switch = header.findViewById(R.id.swt_dark_mode)
//        verificarThemaDefault()
        switchDarkMode()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_aplicacao_apos_logar)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun avatarUsuario() {

        val nav: NavigationView = binding.root.findViewById(R.id.nav_view)
        val header = nav.getHeaderView(0)
        val nomeUser: TextView = header.findViewById(R.id.tv_nome_usuario_logado)
        val imagemUser: ImageView = header.findViewById(R.id.icone_usuario_logado)
        mUsuarioViewModel.pegarImagemUsuario(idUsuario.toLong(), imagemUser)
        nomeUser.text = username

    }

    fun desconectar(item: MenuItem) {
        startActivity(Intent(this, MainActivity::class.java))
        SecurityPreferences.remove(mSharePreferences, getString(R.string.cache_id))
        SecurityPreferences.remove(mSharePreferences, getString(R.string.cache_avatar))
        SecurityPreferences.remove(mSharePreferences, getString(R.string.cache_email))
        finish()
    }


    fun switchDarkMode() {
        switch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SecurityPreferences.store(mSharePreferences, "thema", "dark")
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SecurityPreferences.store(mSharePreferences, "thema", "light")
            }
            delegate.applyDayNight()
        }
    }
    fun verificarCache(){
            idUsuario=  SecurityPreferences.get(SecurityPreferences.sharePrefences(this), getString(R.string.cache_id))
            username=  SecurityPreferences.get(SecurityPreferences.sharePrefences(this), getString(R.string.cache_avatar))
            emailUser=  SecurityPreferences.get(SecurityPreferences.sharePrefences(this), getString(R.string.cache_email))
            senhaUser=  SecurityPreferences.get(SecurityPreferences.sharePrefences(this), getString(R.string.cache_senha))
    }
}