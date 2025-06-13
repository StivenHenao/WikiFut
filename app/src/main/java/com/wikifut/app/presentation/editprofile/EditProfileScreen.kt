package com.wikifut.app.presentation.editprofile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wikifut.app.R
import androidx.compose.material3.LocalTextStyle
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: EditProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf<String?>(null) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(0) } // 0 para masculino, 1 para femenino

    val maleProfileImages = remember {
        listOf<Pair<String, Int>>(
            "bruyne" to R.drawable.bruyne,      // Hombre
            "cristiano" to R.drawable.cristiano, // Hombre
            "messi" to R.drawable.messi,        // Hombre
            "mbape" to R.drawable.mbape         // Hombre
        )
    }

    val femaleProfileImages = remember {
        listOf<Pair<String, Int>>(
            "sam_kerr" to R.drawable.sam_kerr,      // Mujer
            "linda_caicedo" to R.drawable.linda_caicedo, // Mujer
            "aitana_bonmati" to R.drawable.aitana_bonmati, // Mujer
            "alexia_putellas" to R.drawable.alexia_putellas // Mujer
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.wikifutfondo1),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(80.dp),
                    title = { 
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Editar Perfil",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(2f, 2f),
                                        blurRadius = 6f
                                    )
                                ),
                                color = Color.White
                            )
                        }
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Volver",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Box con blur
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .graphicsLayer {
                            clip = true
                        }
                        .blur(radius = 20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.wikifutfondo1),
                        contentDescription = "Background Blur",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }

                // Sombra semitransparente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color.Black.copy(alpha = 0.4f))
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (uiState) {
                        is EditProfileState.Loading -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is EditProfileState.Success -> {
                            val state = uiState as EditProfileState.Success
                            LaunchedEffect(state.username) {
                                if (username.isEmpty()) {
                                    username = state.username
                                }
                            }
                            if (selectedAvatar == null) {
                                selectedAvatar = state.avatar
                            }

                            Text(
                                text = "Nombre de usuario",
                                fontSize = 18.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(2f, 2f),
                                        blurRadius = 6f
                                    )
                                ),
                                modifier = Modifier.padding(vertical = 24.dp)
                            )

                            TextField(
                                value = username,
                                onValueChange = { newValue ->
                                    username = newValue.split(" ").joinToString(" ") { word ->
                                        if (word.isNotEmpty()) {
                                            word.first().uppercase() + word.substring(1)
                                        } else {
                                            word
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 56.dp)
                                    .padding(bottom = 24.dp),
                                shape = RoundedCornerShape(20.dp),
                                textStyle = LocalTextStyle.current.copy(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold
                                ),
                                singleLine = true,
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )

                            Text(
                                text = "Selecciona tu avatar",
                                fontSize = 18.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(2f, 2f),
                                        blurRadius = 6f
                                    )
                                ),
                                modifier = Modifier.padding(vertical = 24.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                // Box con blur
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer {
                                            clip = true
                                        }
                                        .blur(radius = 15.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.wikifutfondo1),
                                        contentDescription = "Background Blur",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillBounds
                                    )
                                }

                                // Sombra semitransparente
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.5f))
                                )

                                TabRow(
                                    selectedTabIndex = selectedGender,
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White,
                                    indicator = { tabPositions ->
                                        TabRowDefaults.SecondaryIndicator(
                                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedGender]),
                                            color = Color(0xFF4CAF50)
                                        )
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF2D1B45).copy(alpha = 0.8f))
                                ) {
                                    Tab(
                                        selected = selectedGender == 0,
                                        onClick = { selectedGender = 0 },
                                        text = {
                                            Text(
                                                "Masculino",
                                                color = if (selectedGender == 0) Color.White else Color.Gray
                                            )
                                        }
                                    )
                                    Tab(
                                        selected = selectedGender == 1,
                                        onClick = { selectedGender = 1 },
                                        text = {
                                            Text(
                                                "Femenino",
                                                color = if (selectedGender == 1) Color.White else Color.Gray
                                            )
                                        }
                                    )
                                }
                            }

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(if (selectedGender == 0) maleProfileImages else femaleProfileImages) { (avatarName, imageRes) ->
                                    val isSelected = selectedAvatar == avatarName

                                    // Animaciones
                                    val borderColor by animateColorAsState(
                                        targetValue = if (isSelected) Color(0xFF4CAF50) else Color.Transparent,
                                        animationSpec = tween(durationMillis = 300)
                                    )

                                    val imageSize by animateDpAsState(
                                        targetValue = if (isSelected) 104.dp else 100.dp,
                                        animationSpec = spring(stiffness = Spring.StiffnessLow)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(110.dp)
                                            .clip(CircleShape)
                                            .clickable { selectedAvatar = avatarName },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(imageSize)
                                                .clip(CircleShape)
                                                .border(3.dp, borderColor, CircleShape)
                                        ) {
                                            Image(
                                                painter = painterResource(id = imageRes),
                                                contentDescription = "Avatar $avatarName",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(CircleShape),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                }
                            }

                            if (showError) {
                                Text(
                                    text = errorMessage,
                                    color = Color.Red,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            Button(
                                onClick = {
                                    val trimmedUsername = username.trim()
                                    if (trimmedUsername.isBlank()) {
                                        showError = true
                                        errorMessage = "El nombre de usuario no puede estar vacío"
                                        return@Button
                                    }
                                    if (selectedAvatar == null) {
                                        showError = true
                                        errorMessage = "Debes seleccionar un avatar"
                                        return@Button
                                    }
                                    viewModel.saveProfile(trimmedUsername, selectedAvatar!!)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Text(
                                    "Guardar cambios",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                        is EditProfileState.Saved -> {
                            LaunchedEffect(Unit) {
                                onBack()
                            }
                        }
                        is EditProfileState.Error -> {
                            val error = (uiState as EditProfileState.Error).message
                            Text(
                                text = error,
                                color = Color.Red,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

class EditProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow<EditProfileState>(EditProfileState.Loading)
    val uiState: StateFlow<EditProfileState> = _uiState

    fun loadUserData() {
        viewModelScope.launch {
            try {
                val user = auth.currentUser
                user?.email?.let { email ->
                    firestore.collection("users").document(email)
                        .get()
                        .addOnSuccessListener { document ->
                            val username = document.getString("username") ?: ""
                            val avatar = document.getString("avatar") ?: "bruyne"
                            _uiState.value = EditProfileState.Success(
                                username = username,
                                avatar = avatar
                            )
                        }
                }
            } catch (e: Exception) {
                _uiState.value = EditProfileState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun saveProfile(username: String, avatar: String) {
        viewModelScope.launch {
            try {
                val user = auth.currentUser
                user?.email?.let { email ->
                    firestore.collection("users").document(email)
                        .update(
                            mapOf(
                                "username" to username,
                                "avatar" to avatar
                            )
                        )
                        .addOnSuccessListener {
                            _uiState.value = EditProfileState.Saved
                        }
                }
            } catch (e: Exception) {
                _uiState.value = EditProfileState.Error(e.message ?: "Error al guardar")
            }
        }
    }
}

sealed class EditProfileState {
    object Loading : EditProfileState()
    object Saved : EditProfileState()
    data class Success(val username: String, val avatar: String) : EditProfileState()
    data class Error(val message: String) : EditProfileState()
} 