package com.wikifut.app.presentation.Home

import java.util.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import com.wikifut.app.model.TipoBusqueda
import com.wikifut.app.presentation.Header.Header
import com.wikifut.app.R
import com.wikifut.app.model.Partido
import com.wikifut.app.utils.convertirHoraAColombia
import com.wikifut.app.utils.formatFechaParaApi
import com.wikifut.app.utils.obtenerFechaActual
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton

@Composable
fun HomeScreenWithDrawer(
    navigateToMatchDetail: (Long) -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateToInitial: () -> Unit,
    viewModel: HomePartidosViewModel = hiltViewModel(),
    onSearchNavigate: (TipoBusqueda, String) -> Unit,
    onFavoritosNavigate: () -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val userName by viewModel.userName.collectAsState()
    val avatar by viewModel.avatar.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarUsuario()
    }


    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent(
                closeDrawer = { scope.launch { drawerState.close() } },
                navigateToEditProfile = navigateToEditProfile,
                navigateToInitial = navigateToInitial,
                userName = userName,
                avatar = avatar,
                onFavoritosNavigate = onFavoritosNavigate
            )
        },
        drawerState = drawerState
    ) {
        HomePartidosScreen(
            userName = userName,
            avatar = avatar,
            viewModel = viewModel,
            navigateToEditProfile = navigateToEditProfile,
            navigateToInitial = navigateToInitial,
            openDrawer = { scope.launch { drawerState.open() } },
            onSearchNavigate = onSearchNavigate,
            navigateToMatchDetail = navigateToMatchDetail
        )
    }
}

@Composable
fun DrawerOption(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}


