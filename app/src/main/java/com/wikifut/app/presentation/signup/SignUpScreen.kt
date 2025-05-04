package com.wikifut.app.presentation.signup

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Spring
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.wikifut.app.R
import com.wikifut.app.utils.Constans.CLIENT_ID_FIREBASE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    auth: FirebaseAuth? = null,
    navigateToInitial: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    navigateToHome: () -> Unit = {}
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Avatar selection
    val profileImages = remember {
        listOf(
            R.drawable.bruyne,
            R.drawable.cristiano,
            R.drawable.messi,
            R.drawable.mbape
        )
    }
    var selectedImage by remember { mutableStateOf<Int?>(null) }

    val oneTapClient = if (auth != null) remember { Identity.getSignInClient(context) } else null
    val signInRequest = if (auth != null) remember {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(CLIENT_ID_FIREBASE)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    } else null

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && auth != null && oneTapClient != null) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val googleIdToken = credential.googleIdToken
                googleIdToken?.let { token ->
                    val firebaseCredential = GoogleAuthProvider.getCredential(token, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                navigateToHome()
                            } else {
                                errorMessage = "Error al iniciar sesión con Google"
                            }
                        }
                }
            } catch (e: Exception) {
                isLoading = false
                errorMessage = "Error al obtener credenciales"
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
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Elige tu foto de perfil",
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Avatar Selection Row with Animations
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                profileImages.forEach { imageRes ->
                    val isSelected = selectedImage == imageRes

                    val borderColor by animateColorAsState(
                        targetValue = if (isSelected) Color(0xFF4CAF50) else Color.Transparent, // Verde si está seleccionado
                        animationSpec = tween(durationMillis = 300)
                    )
                    val imageSize by animateDpAsState(
                        targetValue = if (isSelected) 80.dp else 70.dp, // Animación de tamaño también (opcional)
                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                    )

                    Box(
                        modifier = Modifier
                            .size(imageSize)
                            .clip(CircleShape)
                            .background(Color(0xFF6650a4))
                            .border(width = 3.dp, color = borderColor, shape = CircleShape)
                            .clickable {
                                selectedImage = imageRes
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = imageRes),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White
                )
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
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || username.isBlank()) {
                        errorMessage = "Completa todos los campos"
                        return@Button
                    }

                    if (selectedImage == null) {
                        errorMessage = "Selecciona un avatar"
                        return@Button
                    }

                    if (auth == null) {
                        navigateToHome()
                        return@Button
                    }

                    isLoading = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                navigateToHome()
                            } else {
                                errorMessage = when {
                                    task.exception?.message?.contains("email address") == true -> "Correo inválido o en uso"
                                    password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                                    else -> "Error al registrar"
                                }
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E0414))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = White, modifier = Modifier.size(20.dp))
                } else {
                    Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = {
                    if (auth == null || oneTapClient == null || signInRequest == null) {
                        navigateToHome()
                        return@OutlinedButton
                    }

                    isLoading = true
                    oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener { result ->
                            signInLauncher.launch(
                                IntentSenderRequest.Builder(result.pendingIntent).build()
                            )
                        }
                        .addOnFailureListener {
                            isLoading = false
                            errorMessage = "Error con Google"
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.5.dp, White, RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = White,
                    containerColor = Color.Transparent
                )
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.google_ic),
                    contentDescription = "Google",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Continuar con Google")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onPasswordVisibilityToggle) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(auth = null)
}