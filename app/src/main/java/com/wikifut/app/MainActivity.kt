package com.wikifut.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.wikifut.app.ui.theme.WikifutTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.WindowCompat
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import android.graphics.Color as AndroidColor


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navHostController : NavHostController
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        enableEdgeToEdge()
        
        // Configurar la barra de navegación
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        // Establecer el color de fondo de la barra de navegación
        window.navigationBarColor = AndroidColor.TRANSPARENT
        
        setContent {
            navHostController = rememberNavController()
            val view = LocalView.current
            val isDarkTheme = isSystemInDarkTheme()
            
            DisposableEffect(isDarkTheme) {
                val window = (view.context as android.app.Activity).window
                val insetsController = WindowInsetsControllerCompat(window, view)
                
                // Configurar la apariencia de la barra de navegación para que sea transparente en ambos modos
                insetsController.isAppearanceLightNavigationBars = false
                
                // Configurar el color del texto de la barra de estado
                insetsController.isAppearanceLightStatusBars = false
                
                // Asegurar que la barra de navegación sea transparente
                window.navigationBarColor = AndroidColor.TRANSPARENT
                
                onDispose {}
            }
            
            WikifutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    NavigationWrapper(navHostController = navHostController, auth = auth)
                }
            }
        }
    }

    override fun onStart(){
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            // nav to home
            Log.i("wikifut", "Usuario logueado")
        }
    }
}