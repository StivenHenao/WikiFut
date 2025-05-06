package com.wikifut.app.presentation.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.rememberImagePainter
import androidx.compose.material3.Surface


import com.wikifut.app.model.*
import com.wikifut.app.viewmodel.PlayerViewModel
import com.wikifut.app.presentation.player.components.StatisticBar

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
                            season = 2021
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
                        shots = Shots(100, 20),
                        goals = PlayerGoals(50, 1, 10, 0),
                        passes = Passes(200, 10, 20),
                        tackles = Tackles(10, 5, 20),
                        duels = Duels(30, 20),
                        dribbles = Dribbles(100, 80, 12),
                        fouls = Fouls(100,200),
                        cards = Cards(20, 50, 30),
                        penalty = Penalty(100, 200, 30, 70, 0)
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
fun PlayerDetails(playerDataResponse: PlayerDataResponse) =
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
        val statistic = playerData.statistics.first()

        val statisticGame = statistic.games
        val statisticTeamPlayer = statistic.team
        val statisticPlayerLeague = statistic.league

        val rating = statisticGame?.rating
        val team_player = statisticTeamPlayer.name


        val position = playerData.statistics.firstOrNull()?.games?.position ?: "N/A"
        val numberPlayer =statisticGame?.number?: 0


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            //---------------------------HEADER------------------
            headerVistaPlayer(name = player.name, player.photo, team_player ,rating)

            Spacer(Modifier.height(8.dp))

            containerBasicInfo(team = team_player,
                imageTeam = statisticTeamPlayer.logo,
                nacionalidad = player.nationality,
                nacimiento = player.birth.date,
                altura = player.height,
                posicion = position,
                camiseta = numberPlayer,
                peso = player.weight
            )

            Spacer(Modifier.height(8.dp))

            Text("TEMPORADA", color=Color.White)

            Spacer(Modifier.height(8.dp))

            containerStatisticInfo(
                statistic.shots,
                statistic.goals,
                statistic.passes,
                statistic.tackles,
                statistic.duels,
                statistic.dribbles,
                statistic.fouls,
                statistic.cards,
                statistic.penalty
            )


        }
    }

@Composable
fun headerVistaPlayer(name: String, photo: String, team: String ,rating: String?){
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFF1E1E1E))
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically)
    {

        Image(
            painter = rememberImagePainter(photo),
            contentDescription = "Foto del Jugador",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ){
            Text(text = name,  color = Color.White)
            Text(text = team?: "No team",  color = Color.White)
        }

        Spacer(modifier = Modifier.width(16.dp))
        Text(text = rating?: "N/A", color = Color.White)


    }
}

@Composable
fun containerBasicInfo(
    team: String,
    imageTeam: String,
    nacionalidad: String,
    nacimiento: String,
    altura: String,
    posicion: String,
    camiseta: Int,
    peso: String
) {
    Surface(
        color = Color(0xFF1E1E1E),
        contentColor = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            //---------- Equipo ----------
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberImagePainter(imageTeam),
                    contentDescription = "Escudo del equipo",
                    modifier = Modifier.size(40.dp) // Tamaño definido
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = team)
            }

            Spacer(Modifier.height(24.dp))

            //---------- Primera fila de datos ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Nacionalidad", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(nacionalidad)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Edad", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(text = nacimiento) // Corregido: Usar la edad real del jugador
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Altura", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(altura)
                }
            }

            Spacer(Modifier.height(24.dp))

            //---------- Segunda fila de datos ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Posición", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(posicion)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Camiseta", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(camiseta.toString())
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Peso", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(peso)
                }
            }
        }
    }
}

@Composable
fun containerStatisticInfo(
    shoots: Shots?,
    goals: PlayerGoals?,
    passes: Passes?,
    tackles: Tackles?,
    duels: Duels?,
    dribbles: Dribbles?,
    fouls: Fouls?,
    cards: Cards?,
    penalty: Penalty?
){
    Surface(
        color = Color(0xFF1E1E1E),
        contentColor = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    StatisticBar(label = "Goles:",
                        value = (goals?.total ?: 0).toInt(),
                        maxValue = (shoots?.on ?: 0))
                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    StatisticBar(label = "Porcentaje de pase:",
                        value = (passes?.accuracy ?: 0).toInt(),
                        maxValue = 100)

                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    StatisticBar(label = "Dribles:",
                        value = (dribbles?.success ?: 0),
                        maxValue = 100)
                }


                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Asistencias: ")
                    Text(text = goals?.assists.toString())
                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Faltas: ")
                    Text(text = fouls?.drawn.toString())
                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Pases Clave: ")
                    Text(text = passes?.key.toString())
                }


            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    StatisticBar(label = "Duelos:",
                        value = (duels?.won ?: 0),
                        maxValue = (duels?.total ?: 0))
                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    StatisticBar(label = "Disparos:",
                        value = shoots?.on ?: 0,
                        maxValue = shoots?.total ?: 0)
                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                   StatisticBar(label = "Tackles:",
                       value = (tackles?.interceptions ?: 0),
                       maxValue = tackles?.total ?: 0)
                }


                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Penaltis: ")
                    Text(text = penalty?.won.toString())

                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Tarjetas Amarillas: ")
                    Text(text = cards?.yellow.toString())

                }

                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Tarjetas Rojas: ")
                    Text(text = cards?.red.toString())

                }



            }

        }
    }

}