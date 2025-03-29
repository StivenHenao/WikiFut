package com.wikifut.app.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.JsonArray
import com.wikifut.app.model.*
import com.wikifut.app.viewmodel.PlayerViewModel

/**
 * Vista de Preview que utiliza un mock, para evitar inyección de dependencias en el preview.
 */
@Preview(showBackground = true)
@Composable
fun PreviewPlayerScreen() {
    val playerDataResponse = PlayerDataResponse(
        get = "players",
        parameters = Parameters(
            id = 276,
            team = null,
            league = null,
            season = 2019,
            search = "Messi"
        ),
        errors = null,
        results = 1,
        paging = Paging(current = 1, total = 10),
        response = listOf(
            PlayerData(
                player = Player(
                    id = 276,
                    name = "Lionel Messi",
                    firstname = "Lionel",
                    lastname = "Messi",
                    age = 36,
                    birth = PlayerBirth("1987-06-24", "Rosario", "Argentina"),
                    nationality = "Argentina",
                    height = "170 cm",
                    weight = "72 kg",
                    injured = false,
                    photo = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Lionel_Messi_WC2022.jpg"
                ),
                statistics = listOf(
                    PlayerStatistic(
                        team = PlayerTeam(
                            id = 1,
                            name = "FC Barcelona",
                            logo = "https://example.com/logo.png"
                        ),
                        league = PlayerLeague(
                            id = 1,
                            name = "La Liga",
                            country = "Spain",
                            logo = "https://example.com/league.png",
                            flag = null,
                            season = 2019
                        ),
                        games = PlayerGames(
                            appearences = 30,
                            lineups = 30,
                            minutes = 2700,
                            number = 10,
                            position = "Attacker",
                            rating = "8.5",
                            captain = false
                        ),
                        substitutes = null,
                        shots = null,
                        goals = null,
                        passes = null,
                        tackles = null,
                        duels = null,
                        dribbles = null,
                        fouls = null,
                        cards = null,
                        penalty = null
                    )
                )
            )
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1F1235))
    ) {
       PlayerDetails(playerDataResponse = playerDataResponse)
    }
}

/**
 * PlayerScreen: Se encarga de observar el estado (loading, error y datos) del ViewModel.
 * Si se detectan datos, llama a PlayerDetails para mostrarlos.
 */
@Composable
fun PlayerScreen(
    playerId: Int,
    season: Int,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    // Observamos los LiveData del ViewModel.
    val playerDataState by viewModel.playerData.observeAsState()
    val loading by viewModel.loading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState()

    // Llamamos a la API al iniciar el Composable.
    LaunchedEffect(playerId) {
        viewModel.fetchPlayerData(playerId, season)
    }

    // Layout principal con fondo oscuro.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1F1235))
    ) {
        when {
            loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }
            error != null -> {
                Text(
                    text = error ?: "",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            playerDataState != null -> {
                PlayerDetails(playerDataResponse = playerDataState!!)
            }
            else -> {
                Text(text = "No se encontraron datos", color = Color.White)
            }
        }
    }
}

/**
 * PlayerDetails: Muestra la información del jugador.
 * Se extrae el primer elemento de la lista response.
 * La posición se obtiene del primer estadístico (dentro de games) y se muestra.
 */
@Composable
fun PlayerDetails(playerDataResponse: PlayerDataResponse) {
    if (playerDataResponse.response.isEmpty()) {
        // Mostrar un mensaje informando que no se encontraron datos
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1F1235)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No se encontraron datos del jugador", color = Color.White)
        }
    } else {
        val playerData = playerDataResponse.response.first()
        val player = playerData.player
        // Obtenemos la posición del primer estadístico, o "N/A" si no existe.
        val position = playerData.statistics.firstOrNull()?.games?.position ?: "N/A"

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Imagen del jugador, centrada y con forma circular.
            AsyncImage(
                model = player.photo,
                contentDescription = "Foto de ${player.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Nombre del jugador.
            Text(
                text = player.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Detalles del jugador.
            Text(
                text = "Posición: $position",
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = "Edad: ${player.age}",
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                text = "Nacionalidad: ${player.nationality}",
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Datos de nacimiento.
            Text(
                text = "Fecha de nacimiento: ${player.birth.date}",
                fontSize = 16.sp,
                color = Color.White
            )
            Text(
                text = "Lugar de nacimiento: ${player.birth.place}, ${player.birth.country}",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}