package com.wikifut.app.presentation.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wikifut.app.model.MatchResponse
import com.wikifut.app.repository.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MatchViewModel(private val repository: MatchRepository) : ViewModel() {

    private val _match = MutableStateFlow<MatchResponse?>(null)
    val match: StateFlow<MatchResponse?> = _match.asStateFlow()

    fun fetchMatch(matchId: Long) {
        viewModelScope.launch {
            val response = repository.getMatchById(matchId)
            _match.value = response
        }
    }
}
