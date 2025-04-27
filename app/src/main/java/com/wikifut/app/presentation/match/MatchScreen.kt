package com.wikifut.app.presentation.match

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun MatchScreen(
    matchId: Long,
    viewModel: MatchViewModel = hiltViewModel()
) {
    val match by viewModel.match.collectAsState()


    Log.i("wikifut", "matchId: $matchId")

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Match ID: $matchId")
        Text(text = "Match: ${match?.teams?.home?.name} vs ${match?.teams?.away?.name}")
    }

    LaunchedEffect(matchId) {
        while (true) {
            viewModel.fetchMatch(matchId)
            delay(60000)
        }
    }

    match?.let { Text(text = "${it.teams.home.name} vs ${it.teams.away.name}") }

}