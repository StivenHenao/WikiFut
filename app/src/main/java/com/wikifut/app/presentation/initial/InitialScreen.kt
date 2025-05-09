package com.wikifut.app.presentation.initial

import android.app.Activity
import android.util.Log
 import androidx.activity.compose.rememberLauncherForActivityResult
 import androidx.activity.result.IntentSenderRequest
 import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
 import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
 import com.google.firebase.auth.FirebaseAuth
 import com.google.firebase.auth.GoogleAuthProvider
import com.wikifut.app.R
 import com.wikifut.app.utils.Constans.CLIENT_ID_FIREBASE
 import com.google.android.gms.auth.api.identity.Identity
 import com.google.android.gms.auth.api.identity.BeginSignInRequest
 import com.google.android.gms.auth.api.identity.BeginSignInResult
 import com.google.android.gms.common.api.ApiException
 import com.google.android.gms.common.api.CommonStatusCodes

@Composable
fun InitialScreen(
    navigateToLogin: () -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit
) {
    val context = LocalContext.current

     val auth = remember { FirebaseAuth.getInstance() }
     val oneTapClient = remember { Identity.getSignInClient(context) }
     val signInRequest = remember {
         BeginSignInRequest.builder()
             .setGoogleIdTokenRequestOptions(
                 BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                     .setSupported(true)
                     .setServerClientId(CLIENT_ID_FIREBASE)
                     .setFilterByAuthorizedAccounts(false)
                     .build()
             )
             .build()
     }

     val signInLauncher = rememberLauncherForActivityResult(
         contract = ActivityResultContracts.StartIntentSenderForResult()
     ) { result ->
         if (result.resultCode == Activity.RESULT_OK) {
             try {
                 val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                 val googleIdToken = credential.googleIdToken
                 googleIdToken?.let { token: String ->
                     val firebaseCredential = GoogleAuthProvider.getCredential(token, null)
                     auth.signInWithCredential(firebaseCredential)
                         .addOnCompleteListener { task ->
                             if (task.isSuccessful) {
                                 navigateToHome()
                             } else {
                                 Log.e(
                                     "GoogleSignIn",
                                     "Error en autenticación con Google",
                                     task.exception
                                 )
                             }
                         }
                 }
             } catch (e: Exception) {
                 Log.e("GoogleSignIn", "Error al obtener credenciales", e)
             }
         }
     }

    Box(modifier = Modifier.fillMaxSize()
        .background(Color(0xFF6650a4))
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_login_register),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(90.dp))

            Text(
                text = "¡Cientos de partidos",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E0414)
            )
            Text(
                text = "gratis en Wikifut!",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E0414)
            )

            Spacer(modifier = Modifier.height(80.dp))

            // BOTÓN REGISTRARSE
            Button(
                onClick = { navigateToSignUp() },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E0414))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Registrarse Icono",
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterStart)
                    )
                    Text(
                        "Registrarse",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(19.dp))

            // BOTÓN GOOGLE
            OutlinedButton(
                onClick = {
                    oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener { result: BeginSignInResult ->
                            signInLauncher.launch(
                                IntentSenderRequest.Builder(result.pendingIntent).build()
                            )
                        }
                        .addOnFailureListener { e: Exception ->
                            Log.e("GoogleSignIn", "Error en el inicio de sesión con Google", e)
                            when ((e as? ApiException)?.statusCode) {
                                CommonStatusCodes.INVALID_ACCOUNT -> {
                                    Log.e("GoogleSignIn", "Cuenta no válida")
                                }

                                CommonStatusCodes.NETWORK_ERROR -> {
                                    Log.e("GoogleSignIn", "Error de red")
                                }

                                else -> {
                                    Log.e("GoogleSignIn", "Error desconocido: ${e.message}")
                                }
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .border(2.dp, White, RoundedCornerShape(30.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF0E0414))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.mipmap.google_ic),
                        contentDescription = "Google Icon",
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.CenterStart)
                    )
                    Text(
                        "Continuar con Google",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BOTÓN INICIAR SESIÓN
            Button(
                onClick = { navigateToLogin() },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E0414))
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.registrarse),
                        contentDescription = "Iniciar Sesión Icono",
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterStart)
                    )
                    Text(
                        "Iniciar Sesión",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InitialScreenPreview() {
    InitialScreen(
        navigateToLogin = {},
        navigateToSignUp = {},
        navigateToHome = {}
    )
}
