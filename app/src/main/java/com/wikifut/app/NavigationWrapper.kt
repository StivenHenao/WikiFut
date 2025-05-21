package com.wikifut.app

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.wikifut.app.model.Team
import com.wikifut.app.presentation.initial.InitialScreen
import com.wikifut.app.presentation.login.LoginScreen
import com.wikifut.app.presentation.match.MatchScreen
import com.wikifut.app.presentation.match.MatchViewModel
import com.wikifut.app.presentation.signup.SignUpScreen
import com.wikifut.app.presentation.editprofile.EditProfileScreen
import com.wikifut.app.presentation.Home.HomeScreenWithDrawer
import com.wikifut.app.model.TipoBusqueda
import com.wikifut.app.model.Venue
import com.wikifut.app.presentation.Favoritos.FavoritesViewModel
import com.wikifut.app.presentation.Search.SearchScreen
import com.wikifut.app.presentation.Team.TeamScreen
import com.wikifut.app.presentation.Ligas.LigaDetalleScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.hilt.navigation.compose.hiltViewModel
import com.wikifut.app.presentation.Favoritos.FavoritosScreen
import com.wikifut.app.presentation.player.PlayerScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {


    val isUserLoggedIn = auth.currentUser != null
    val startDestination = if (isUserLoggedIn) "home" else "initial"
    //val favoritesViewModel: FavoritesViewModel = hiltViewModel()
    val currentUser = auth.currentUser

    //LaunchedEffect(currentUser) {
    //currentUser?.let {
            //favoritesViewModel.cargarFavoritos()
            //Log.d("NavigationWrapper", "Favoritos cargados: ${favoritesViewModel.favoritos.value.toString()}")
    //}
    //}
    // Función que maneja la navegación a búsqueda
    val onSearchNavigate: (TipoBusqueda, String) -> Unit = { tipo, query ->
        when(tipo){
            TipoBusqueda.Equipos -> navHostController.navigate("busqueda/Equipos/$query")
            TipoBusqueda.Ligas -> navHostController.navigate("busqueda/Ligas/$query")
            TipoBusqueda.Partidos -> navHostController.navigate("busqueda/Partidos/$query")
            TipoBusqueda.Jugadores -> navHostController.navigate("busqueda/Jugadores/$query")
        }
    }
    val onTeamNavigate: (Team, Venue) -> Unit = { team, venue ->
        val teamJson = URLEncoder.encode(Gson().toJson(team), StandardCharsets.UTF_8.toString())
        val venueJson = URLEncoder.encode(Gson().toJson(venue), StandardCharsets.UTF_8.toString())
        navHostController.navigate("teamScreen/$teamJson/$venueJson")
    }

    val onPlayerNavigate: (String, String) -> Unit = { playerId, season ->
        navHostController.navigate("playerscreen/$playerId/$season")
    }

    val onLigasNavigate: (leagueId: Int, season: Int) -> Unit = { leagueId, season ->
        navHostController.navigate("ligaDetalle/$leagueId/$season")
    }


    NavHost(navController = navHostController, startDestination = startDestination) {
        composable("initial") {
            InitialScreen(
                navigateToLogin = { navHostController.navigate("login") },
                navigateToSignUp = { navHostController.navigate("signup") },
                navigateToHome = { navHostController.navigate("home") }
            )
        }
        composable("login") {
            LoginScreen(
                auth,
                navigateToInitial = { navHostController.navigate("initial") },
                navigateToSignUp = { navHostController.navigate("signup") },
                navigateToHome = {
                    navHostController.navigate("home") {
                        popUpTo("initial") { inclusive = true }
                    }
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                auth,
                navigateToInitial = { navHostController.navigate("initial") },
                navigateToLogin = { navHostController.navigate("login") },
                navigateToHome = {
                    navHostController.navigate("home") {
                        popUpTo("initial") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            Log.i("wikifut", "Entro a home")
            HomeScreenWithDrawer(
                navigateToEditProfile = { navHostController.navigate("edit_profile") },
                navigateToInitial = {
                    navHostController.navigate("initial") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                navigateToMatchDetail = { matchId -> navHostController.navigate("matchDetail/$matchId" )},
                onSearchNavigate = onSearchNavigate,
                onFavoritosNavigate = { navHostController.navigate("favoritos") }
            )
        }

        // Nueva ruta ↓↓↓
        composable("edit_profile") {
            EditProfileScreen(
                onBack = { navHostController.popBackStack() }
            )
        }

        composable(
            route = "matchDetail/{matchId}",
            arguments = listOf(
                navArgument("matchId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getLong("matchId")

            if (matchId != null) {
                MatchScreen(matchId = matchId)
            } else {
                navHostController.popBackStack("home", false)
            }
        }

        // ruta busquedas
        composable(
            "busqueda/{tipo}/{query}",
            arguments = listOf(
                navArgument("tipo") { type = NavType.StringType },
                navArgument("query") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val tipoString = backStackEntry.arguments?.getString("tipo") ?: "Equipos"
            val query = backStackEntry.arguments?.getString("query") ?: ""

            val tipo = when (tipoString) {
                "Ligas" -> TipoBusqueda.Ligas
                "Partidos" -> TipoBusqueda.Partidos
                "Jugadores" -> TipoBusqueda.Jugadores
                "Equipos" -> TipoBusqueda.Equipos
                else -> TipoBusqueda.Equipos
            }

            SearchScreen(
                tipo = tipo,
                query = query,
                onSearchNavigate = onSearchNavigate,
                HomeNavigate = {
                    navHostController.popBackStack("home", inclusive = false)
                },

                onTeamNavigate = onTeamNavigate,
                onPlayerNavigate = onPlayerNavigate,
                onLigasNavigate = onLigasNavigate
            )
        }


        composable("teamScreen/{teamJson}/{venueJson}") { backStackEntry ->
            val teamjson = URLDecoder.decode(backStackEntry.arguments?.getString("teamJson") ?: "", StandardCharsets.UTF_8.toString())
            val venuejson = URLDecoder.decode(backStackEntry.arguments?.getString("venueJson") ?: "", StandardCharsets.UTF_8.toString())
            val team = Gson().fromJson(teamjson, Team::class.java)
            val venue = Gson().fromJson(venuejson, Venue::class.java)
            TeamScreen(team = team,
                venue = venue,
                onBackClick = { navHostController.popBackStack()},
            )
        }

        composable("favoritos") {
            //val favoritesViewModel: FavoritesViewModel = hiltViewModel()
            FavoritosScreen(
                onTeamClick = onTeamNavigate,
                onBackClick = { navHostController.popBackStack() }
            )
        }

        composable("playerscreen/{playerId}/{season}") { backStackEntry ->
            val playerId = backStackEntry.arguments?.getString("playerId")
            val season = backStackEntry.arguments?.getString("season")
            PlayerScreen(playerId = playerId!!.toInt(), season = season!!.toInt())
        }

        composable("ligaDetalle/{leagueId}/{season}") { backStackEntry ->
            val leagueId = backStackEntry.arguments?.getString("leagueId")?.toIntOrNull() ?: return@composable
            val season = backStackEntry.arguments?.getString("season")?.toIntOrNull() ?: return@composable
            LigaDetalleScreen(
                leagueId = leagueId,
                season = season,
                navController = navHostController // para el botón "Atrás"
            )
        }
    }
}