@Composable
fun DrawerContent(
    closeDrawer: () -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateToInitial: () -> Unit,
    userName: String?,
    avatar: String?,
    onFavoritosNavigate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.85f)
            .clip(RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp, topStart = 32.dp, bottomStart = 32.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2D1B45).copy(alpha = 0.97f),
                        Color(0xFF4A256F).copy(alpha = 0.97f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 12.dp, end = 12.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "¡ Hola !",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = userName ?: "Usuario",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(28.dp))
            
            val avatarImage = when (avatar) {
                "messi" -> R.drawable.messi
                "cristiano" -> R.drawable.cristiano
                "bruyne" -> R.drawable.bruyne
                "mbape" -> R.drawable.mbape
                "sam_kerr" -> R.drawable.sam_kerr
                "linda_caicedo" -> R.drawable.linda_caicedo
                "aitana_bonmati" -> R.drawable.aitana_bonmati
                "alexia_putellas" -> R.drawable.alexia_putellas
                else -> R.drawable.bruyne
            }
            
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(3.dp, Color(0xFF8E44AD), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = avatarImage),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(70.dp))

            DrawerOption(
                icon = Icons.Default.Edit,
                label = "Editar perfil",
                onClick = {
                    navigateToEditProfile()
                    closeDrawer()
                }
            )

            DrawerOption(
                icon = Icons.Default.Star,
                label = "Favoritos",
                onClick = {
                    onFavoritosNavigate()
                    closeDrawer()
                }
            )

            DrawerOption(
                icon = Icons.Default.Home,
                label = "Inicio",
                onClick = { closeDrawer() }
            )

            Spacer(modifier = Modifier.height(200.dp))

            DrawerOption(
                icon = Icons.Default.Logout,
                label = "Cerrar sesión",
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    navigateToInitial()
                    closeDrawer()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePartidosScreen(
    userName: String?,
    avatar: String?,
    viewModel: HomePartidosViewModel = hiltViewModel(),
    navigateToEditProfile: () -> Unit = {},
    navigateToMatchDetail: (Long) -> Unit,
    navigateToInitial: () -> Unit = {},
    openDrawer: () -> Unit = {},
    onSearchNavigate: (TipoBusqueda, String) -> Unit,
) {
    // Estado de la lista de partidos observada desde el ViewModel
    val state by viewModel.state.collectAsState()

    // Estado para la búsqueda de texto
    var searchQuery by remember { mutableStateOf("") }

    // Estado para mostrar el selector de fecha
    var showDatePicker by remember { mutableStateOf(false) }

    // Fecha seleccionada, inicia con la fecha actual
    var selectedDate by remember { mutableStateOf(obtenerFechaActual()) }



    // Al cambiar la fecha seleccionada, se cargan los partidos de esa fecha
    LaunchedEffect(selectedDate) {
        viewModel.cargarPartidosPorFecha(selectedDate)
    }

    // Filtro de partidos según la búsqueda
    val partidosFiltrados = state.filter { partido ->
        partido.league.name.contains(searchQuery, ignoreCase = true) ||
                partido.teams.home.name.contains(searchQuery, ignoreCase = true) ||
                partido.teams.away.name.contains(searchQuery, ignoreCase = true)
    }
    //val partidosFiltrados = state;


    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { date ->
                selectedDate = formatFechaParaApi(date)
                showDatePicker = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(0.dp),
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1F1235)
                )
            )
        },
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fondo base
            Image(
                painter = painterResource(id = R.drawable.wikifutfondo1),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Box con blur que coincide con el header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                // Sombra semi-transparente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color.Black.copy(alpha = 0.4f))
                ) {
                    Header(
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        onBuscar = onSearchNavigate,
                        actions = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(
                                    onClick = {showDatePicker = true},
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_calendar),
                                        contentDescription = "Seleccionar fecha",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                IconButton(
                                    onClick = openDrawer,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_menu),
                                        contentDescription = "Menú",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                    )
                }

                // Lista de partidos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(
                            top = 0.dp,
                            bottom = 35.dp,
                            start = 8.dp,
                            end = 8.dp
                        ),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Text(
                            text = "Partidos del día",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(2f, 2f),
                                    blurRadius = 12f
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    item {
                        Text(
                            text = selectedDate,
                            fontSize = 16.sp,
                            color = Color.White,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(2f, 2f),
                                    blurRadius = 8f
                                )
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    items(partidosFiltrados) { partido -> 
                        PartidoCard(partido, onMatchClick = navigateToMatchDetail) 
                    }

                    if (partidosFiltrados.isEmpty()) {
                        item {
                            Text(
                                "No se encontraron partidos.",
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis
    )
    val selectedDateMillis = datePickerState.selectedDateMillis
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    if (selectedDateMillis != null) {
                        onDateSelected(Date(selectedDateMillis))
                    }
                    onDismissRequest()
                }
            ) {
                Text("Seleccionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        },
        title = { Text("Seleccionar fecha") },
        text = {
            DatePicker(state = datePickerState)
        }
    )
}

@Composable
fun PartidoCard(
    partido: Partido,
    onMatchClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onMatchClick(partido.fixture.id.toLong())
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A256F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Liga
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = partido.league.logo,
                    contentDescription = "Liga",
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = partido.league.name,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Contenido principal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Equipo local
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = partido.teams.home.logo,
                        contentDescription = partido.teams.home.name,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = partido.teams.home.name,
                        fontSize = 12.sp,
                        color = Color.White,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }

                // Marcador y hora
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.8f)
                ) {
                    Text(
                        text = "${partido.goals?.home ?: "-"} - ${partido.goals?.away ?: "-"}",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = convertirHoraAColombia(partido.fixture.timestamp),
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Equipo visitante
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = partido.teams.away.logo,
                        contentDescription = partido.teams.away.name,
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = partido.teams.away.name,
                        fontSize = 12.sp,
                        color = Color.White,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            // Estado del partido
            Text(
                text = getEstadoPartido(partido),
                fontSize = 12.sp,
                color = if (partido.fixture.status.short == "FT") Color.Red else Color.Yellow,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    }
}

// Función lógica que traduce el estado del partido a texto legible
fun getEstadoPartido(partido: Partido): String {
    return when (partido.fixture.status.short) {
        "FT" -> "Finalizado"
        "CANC" -> "Cancelado"
        "NS" -> "Programado"
        else -> partido.fixture.status.long
    }
}