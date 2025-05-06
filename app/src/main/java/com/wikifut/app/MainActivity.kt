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
import com.wikifut.app.presentation.player.PlayerScreen
import com.wikifut.app.ui.theme.WikifutTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navHostController : NavHostController
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        enableEdgeToEdge()
        setContent {
            navHostController = rememberNavController()
            WikifutTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    //NavigationWrapper(navHostController, auth)
                    //NavigationWrapper(navHostController = navHostController, auth = auth)
                    PlayerScreen(276, 2022)
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