package com.wikifut.app.presentation.login

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.wikifut.app.R
import com.wikifut.app.utils.Constans.CLIENT_ID_FIREBASE

@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    navigateToInitial: () -> Unit = {},
    navigateToSignUp: () -> Unit = {},
    navigateToHome: () -> Unit = {}
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showResetDialog by remember { mutableStateOf(false) }

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
                    auth?.signInWithCredential(firebaseCredential)
                        ?.addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Log.i("GoogleSignIn", "Autenticación con Google exitosa")
                                navigateToHome()
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(180.dp))

            Spacer(modifier = Modifier.height(70.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo electrónico") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .heightIn(min = 56.dp),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.height(10.dp))

            PasswordTextField(
                value = password,
                onValueChange = { password = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (errorMessage != null) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
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
                    auth?.signInWithEmailAndPassword(email, password)
                        ?.addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                Log.i("wikifut", "Login success")
                                navigateToHome()
                            } else {
                                errorMessage = "Correo electrónico o contraseña incorrectos. Inténtalo de nuevo."
                                Log.i("wikifut", "Login failed")
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
                    Text("Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                    .height(50.dp)
                    .border(1.5.dp, White, RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = White,
                    containerColor = Color.Transparent
                )
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.google_ic),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Iniciar sesión con Google",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = { navigateToSignUp() }) {
                Text(
                    "¿No tienes cuenta? Crea una",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
            TextButton(onClick = { showResetDialog = true }) {
                Text(
                    "¿Olvidaste tu contraseña?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
            ResetPasswordDialog(
                show = showResetDialog,
                onDismiss = { showResetDialog = false },
                auth = auth
            )

        }
    }
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Contraseña") },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else
                Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription = "Toggle password visibility")
            }
        }
    )
}

@Composable
fun ResetPasswordDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    auth: FirebaseAuth
) {
    var email by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Recuperar contraseña") },
            text = {
                Column {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        )
                    )
                    if (statusMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(statusMessage ?: "", color = Color.Red, fontSize = 13.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (email.isBlank()) {
                        statusMessage = "Por favor ingresa un correo válido."
                        return@TextButton
                    }

                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            statusMessage = if (task.isSuccessful) {
                                "Correo enviado. Revisa tu bandeja de entrada."
                            } else {
                                "Error al enviar el correo. Verifica el correo ingresado."
                            }
                        }
                }) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        )
    }
}
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun LoginScreenPreview() {
//    LoginScreen()
//}
