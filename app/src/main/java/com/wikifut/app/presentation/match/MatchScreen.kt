package com.wikifut.app.presentation.match

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wikifut.app.R
import com.wikifut.app.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.navigation.NavHostController

// Colores
val DarkPurpleBackground = Color(0xFF1A1032)
val TabRowBackground = Color(0xFF2C1F4A)
val AccentYellow = Color(0xFFFFD700)
val AccentPurple = Color(0xFFE040FB)
val TextColorLight = Color.White
val TextColorSecondary = Color.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScreen(
    matchId: Long,
    viewModel: MatchViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val match: MatchResponse? by viewModel.match.collectAsState()

    // Refrescar el partido cada 60 segundos
    LaunchedEffect(matchId) {
        while (true) {
            viewModel.fetchMatch(matchId)
            Log.i("wikifut", "MatchScreen: Re-fetching match data in 60s for ID: $matchId")
            delay(60000)
        }
    }

    match?.let { currentMatchData ->
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.height(70.dp),
                    title = { 
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Detalles del Partido",
                                color = TextColorLight,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Atrás",
                                tint = TextColorLight,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = DarkPurpleBackground,
                        titleContentColor = TextColorLight,
                        navigationIconContentColor = TextColorLight
                    )
                )
            },
            containerColor = DarkPurpleBackground
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
                    .background(DarkPurpleBackground)
            ) {
                MatchHeader(match = currentMatchData)
                MatchTabs(match = currentMatchData)
            }
        }
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkPurpleBackground)
                .statusBarsPadding(), 
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AccentYellow)
        }
    }
}

@Composable
fun MatchHeader(match: MatchResponse) {
    val fixture = match.fixture
    val league = match.league
    val teams = match.teams
    val score = match.score

    Log.i("Informacion partido", "LineUps: ${match.lineups}")
    Log.i("Informacion partido", "ID partido: ${match.fixture.id}")

    val formattedDate = remember(fixture.date) {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEEE, d 'de' MMMM yyyy - HH:mm", Locale("es", "ES"))
            val date = inputFormat.parse(fixture.date)
            date?.let { outputFormat.format(it) } ?: fixture.date
        } catch (e: Exception) {
            Log.e("MatchHeader", "Error parsing date: ${fixture.date}", e)
            fixture.date
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = league.name,
                    color = TextColorLight,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formattedDate,
                    color = TextColorSecondary,
                    fontSize = 11.sp
                )
            }

            IconButton(onClick = { /* TODO: Lógica de favorito */ }) {
                Icon(Icons.Default.StarBorder, contentDescription = "Favorito", tint = TextColorLight)
            }
            IconButton(onClick = { /* TODO: Lógica de compartir */ }) {
                Icon(Icons.Default.Share, contentDescription = "Compartir", tint = TextColorLight)
            }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TeamDisplay(team = teams.home, alignment = Alignment.CenterHorizontally)
            Spacer(Modifier.width(8.dp))
            ScoreDisplay(fixture = fixture, score = score)
            Spacer(Modifier.width(8.dp))
            TeamDisplay(team = teams.away, alignment = Alignment.CenterHorizontally)
        }
    }
}

