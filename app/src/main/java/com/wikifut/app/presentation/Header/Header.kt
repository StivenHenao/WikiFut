package com.wikifut.app.presentation.Header

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wikifut.app.R
import com.wikifut.app.model.TipoBusqueda

fun noHacerNada(tipoBusqueda: TipoBusqueda  , string: String) {
    // No hace nada
}


@Composable
fun Header(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onBuscar: (TipoBusqueda, String) -> Unit,
    actions: @Composable () -> Unit = {}
) {
    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Equipos") }
    var modoBusquedaActiva by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val menuOptions = listOf("Equipos", "Ligas", "Partidos", "Jugador")
    val dropdownBackgroundColor = Color(0xFF4A148C)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x991F1235))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo
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

        // Buscador + Dropdown
        Row(
            modifier = Modifier
                .weight(1f)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Buscar $selectedOption...") },
                modifier = Modifier
                    .weight(1f)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp) {
                            focusManager.clearFocus()
                            val tipoBusqueda = when (selectedOption) {
                                "Ligas" -> TipoBusqueda.Ligas
                                "Partidos" -> TipoBusqueda.Partidos
                                "Jugador" -> TipoBusqueda.Jugadores
                                else -> TipoBusqueda.Equipos
                            }
                            onBuscar(tipoBusqueda, searchQuery)
                            true
                        } else false
                    },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true,
                maxLines = 1,
            )

            if (modoBusquedaActiva) {
                IconButton(onClick = {
                    val tipoBusqueda = when (selectedOption) {
                        "Ligas" -> TipoBusqueda.Ligas
                        "Partidos" -> TipoBusqueda.Partidos
                        "Jugador" -> TipoBusqueda.Jugadores
                        else -> TipoBusqueda.Equipos
                    }
                    focusManager.clearFocus()
                    onBuscar(tipoBusqueda, searchQuery)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search), // Usa un ícono de búsqueda
                        contentDescription = "Buscar",
                        tint = Color.Black
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(dropdownBackgroundColor, shape = RoundedCornerShape(6.dp))
            ) {
                IconButton(onClick = { dropdownExpanded = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_drop_down),
                        contentDescription = "Seleccionar tipo",
                        tint = Color.White
                    )
                }
            }

            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                menuOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (!modoBusquedaActiva) {
            // Botón de activar modo búsqueda
            IconButton(onClick = { modoBusquedaActiva = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search), // mismo ícono de búsqueda
                    contentDescription = "Activar búsqueda",
                    tint = Color.White
                )
            }

            actions()
        } else {
            // Botón para salir del modo búsqueda
            IconButton(onClick = {
                modoBusquedaActiva = false
                focusManager.clearFocus()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close), // ícono de cerrar
                    contentDescription = "Cerrar búsqueda",
                    tint = Color.White
                )
            }
        }
    }
}
