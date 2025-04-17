package com.wikifut.app.presentation.signup

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.wikifut.app.R
import com.wikifut.app.utils.Constans.CLIENT_ID_FIREBASE
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SignUpScreen(
    auth: FirebaseAuth,
    navigateToInitial: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    navigateToHome: () -> Unit = {}
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


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
                            isLoading = false
                            if (task.isSuccessful) {
                                Log.i("GoogleSignIn", "Autenticación con Google exitosa")
                                navigateToHome() // Navega a la pantalla principal
                            } else {
                                errorMessage = "Error al iniciar sesión con Google. Inténtalo de nuevo."
                                Log.e("GoogleSignIn", "Error en autenticación con Google", task.exception)
                            }
                        }
                }
            } catch (e: Exception) {
                isLoading = false
                errorMessage = "Error al obtener credenciales de Google. Inténtalo de nuevo."
                Log.e("GoogleSignIn", "Error al obtener credenciales", e)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6650a4))
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_login_register),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(190.dp))
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                passwordVisible = passwordVisible,
                onPasswordVisibilityToggle = { passwordVisible = !passwordVisible }
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }


            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Por favor, completa todos los campos."
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Log.i("wikifut", "Usuario creado")
                                navigateToHome()
                            } else {
                                errorMessage = when (task.exception?.message) {
                                    "The email address is already in use by another account." -> "El correo electrónico ya está en uso."
                                    "The email address is badly formatted." -> "El formato del correo electrónico no es válido."
                                    "The given password is invalid. [ Password should be at least 6 characters ]" -> "La contraseña debe tener al menos 6 caracteres."
                                    else -> "Error al crear el usuario. Inténtalo de nuevo."
                                }
                                Log.i("wikifut", "Error al crear usuario")
                            }
                        }
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E0414))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = White)
                } else {
                    Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener { result: BeginSignInResult ->
                            signInLauncher.launch(IntentSenderRequest.Builder(result.pendingIntent).build())
                        }
                        .addOnFailureListener { e: Exception ->
                            isLoading = false
                            errorMessage = "Error al iniciar sesión con Google. Inténtalo de nuevo."
                            Log.e("GoogleSignIn", "Error en el inicio de sesión con Google", e)
                        }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .border(2.dp, White, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF0E0414))
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.google_ic),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Registrarse con Google",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = {
                navigateToLogin()
            }) {
                Text(
                    "¿Ya tienes cuenta? Iniciar Sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    }
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityToggle: () -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Contraseña") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(0.8f),
        shape = RoundedCornerShape(20.dp),
        trailingIcon = {
            val image = if (passwordVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(onClick = onPasswordVisibilityToggle) {
                Icon(
                    imageVector = image,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        }
    )
}
