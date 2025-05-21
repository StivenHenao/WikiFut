package com.wikifut.app.presentation.Ligas
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.LigaData
import com.wikifut.app.repository.LigasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.wikifut.app.model.LigaInfo
import com.wikifut.app.model.PaisInfo

@HiltViewModel
class LigasViewModel @Inject constructor(
    private val repository: LigasRepository
): ViewModel() {

    private val _ligas = mutableStateOf<List<LigaData>>(emptyList())
    val ligas: State<List<LigaData>> = _ligas

    init {
        obtenerLigas()
    }

    var searchQuery = mutableStateOf("")
        private set

    var ligasFiltradas = mutableStateOf<List<LigaData>>(emptyList())
        private set

    fun actualizarBusqueda(query: String) {
        searchQuery.value = query
        filtrarLigas()
    }

    private fun filtrarLigas() {
        val query = searchQuery.value.lowercase()

        ligasFiltradas.value = if (query.isBlank()) {
            // Muestra destacadas si no hay búsqueda
            _ligas.value
        } else {
            todasLasLigas.value.filter { liga ->
                liga.league.name.lowercase().contains(query) ||
                        liga.country.name.lowercase().contains(query)
            }
        }
    }


    private val todasLasLigas = mutableStateOf<List<LigaData>>(emptyList())

    private fun obtenerLigas() {
        viewModelScope.launch {
            Log.d("LigasViewModel", " Llamando a API para obtener ligas")
            val resultado = repository.obtenerLigas()

            if (resultado != null) {
                Log.d("LigasViewModel", "✅ Se recibieron ${resultado.size} ligas")
                resultado.forEach { liga ->
                    Log.d("LigasViewModel", "📌 Liga: ${liga.league.name} (${liga.country.name})")
                }
            } else {
                Log.e("LigasViewModel", "❌ Error al obtener ligas, resultado nulo")
            }

            resultado?.let {
                todasLasLigas.value = it
                _ligas.value = it.filter { liga ->
                    liga.league.id in listOf(39, 140, 135, 61, 78, 128, 71, 239)
                }
                filtrarLigas()
            }
        }
    }



}