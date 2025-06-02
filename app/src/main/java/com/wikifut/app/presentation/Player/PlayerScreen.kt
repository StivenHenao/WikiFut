package com.wikifut.app.presentation.player

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.wikifut.app.R

import com.wikifut.app.model.*
import com.wikifut.app.viewmodel.PlayerViewModel
import com.wikifut.app.presentation.player.components.StatisticBar
import kotlinx.coroutines.launch

/**
 * Vista de Preview que utiliza un mock, para evitar inyección de dependencias en el preview.
 */
@Preview(showBackground = true)
@Composable
fun PreviewPlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val playerDataResponse = PlayerDataResponse(
        get = "players",
        parameters = Parameters(
            id = 276,
            team = null,
            league = null,
            season = 2019,
            search = "Messi",
            player = TODO()
        ),
        errors = null,
        results = 1,
        paging = Paging(current = 1, total = 10),
        response = listOf(
            ResponseItem(
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
                    photo = "https://upload.wikimedia.org/wikipedia/commons/8/8c/Lionel_Messi_WC2022.jpg",
                    number = TODO(),
                    position = TODO()
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
        PlayerDetails(
            playerDataResponse = playerDataResponse,
            viewModel = viewModel,
            navController = rememberNavController()
        )
    }
}

/**
 * PlayerScreen: Se encarga de observar el estado (loading, error y datos) del ViewModel.
 * Si se detectan datos, llama a PlayerDetails para mostrarlos.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    playerId: Int,
    season: Int,
    navController: NavController,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val playerDataState by viewModel.playerData.observeAsState()
    val loading by viewModel.loading.observeAsState(initial = false)
    val error by viewModel.error.observeAsState()

    LaunchedEffect(playerId) {
        viewModel.fetchPlayerData(playerId, season)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo base
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
                .height(175.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.wikifutfondo1),
                contentDescription = "Background Blur",
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 10.dp),
                contentScale = ContentScale.FillBounds
            )
        }

        // Box con sombra
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // TopAppBar
            TopAppBar(
                modifier = Modifier.height(80.dp),
                title = { 
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Detalles del Jugador",
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
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Atrás",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                actions = {
                    if (playerDataState != null) {
                        val player = playerDataState!!.response.first().player
                        val favoritos by viewModel.favoritePlayersList.collectAsState()
                        val isFavorite = favoritos.any { it.id == player.id }
                        val coroutineScope = rememberCoroutineScope()

                        LaunchedEffect(Unit) {
                            viewModel.cargarJugadoresFavoritos()
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(end = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    if (isFavorite) {
                                        Log.d("TeamScreen", "Se elimino el favorito")
                                        coroutineScope.launch {
                                            viewModel.eliminarJugadorDeFavoritos(player)
                                            viewModel.cargarJugadoresFavoritos()
                                        }
                                    } else {
                                        coroutineScope.launch {
                                            viewModel.agregarJugadorAFavoritos(player)
                                            viewModel.cargarJugadoresFavoritos()
                                        }
                                        Log.d("TeamScreen", "Se agrego el favorito teamId: ${player.id}")
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                                    contentDescription = "Favorito",
                                    tint = if (isFavorite) Color.Yellow else Color.White,
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )

            // Card del jugador
            if (playerDataState != null) {
                PlayerDetails(
                    playerDataResponse = playerDataState!!, 
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

/**
 * PlayerDetails: Muestra la información del jugador.
 * Se extrae el primer elemento de la lista response.
 * La posición se obtiene del primer estadístico (dentro de games) y se muestra.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerDetails(
    playerDataResponse: PlayerDataResponse, 
    viewModel: PlayerViewModel = hiltViewModel(),
    navController: NavController
) = if (playerDataResponse.response.isEmpty()) {
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
    val statistic = playerData.statistics?.first()
    val statisticGame = statistic?.games
    val statisticTeamPlayer = statistic?.team
    val statisticPlayerLeague = statistic?.league
    val rating = statisticGame?.rating
    val team_player = statisticTeamPlayer?.name
    val position = playerData.statistics?.firstOrNull()?.games?.position ?: "N/A"
    val numberPlayer = statisticGame?.number ?: 0

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Card fija del jugador pegada al TopAppBar
        if (team_player != null) {
            headerVistaPlayer(
                name = player.name, 
                photo = player.photo, 
                team = team_player, 
                rating = rating
            )
        }

        // Contenido scrolleable debajo de la card
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 95.dp) // Espacio para la card del jugador
        ) {
            item {
                if (statisticTeamPlayer != null) {
                    if (team_player != null) {
                        player.weight?.let {
                            containerBasicInfo(
                                team = team_player,
                                imageTeam = statisticTeamPlayer.logo,
                                nacionalidad = player.nationality,
                                nacimiento = player.age,
                                altura = player.height,
                                posicion = position,
                                camiseta = numberPlayer,
                                peso = it
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 60.dp)
                ) {
                    // Box con blur
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clip(RoundedCornerShape(topStart = 25.dp, bottomEnd = 25.dp))
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
                            .height(40.dp)
                            .clip(RoundedCornerShape(topStart = 25.dp, bottomEnd = 25.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                    )

                    // Surface con el contenido
                    Surface(
                        color = Color.Transparent,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(topStart = 25.dp, bottomEnd = 25.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "TEMPORADA",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            item {
                if (statistic != null) {
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

            item {
                Spacer(Modifier.height(8.dp))
            }

            item {
                if (statisticPlayerLeague != null) {
                    containerResume(statisticPlayerLeague, statisticGame)
                }
            }
        }
    }
}

@Composable
fun headerVistaPlayer(name: String, photo: String, team: String ,rating: String?){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .background(Color.Transparent)
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
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
            Text(text = name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp)

            Text(text = team?: "No team",
                color = Color.White
                )
        }

        Spacer(modifier = Modifier.width(16.dp))

        val ratingValue = rating?.toFloatOrNull()
        val isLowRating = ratingValue?.let { it < 7.0 } ?: false
        val formattedRating = ratingValue?.let {
            "%.1f".format(it)
        } ?: "N/A"

        Text(
            text = formattedRating ?: "N/A",
            color = if (isLowRating) Color.Yellow else Color(0xFF4CAF50),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            style = TextStyle(
                shadow = Shadow(color = Color.Black)
            )
        )
    }
}

@Composable
fun containerBasicInfo(
    team: String,
    imageTeam: String,
    nacionalidad: String,
    nacimiento: Int,
    altura: String,
    posicion: String,
    camiseta: Int,
    peso: String
) {
    Surface(
        color = Color(0xFF2E0854),
        contentColor = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            //---------- Equipo ----------
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberImagePainter(imageTeam),
                    contentDescription = "Escudo del equipo",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = team,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp)
            }

            Spacer(Modifier.height(30.dp))

            //---------- Primera fila de datos ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("NACIÓN", fontWeight = FontWeight.Bold,
                        fontSize = 15.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(nacionalidad)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("EDAD", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(text = nacimiento.toString())
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("ALTURA", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(altura)
                }
            }

            Spacer(Modifier.height(30.dp))

            //---------- Segunda fila de datos ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("POSICIÓN", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(posicion)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("CAMISETA", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(camiseta.toString())
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("PESO", fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        color = Color(0xFF2E0854),
        contentColor = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { expanded = !expanded }
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .heightIn(max = if (expanded) Dp.Unspecified else 204.dp)
        ) {
            // Sección superior de StatisticBars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    StatisticBar(label = "GOLES:",
                        value = (goals?.total ?: 0).toInt(),
                        maxValue = (shoots?.on ?: 0))

                    Spacer(Modifier.height(20.dp))

                    StatisticBar(label = "(%) PASES:",
                        value = (passes?.accuracy ?: 0).toInt(),
                        maxValue = 100)

                    Spacer(Modifier.height(20.dp))

                    StatisticBar(label = "DRIBBLES:",
                        value = (dribbles?.success ?: 0),
                        maxValue = 100)
                }

                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    StatisticBar(label = "DUELOS:",
                        value = (duels?.won ?: 0),
                        maxValue = (duels?.total ?: 0))

                    Spacer(Modifier.height(20.dp))

                    StatisticBar(label = "DISPAROS:",
                        value = shoots?.on ?: 0,
                        maxValue = shoots?.total ?: 0)

                    Spacer(Modifier.height(20.dp))

                    StatisticBar(label = "TACKLES:",
                        value = (tackles?.interceptions ?: 0),
                        maxValue = tackles?.total ?: 0)
                }
            }

            Spacer(Modifier.height(30.dp))

            // Sección inferior de texto
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                        Text("ASISTENCIAS:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp)
                        Text(text = goals?.assists.toString())
                    }

                    Spacer(Modifier.height(20.dp))

                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                        Text("FALTAS:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp)
                        Text(text = fouls?.drawn.toString())
                    }

                    Spacer(Modifier.height(20.dp))

                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                        Text("PASES CLAVES:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp)
                        Text(text = passes?.key.toString())
                    }
                }

                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                        Text("PENALTIS:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp)
                        Text(text = penalty?.won.toString())
                    }

                    Spacer(Modifier.height(20.dp))

                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                        Text("AMARILLAS:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp)
                        Text(text = cards?.yellow.toString())
                    }

                    Spacer(Modifier.height(20.dp))

                    Column(verticalArrangement = Arrangement.SpaceBetween) {
                        Text("ROJAS:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp)
                        Text(text = cards?.red.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun containerResume( league: PlayerLeague, games: PlayerGames? ){
    Surface(
        color = Color(0xFF2E0854),
        contentColor = Color.White,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ){
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            //--------LIGA---------
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberImagePainter(league.logo),
                    contentDescription = "Escudo de la liga",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)){
                    Text(text = league.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp)
                    Text(text = league.season.toString())
                }
            }

            Spacer(Modifier.height(30.dp))

            //-------Games----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Columna izquierda
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "APARICIONES",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        games?.appearences?.toString() ?: "-",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "MINUTOS",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        games?.minutes?.toString() ?: "-",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                // Columna derecha
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "TITULAR",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Text(
                        games?.lineups?.toString() ?: "-",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        "CAPITAN",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = games?.captain?.let { if (it) "Sí" else "No" } ?: "-"
                    )
                }
            }
        }
    }
}