@Composable
fun TeamDisplay(team: MRTeam, alignment: Alignment.Horizontal) {
    Column(
        horizontalAlignment = alignment,
        modifier = Modifier.widthIn(max = 100.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(team.logo)
                .crossfade(true)
                .build(),
            contentDescription = "Logo ${team.name}",
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = team.name,
            color = TextColorLight,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        if (team.winner == true) {
            Icon(
                painterResource(id = R.drawable.ic_star_filled),
                contentDescription = "Ganador",
                tint = AccentYellow,
                modifier = Modifier.size(16.dp).padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun ScoreDisplay(fixture: MRFixture, score: MRScore) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "${score.fulltime.home ?: "-"} - ${score.fulltime.away ?: "-"}",
            color = TextColorLight,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = fixture.status.short.uppercase(),
            color = TextColorSecondary,
            fontSize = 12.sp
        )
        Text(
            text = "HT ${score.halftime.home ?: "-"} - ${score.halftime.away ?: "-"}",
            color = TextColorSecondary,
            fontSize = 11.sp
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MatchTabs(match: MatchResponse) {
    val tabTitles = listOf("Alineaciones", "Estadísticas")
    val pagerState = rememberPagerState { tabTitles.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.background(TabRowBackground)) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = TabRowBackground,
            contentColor = AccentYellow,
            indicator = { tabPositions ->
                if (pagerState.currentPage < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = AccentYellow
                    )
                }
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title) },
                    selectedContentColor = AccentYellow,
                    unselectedContentColor = TextColorSecondary
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(DarkPurpleBackground)
        ) { page ->
            when (page) {
                0 -> LineupsScreen(
                    lineups = match.lineups,
                    teams = match.teams,
                    modifier = Modifier.fillMaxSize()
                )
                1 -> StatisticsScreen(
                    teamStatistics = match.statistics,
                    teams = match.teams,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun LineupsScreen(
    lineups: List<MRLineup>,
    teams: MRTeams,
    modifier: Modifier = Modifier
) {
    val homeLineup = lineups.find { it.team.id == teams.home.id }
    val awayLineup = lineups.find { it.team.id == teams.away.id }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.football_pitch_background),
            contentDescription = "Campo de fútbol",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            homeLineup?.let { lineup ->
                TeamLineupDetail(
                    teamName = teams.home.name,
                    formation = lineup.formation,
                    coach = lineup.coach,
                    players = lineup.startXI,
                    isHomeTeam = true,
                    teamColor = AccentYellow,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            awayLineup?.let { lineup ->
                TeamLineupDetail(
                    teamName = teams.away.name,
                    formation = lineup.formation,
                    coach = lineup.coach,
                    players = lineup.startXI,
                    isHomeTeam = false,
                    teamColor = AccentPurple,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

fun parseGrid(gridString: String?): Pair<Int, Int>? {
    return gridString?.split(':')
        ?.takeIf { it.size == 2 }
        ?.let { parts ->
            parts.getOrNull(0)?.toIntOrNull()?.let { row ->
                parts.getOrNull(1)?.toIntOrNull()?.let { col ->
                    Pair(row, col)
                }
            }
        }
}

@Composable
fun TeamLineupDetail(
    teamName: String,
    formation: String?,
    coach: MRCoach?,
    players: List<MRStartingPlayer>, // Esta es la lista de startXI
    isHomeTeam: Boolean,
    teamColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "${teamName.uppercase()} (${formation ?: "N/A"})",
            color = TextColorLight,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        coach?.name?.let {
            Text(
                "Entrenador: $it",
                color = TextColorSecondary,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .aspectRatio(105 / 60f) // Aspecto del campo
        ) {
            val playersWithParsedGrid = remember(players) {
                players.mapNotNull { playerNode ->
                    parseGrid(playerNode.player.grid)?.let { parsedGrid ->
                        Triple(playerNode, parsedGrid.first, parsedGrid.second)
                    }
                }
            }

            val playersGroupedByGridRow = remember(playersWithParsedGrid) {
                playersWithParsedGrid.groupBy { it.second }
            }

            // Configuracion de posiciones de los jugadores en el grid TODO: mejorar?
            playersWithParsedGrid.forEach { (playerNode, gridRow, gridCol) ->
                val verticalBiasValue = when (gridRow) {
                    1 -> if (!isHomeTeam) 1.06f else -1.06f  // Portero (más cerca del borde)
                    2 -> if (!isHomeTeam) 0.75f else -0.75f  // Defensas (línea inferior de defensa)
                    3 -> if (!isHomeTeam) -0.0f else -0.0f  // Mediocampistas (línea central)
                    4 -> if (!isHomeTeam) -0.9f else 0.9f // Delanteros/Mediapuntas (línea superior de ataque)
                    5 -> if (!isHomeTeam) -1.1f else 1.1f // Delantero más adelantado (si existe)
                    else -> 0f
                }

                val playersInThisFormationLine = playersGroupedByGridRow.getOrDefault(gridRow, emptyList())
                val countInLine = playersInThisFormationLine.size

                val maxSpread = 0.9f
                val horizontalBiasValue = if (countInLine > 1) {
                    val spacing = 1f / (countInLine - 1)
                    val position = (gridCol - 1).coerceAtLeast(0)
                    -maxSpread + position * spacing * 2 * maxSpread
                } else {
                    0f
                }


                PlayerMarkerOnPitch(
                    player = playerNode.player,
                    alignment = BiasAlignment(horizontalBias = horizontalBiasValue, verticalBias = verticalBiasValue),
                    teamColor = teamColor
                )
            }

            val playersWithoutValidGrid = remember(players, playersWithParsedGrid) {
                val griddedPlayerIds = playersWithParsedGrid.map { it.first.player.id }.toSet()
                players.filterNot { playerNode -> griddedPlayerIds.contains(playerNode.player.id) }
            }

            if (playersWithoutValidGrid.isNotEmpty()) {
                Log.w("TeamLineupDetail", "Jugadores sin grid válido para ${teamName}: ${playersWithoutValidGrid.joinToString { it.player.name }}")
                // Podrías implementar una lógica de fallback aquí si es necesario
            }
        }
    }
}


@Composable
fun BoxScope.PlayerMarkerOnPitch(
    player: MRPlayerLineupInfo,
    alignment: BiasAlignment,
    teamColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.align(alignment)
    ) {
        Box(
            modifier = Modifier
                .size(31.dp)
                .clip(CircleShape)
                .background(teamColor.copy(alpha = 0.8f))
                .padding(1.dp)
                .background(DarkPurpleBackground.copy(alpha=0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = player.number.toString(),
                color = TextColorLight,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = player.name.substringAfterLast(" ").ifEmpty { player.name }.take(10),
            color = TextColorLight,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(60.dp),
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StatisticsScreen(
    teamStatistics: List<MRTeamStatistics>,
    teams: MRTeams,
    modifier: Modifier = Modifier
) {
    // Verificar si teamStatistics esta vacia
    if (teamStatistics.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Estadísticas aún no disponibles.",
                color = TextColorSecondary,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    // Ejecucion normal cuando existen estadisticas
    val homeTeamId = teams.home.id
    val awayTeamId = teams.away.id

    val homeStatsMap = remember(teamStatistics, homeTeamId) {
        teamStatistics.find { it.team.id == homeTeamId }?.statistics?.associate { it.type to it.value } ?: emptyMap()
    }
    val awayStatsMap = remember(teamStatistics, awayTeamId) {
        teamStatistics.find { it.team.id == awayTeamId }?.statistics?.associate { it.type to it.value } ?: emptyMap()
    }

    // if (homeStatsMap.isEmpty() && awayStatsMap.isEmpty()) {
    //     Box(
    //         modifier = modifier.fillMaxSize().padding(16.dp),
    //         contentAlignment = Alignment.Center
    //     ) {
    //         Text(
    //             text = "No se encontraron estadísticas para los equipos.",
    //             color = TextColorSecondary,
    //             fontSize = 16.sp,
    //             textAlign = TextAlign.Center
    //         )
    //     }
    //     return
    // }


    val statsOrder = listOf(
        "Ball Possession" to "Posesión (%)",
        "Shots on Goal" to "Tiros a gol",
        "Yellow Cards" to "Tarjetas Amarillas",
        "Corner Kicks" to "Tiros de esquina",
        "Total passes" to "Pases Totales",
        "Red Cards" to "Tarjetas Rojas",
        "Goalkeeper Saves" to "Salvadas"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Posesion
        val homePossessionStr = homeStatsMap["Ball Possession"] ?: "0%"
        val awayPossessionStr = awayStatsMap["Ball Possession"] ?: "0%"
        val homePossessionVal = homePossessionStr.removeSuffix("%").toFloatOrNull() ?: 0f
        val awayPossessionVal = awayPossessionStr.removeSuffix("%").toFloatOrNull() ?: 0f
        item {
            PossessionStatisticRow(
                label = "Posesión (%)",
                homeValue = homePossessionStr,
                awayValue = awayPossessionStr,
                homePercentage = homePossessionVal / 100f,
                awayPercentage = awayPossessionVal / 100f
            )
        }

        // Estadisticas
        statsOrder.filterNot { it.first == "Ball Possession" }.forEach { (apiKey, displayLabel) ->
            item {
                val homeValueStr = homeStatsMap[apiKey] ?: "0" // Default "0" si no se encuentra
                val awayValueStr = awayStatsMap[apiKey] ?: "0" // Default "0" si no se encuentra
                val homeVal = homeValueStr.toFloatOrNull() ?: 0f
                val awayVal = awayValueStr.toFloatOrNull() ?: 0f

                ComparisonStatisticRow(
                    label = displayLabel,
                    homeValue = homeValueStr,
                    awayValue = awayValueStr,
                    homeProgress = homeVal,
                    awayProgress = awayVal
                )
            }
        }

        // TODO: en el modelo no existen lesiones, revisar si la api las arroja
        item {
            ComparisonStatisticRow(
                label = "Lesiones",
                homeValue = "0",
                awayValue = "0",
                homeProgress = 0f,
                awayProgress = 0f
            )
        }
    }
}

@Composable
fun PossessionStatisticRow(
    label: String,
    homeValue: String,
    awayValue: String,
    homePercentage: Float,
    awayPercentage: Float
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(homeValue, color = AccentYellow, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.2f), textAlign = TextAlign.Start)
            Text(label, color = TextColorSecondary, fontSize = 12.sp, modifier = Modifier.weight(0.6f).padding(horizontal = 8.dp), textAlign = TextAlign.Center)
            Text(awayValue, color = AccentPurple, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.2f), textAlign = TextAlign.End)
        }
        Spacer(Modifier.height(4.dp))

        Row(Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp))) {
            val totalPercentage = (homePercentage + awayPercentage).coerceAtLeast(0.001f) // Evitar división por cero si ambos son 0
            Box(modifier = Modifier.weight(homePercentage / totalPercentage).background(AccentYellow))
            Box(modifier = Modifier.weight(awayPercentage / totalPercentage).background(AccentPurple))
        }
    }
}

@Composable
fun ComparisonStatisticRow(
    label: String,
    homeValue: String,
    awayValue: String,
    homeProgress: Float,
    awayProgress: Float
) {
    val total = (homeProgress + awayProgress).coerceAtLeast(1f)
    val homeNormalizedProgress = if (homeProgress + awayProgress == 0f) 0f else homeProgress / total
    val awayNormalizedProgress = if (homeProgress + awayProgress == 0f) 0f else awayProgress / total

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                homeValue,
                color = AccentYellow,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.15f),
                textAlign = TextAlign.Start
            )
            Text(
                label,
                color = TextColorSecondary,
                fontSize = 12.sp,
                modifier = Modifier.weight(0.7f).padding(horizontal = 4.dp), // Peso para la etiqueta
                textAlign = TextAlign.Center
            )
            Text(
                awayValue,
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.15f),
                textAlign = TextAlign.End
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = { homeNormalizedProgress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = AccentYellow,
                trackColor = AccentYellow.copy(alpha = 0.3f),
                strokeCap = StrokeCap.Round
            )
            Spacer(Modifier.width(8.dp))
            LinearProgressIndicator(
                progress = { awayNormalizedProgress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = AccentPurple,
                trackColor = AccentPurple.copy(alpha = 0.3f),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

// --- Para el Preview - Datos generados ---
class PreviewMatchViewModelPlaceholder {
    private val _matchFlow = MutableStateFlow<MatchResponse?>(null)
    val match: StateFlow<MatchResponse?> = _matchFlow.asStateFlow()

    init {
        _matchFlow.value = createSampleMatchResponseForPreview()
    }

    private fun createSampleMatchResponseForPreview(): MatchResponse {
        val homeTeam = MRTeam(id = 1, name = "Real Madrid", logo = "url_logo_rm", winner = false, colors = null)
        val awayTeam = MRTeam(id = 2, name = "Barcelona", logo = "url_logo_bcn", winner = true, colors = null)

        return MatchResponse(
            fixture = MRFixture(
                id = 123, referee = "Undiano Mallenco", timezone = "UTC", date = "2009-05-02T20:00:00+02:00",
                timestamp = 1241294400, periods = MRPeriods(1241294400, 1241298000),
                venue = MRVenue(name = "Santiago Bernabéu", city = "Madrid"),
                status = MRStatus("Full Time", "FT", 90)
            ),
            league = MRLeague(140, "Liga Española", "Spain", "league_logo_url", "flag_url", 2008, "Jornada 34", true),
            teams = MRTeams(homeTeam, awayTeam),
            goals = MRGoals(2, 6),
            score = MRScore(MRGoals(1,3), MRGoals(2,6), MRGoals(null,null), MRGoals(null,null)),
            events = emptyList(),
            lineups = listOf(
                MRLineup(homeTeam, MRCoach(101, "J. Ramos", "coach_photo_url"), "4-4-2",
                    startXI = listOf(
                        MRStartingPlayer(MRPlayerLineupInfo(1001, "Iker Casillas", 1, MRPosition.G, "1:1")),
                        MRStartingPlayer(MRPlayerLineupInfo(1002, "Sergio Ramos", 4, MRPosition.D, "2:4")),
                        MRStartingPlayer(MRPlayerLineupInfo(1003, "F. Cannavaro", 5, MRPosition.D, "2:3")),
                        MRStartingPlayer(MRPlayerLineupInfo(1004, "G. Heinze", 16, MRPosition.D, "2:2")),
                        MRStartingPlayer(MRPlayerLineupInfo(1005, "Marcelo", 12, MRPosition.D, "2:1")),
                        MRStartingPlayer(MRPlayerLineupInfo(1006, "A. Robben", 11, MRPosition.M, "3:4")),
                        MRStartingPlayer(MRPlayerLineupInfo(1007, "F. Gago", 8, MRPosition.M, "3:1")),
                        MRStartingPlayer(MRPlayerLineupInfo(1008, "L. Diarra", 6, MRPosition.M, "3:2")),
                        MRStartingPlayer(MRPlayerLineupInfo(1011, "W. Sneijder", 23, MRPosition.M, "3:3")),
                        MRStartingPlayer(MRPlayerLineupInfo(1009, "G. Higuaín", 20, MRPosition.F, "4:2")),
                        MRStartingPlayer(MRPlayerLineupInfo(1010, "Raúl G.", 7, MRPosition.F, "4:1"))
                    ),
                    substitutes = emptyList()),
                MRLineup(awayTeam, MRCoach(201, "Pep Guardiola", "coach_photo_url"), "4-3-3",
                    startXI = listOf(
                        MRStartingPlayer(MRPlayerLineupInfo(2001, "Víctor Valdés", 1, MRPosition.G, "1:1")),
                        MRStartingPlayer(MRPlayerLineupInfo(2002, "Dani Alves", 20, MRPosition.D, "2:1")),
                        MRStartingPlayer(MRPlayerLineupInfo(2003, "Gerard Piqué", 3, MRPosition.D, "2:2")),
                        MRStartingPlayer(MRPlayerLineupInfo(2004, "Carles Puyol", 5, MRPosition.D, "2:3")),
                        MRStartingPlayer(MRPlayerLineupInfo(2005, "Éric Abidal", 22, MRPosition.D, "2:4")),
                        MRStartingPlayer(MRPlayerLineupInfo(2006, "Yaya Touré", 24, MRPosition.M, "3:2")),
                        MRStartingPlayer(MRPlayerLineupInfo(2007, "Xavi H.", 6, MRPosition.M, "3:1")),
                        MRStartingPlayer(MRPlayerLineupInfo(2008, "Andrés Iniesta", 8, MRPosition.M, "3:3")),
                        MRStartingPlayer(MRPlayerLineupInfo(2009, "Lionel Messi", 10, MRPosition.F, "4:2")),
                        MRStartingPlayer(MRPlayerLineupInfo(2010, "Samuel Eto'o", 9, MRPosition.F, "4:1")),
                        MRStartingPlayer(MRPlayerLineupInfo(2011, "Thierry Henry", 14, MRPosition.F, "4:3"))
                    ),
                    substitutes = emptyList())
            ),
            statistics = listOf(
                MRTeamStatistics(homeTeam, listOf(
                    MRStatistic("Ball Possession", "35%"), MRStatistic("Shots on Goal", "7"),
                    MRStatistic("Yellow Cards", "2"), MRStatistic("Corner Kicks", "4"),
                    MRStatistic("Total passes", "350"), MRStatistic("Red Cards", "0"),
                    MRStatistic("Goalkeeper Saves", "5")
                )),
                MRTeamStatistics(awayTeam, listOf(
                    MRStatistic("Ball Possession", "65%"), MRStatistic("Shots on Goal", "15"),
                    MRStatistic("Yellow Cards", "1"), MRStatistic("Corner Kicks", "6"),
                    MRStatistic("Total passes", "680"), MRStatistic("Red Cards", "0"),
                    MRStatistic("Goalkeeper Saves", "3")
                ))
            ),
            players = emptyList()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1032)
@Composable
fun MatchScreenContentPreview() {
    val previewViewModel = PreviewMatchViewModelPlaceholder()
    val matchForPreview by previewViewModel.match.collectAsState()

    MaterialTheme {
        matchForPreview?.let { currentMatchData ->
            Scaffold(
                containerColor = DarkPurpleBackground,
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(DarkPurpleBackground)
                ) {
                    MatchHeader(match = currentMatchData)
                    MatchTabs(match = currentMatchData)
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize().background(DarkPurpleBackground), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentYellow)
            }
        }
    }
}