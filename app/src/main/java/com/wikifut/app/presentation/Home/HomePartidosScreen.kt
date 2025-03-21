package com.wikifut.app.presentation.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.wikifut.app.R
import com.wikifut.app.model.Partido
import java.util.*
import com.wikifut.app.utils.convertirHoraAColombia
import com.wikifut.app.utils.formatFechaParaApi
import com.wikifut.app.utils.obtenerFechaActual

@Composable
fun HomePartidosScreen(
    viewModel: HomePartidosViewModel = hiltViewModel(),
    navigateToInitial: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(obtenerFechaActual()) }

    LaunchedEffect(selectedDate) {
        viewModel.cargarPartidosPorFecha(selectedDate)
    }

    val partidosFiltrados = state.filter { partido ->
        partido.league.name.contains(searchQuery, ignoreCase = true) ||
                partido.teams.home.name.contains(searchQuery, ignoreCase = true) ||
                partido.teams.away.name.contains(searchQuery, ignoreCase = true)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { date ->
                selectedDate = formatFechaParaApi(date)
                showDatePicker = false
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF2D1B45))) {
        Header(
            navigateToInitial = navigateToInitial,
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            onDateSelected = { showDatePicker = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Partidos del día", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Text(selectedDate, fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(partidosFiltrados) { partido -> PartidoCard(partido) }
            }

            if (partidosFiltrados.isEmpty()) {
                Text("No se encontraron partidos.", color = Color.White, modifier = Modifier.padding(16.dp))
            }
        }
    }
}


@Composable
fun Header(
    navigateToInitial: () -> Unit,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onDateSelected: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F1235))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, shape = CircleShape)
                .clip(CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.wikifutlogo),
                contentDescription = "Logo",
                modifier = Modifier.size(50.dp)
            )
        }


        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Buscar partidos...") },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.width(8.dp))


        IconButton(
            onClick = onDateSelected,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = "Seleccionar fecha",
                tint = Color.White
            )
        }

        Box {
            Image(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "Menú",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(
                    text = { Text("Cerrar sesión") },
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navigateToInitial()
                        expanded = false
                    }
                )
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
fun PartidoCard(partido: Partido) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A256F))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {


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


            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
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
                            textAlign = TextAlign.Center
                        )
                    }


                    Text(
                        text = " ${partido.goals?.home ?: "-"} | ${partido.goals?.away ?: "-"} ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )


                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
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
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(8.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = getEstadoPartido(partido),
                    fontSize = 14.sp,
                    color = if (partido.fixture.status.short == "FT") Color.Red else Color.Yellow,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )


                Text(
                    text = convertirHoraAColombia(partido.fixture.timestamp),
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                var isClicked by remember { mutableStateOf(false) }

                Image(
                    painter = painterResource(id = R.drawable.baseline_notifications_24),
                    contentDescription = "Notificación",
                    modifier = Modifier
                        .size(24.dp)
                        .weight(1f)
                        .clickable { isClicked = !isClicked },
                    colorFilter = ColorFilter.tint(if (isClicked) Color.Yellow else Color.White),
                    alignment = Alignment.CenterEnd
                )
            }


        }
    }
}


fun getEstadoPartido(partido: Partido): String {
    return when (partido.fixture.status.short) {
        "FT" -> "Finalizado"
        "CANC" -> "Cancelado"
        "NS" -> "Programado"
        else -> partido.fixture.status.long
    }
}