package com.wikifut.app.presentation.Home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.Partido
import com.wikifut.app.repository.PartidosRepository
import com.wikifut.app.utils.obtenerFechaActual
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@HiltViewModel
class HomePartidosViewModel @Inject constructor(
    private val partidosRepository: PartidosRepository
) : ViewModel() {

    private val _state = MutableStateFlow(emptyList<Partido>())
    val state: StateFlow<List<Partido>>
        get() = _state


    private val ligasImportantes = setOf(

        45, // FA Cup (Inglaterra)
        48, // EFL Cup (Inglaterra)
        39,  // Premier com.wikifut.app.model.League (Inglaterra)
        140, // España - La Liga
        143, // España - Copa del Rey
        556, // España - Supercopa
        135, // Italia - Serie A
        137, // Italia - Coppa Italia
        78, // Alemania - Bundesliga
        81, // Alemania - DFB Pokal
        61, // Francia - Ligue 1
        66, // Francia - Coupe de France
        65, // Francia - Coupe de la Ligue
        94, // Portugal - Primeira Liga
        97, // Portugal - Taça de Portugal


        71, // Brasil - Serie A
        73, // Brasil - Copa de Brasil
        128, // Argentina - Liga
        130, // Argentina - Copa Argentina
        239, // Colombia - Liga BetPlay
        241, // Colombia - Copa Colombia
        713, // Colombia - Superliga
        240, // Colombia - Primera B


        262, // México - Liga MX
        264, // México - Copa MX
        253, // USA - MLS
        257, // USA - US Open Cup

        307, // Arabia - Pro com.wikifut.app.model.League

        2, // Champions com.wikifut.app.model.League
        3, // Europa com.wikifut.app.model.League
        848, // Conference com.wikifut.app.model.League
        13, // Copa Libertadores
        11, // Copa Sudamericana

        5, // Nations com.wikifut.app.model.League
        31, // CONCACAF - Qualifiers
        34, // CONMEBOL - Qualifiers
        32, // UEFA - Qualifiers

        // 667, // Friendly Clubs HAY MUCHOS
        10, // Friendly International
    )

    init {
        cargarPartidosPorFecha(obtenerFechaActual())
    }

    fun cargarPartidosPorFecha(fechaColombia: String) {
        viewModelScope.launch {

            val fechaHoy = fechaColombia
            val fechaManana = obtenerFechaSiguiente(fechaColombia)


            val partidosHoy = partidosRepository.getPartidos(fechaHoy).response
            val partidosManana = partidosRepository.getPartidos(fechaManana).response


            val partidosTotales = (partidosHoy + partidosManana)


            val partidosFiltrados = partidosTotales.filter { partido ->
                partido.league.id in ligasImportantes && partidoEnFechaColombiana(partido, fechaHoy)
            }

            _state.value = partidosFiltrados
        }
    }

    private fun partidoEnFechaColombiana(partido: Partido, fechaColombia: String): Boolean {

        val fechaPartidoColombia = obtenerFechaColombia(partido.fixture.timestamp)

        return fechaPartidoColombia == fechaColombia
    }

    private fun obtenerFechaColombia(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("America/Bogota")
        return sdf.format(Date(timestamp * 1000))
    }

    private fun obtenerFechaSiguiente(fecha: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaDate = sdf.parse(fecha) ?: return fecha
        val calendar = Calendar.getInstance().apply { time = fechaDate }
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return sdf.format(calendar.time)
    }

}
