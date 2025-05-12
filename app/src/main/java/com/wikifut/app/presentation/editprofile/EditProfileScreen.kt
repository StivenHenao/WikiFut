package com.wikifut.app.presentation.editprofile

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    val profileImages = remember {
        listOf<Pair<String, Int>>(
            "bruyne" to R.drawable.bruyne,
            "cristiano" to R.drawable.cristiano,
            "messi" to R.drawable.messi,
            "mbape" to R.drawable.mbape
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F1235),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF2D1B45))
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
                        modifier = Modifier.padding(vertical = 24.dp)
                    )

                    TextField(
                        value = username,
                        onValueChange = { newValue ->
                            username = newValue
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp)
                            .padding(bottom = 24.dp),
                        shape = RoundedCornerShape(20.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
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
                        modifier = Modifier.padding(vertical = 24.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(profileImages) { (avatarName, imageRes) ->
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

                    Button(
                        onClick = {
                            if (username.isBlank()) {
                                showError = true
                                errorMessage = "El nombre de usuario no puede estar vacío"
                                return@Button
                            }
                            if (selectedAvatar == null) {
                                showError = true
                                errorMessage = "Debes seleccionar un avatar"
                                return@Button
                            }
                            viewModel.saveProfile(username, selectedAvatar!!)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0E0414)
                        )
                    ) {
                        Text(
                            "Guardar cambios",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
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