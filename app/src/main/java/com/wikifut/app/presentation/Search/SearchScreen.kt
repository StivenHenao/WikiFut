package com.wikifut.app.presentation.Search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.wikifut.app.model.Team
import com.wikifut.app.model.TipoBusqueda
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.wikifut.app.presentation.Header.Header
import com.wikifut.app.R
import com.wikifut.app.model.League
import com.wikifut.app.model.LigaResponse
import com.wikifut.app.model.Venue
import com.wikifut.app.presentation.Search.EquiposResult
import com.wikifut.app.presentation.Search.LigasResult
import com.wikifut.app.presentation.Search.PlayerResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer

val MoradoOscuro = Color(0xFF2E0854)    // Morado oscuro
val MoradoClaro = Color(0xFF7E57C2)    // Morado más claro para el Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    tipo: TipoBusqueda,
    query: String,
    onSearchNavigate: (TipoBusqueda, String) -> Unit,
    HomeNavigate: () -> Unit,
    onTeamNavigate: (team: Team, venue: Venue) -> Unit,
    onPlayerNavigate: (playerId: String, season: String) -> Unit,
    onLigasNavigate: (leagueId: League, season: Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(key1 = tipo, key2 = query) {
        if (query.isNotEmpty()) {
            viewModel.buscar(tipo, query)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.height(70.dp),
                title = { 
                    Text(
                        "Inicio",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { HomeNavigate() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_back),
                            contentDescription = "Atrás",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF2D1B45)
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Imagen de fondo que ocupa toda la pantalla
            Image(
                painter = painterResource(id = R.drawable.wikifutfondo1),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Box con blur
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(134.dp) // 70.dp (TopAppBar) + 64.dp (Header)
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
                    .height(134.dp)
                    .background(Color.Black.copy(alpha = 0.4f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                Box(
                    modifier = Modifier
                        .height(64.dp)
                        .background(Color.Transparent)
                ) {
                    Header(
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        onBuscar = onSearchNavigate,
                        actions = {},
                        applyStatusBarPadding = false
                    )
                }

                Spacer(modifier = Modifier.height(0.dp))

                if (query.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ingresa algo para buscar",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    when (tipo) {
                        TipoBusqueda.Equipos -> {
                            EquiposResult(viewModel, onTeamNavigate)
                        }
                        TipoBusqueda.Ligas -> {
                            LigasResult(viewModel, onLigasNavigate)
                        }
                        TipoBusqueda.Jugadores -> {
                            PlayerResult(viewModel,onPlayerNavigate)
                        }
                        TipoBusqueda.Partidos -> TODO()
                    }
                }
            }
        }
    }
}



