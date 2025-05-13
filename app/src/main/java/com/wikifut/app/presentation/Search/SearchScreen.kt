package com.wikifut.app.presentation.Search

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
import com.wikifut.app.model.Team // Asegúrate de que la ruta a tu data class Team sea correcta
import com.wikifut.app.model.TipoBusqueda // Asegúrate de que la ruta a tu enum TipoBusqueda sea correcta

@Composable
fun SearchScreen(
    tipo: TipoBusqueda, // El tipo de búsqueda que quieres realizar
    query: String, // La consulta de búsqueda
    viewModel: SearchViewModel = hiltViewModel()
) {
    // Disparar la búsqueda al entrar a la pantalla o si cambian el tipo o la consulta
    LaunchedEffect(key1 = tipo, key2 = query) {
        if (query.isNotEmpty()) {
            viewModel.buscar(tipo, query)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Aquí solo mostramos el resultado para equipos como pediste
        if (tipo == TipoBusqueda.Equipos) {
            EquiposResult(viewModel)
            //Text("query: $query tipo: $tipo")
        } else {
            // Puedes añadir lógica aquí para otros tipos de búsqueda si es necesario
            Text("Seleccione un tipo de búsqueda válido para ver resultados.")
        }
    }
}

@Composable
private fun EquiposResult(viewModel: SearchViewModel) {
    // Recolecta el estado de los resultados de equipos del ViewModel
    val resultado by viewModel.resultadoEquipos.collectAsState()
    print(resultado)
    // Muestra un indicador de carga o un mensaje si los resultados son nulos
    if (resultado == null) {
        // Aquí puedes mostrar un CircularProgressIndicator o un mensaje de "Cargando..."
        //Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        //    CircularProgressIndicator()
        //}
        Text("No hay resultados")
    } else {
        // Si hay resultados, muestra la lista de equipos en un LazyColumn
        LazyColumn {
            resultado?.response?.let { equipos ->
                items(equipos) { equipo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = equipo.logo,
                            contentDescription = "Logo ${equipo.name}",
                            modifier = Modifier.size(48.dp)
                        ) // <--- Cierra el paréntesis de AsyncImage aquí
                        Spacer(modifier = Modifier.width(16.dp)) // <--- Spacer está fuera de AsyncImage
                        Text(text = equipo.name) // <--- Text está fuera de AsyncImage
                    }
                }
            }
        }
    }
}

@Composable
fun TeamItem(team: Team) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Muestra el logo del equipo usando Coil
        AsyncImage(
            model = team.logo,
            contentDescription = "Logo ${team.name}",
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        // Muestra el nombre del equipo
        Text(
            text = team.name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}