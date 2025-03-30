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
            resultado?.let { _ligas.value = it }
        }
    }
}