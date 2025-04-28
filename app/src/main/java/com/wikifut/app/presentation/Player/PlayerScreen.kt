package com.wikifut.app.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import androidx.hilt.navigation.compose.hiltViewModel
import com.wikifut.app.model.Birth
import com.wikifut.app.model.Paging
import com.wikifut.app.model.Parameters
import com.wikifut.app.model.Player
import com.wikifut.app.model.PlayerDataResponse
import com.wikifut.app.model.ResponseItem
import com.wikifut.app.viewmodel.PlayerViewModel

@Preview(showBackground = true)
@Composable
fun PreviewPlayerScreen() {
    val playerDataResponse = PlayerDataResponse(
        get = "players",
        parameters = Parameters(search= "Messi", player = 276), // ✅ Se inicializa correctamente
        errors = emptyList(),
        results = 1,
        paging = Paging(current = 1, total = 10),
        response = listOf(
            ResponseItem(
                Player(
                    id = 276,
                    name = "Lionel Messi",
                    firstname = "Lionel",
                    lastname = "Messi",
                    age = 36,
                    birth = Birth("1987-06-24", "Rosario", "Argentina"),
                    nationality = "Argentina",
                    height = "170 cm",
                    weight = "72 kg",
                    number = 10,
                    position = "Forward",
                    photo = "https://example.com/messi.jpg"
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

@Composable
fun PlayerScreen(
    playerId: Int,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    // Convertimos nuestros StateFlow a estados de Compose
    val playerDataState by viewModel.playerData.observeAsState()
    val loading by viewModel.loading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState()

    LaunchedEffect(playerId) {
        viewModel.fetchPlayerData(playerId)
    }

    //----------Layout principal----------
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1F1235))
    ) {
        // Si está cargando, mostramos un spinner en el centro
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
        } else if (error != null) {
            Text(
                text = error ?: "",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            // Si la data está disponible, mostramos la info del jugador
            playerDataState?.let { playerDataState ->
                PlayerDetails(playerDataResponse = playerDataState)
            }
        }
    }
}

@Composable
fun PlayerDetails(playerDataResponse: PlayerDataResponse) {
    // Asumimos que la respuesta viene con una lista en "response" y tomamos el primer elemento
    val player = playerDataResponse.response.first().player

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Imagen del jugador (usando Coil para cargar la imagen desde URL)
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
        // Nombre del jugador
        Text(
            text = player.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Otros detalles: posición, edad, nacionalidad
        Text(
            text = "Posición: ${player.position}",
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
        // Datos de nacimiento
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
