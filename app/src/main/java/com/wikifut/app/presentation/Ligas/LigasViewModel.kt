package com.wikifut.app.presentation.Ligas
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

    private fun obtenerLigas() {
        viewModelScope.launch {
            val resultado = repository.obtenerLigas()
            resultado?.let {
                val ligasImportantes = listOf(39, 140, 135, 61, 78, 128, 71, 239) // IDs destacados

                _ligas.value = it.filter { liga ->
                    liga.league.id in ligasImportantes
                }.map { liga ->
                    // Asegurar que muestres solo la temporada actual
                    LigaData(
                        league = LigaInfo(
                            id = liga.league.id,
                            name = liga.league.name,
                            logo = liga.league.logo,
                            season = liga.league.season // temporada actual
                        ),
                        country = PaisInfo(
                            name = liga.country.name,
                            flag = liga.country.flag
                        )
                    )
                }
            }
        }
    }
}