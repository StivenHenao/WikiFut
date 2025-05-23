package com.wikifut.app

import androidx.compose.runtime.Composable
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
import com.wikifut.app.presentation.signup.SignUpScreen
import com.wikifut.app.presentation.editprofile.EditProfileScreen
import com.wikifut.app.presentation.Home.HomeScreenWithDrawer
import com.wikifut.app.model.TipoBusqueda
import com.wikifut.app.model.Venue
import com.wikifut.app.presentation.Search.SearchScreen
import com.wikifut.app.presentation.Team.TeamScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {
    val isUserLoggedIn = auth.currentUser != null
    val startDestination = if (isUserLoggedIn) "home" else "initial"

    // Función que maneja la navegación a búsqueda
    val onSearchNavigate: (TipoBusqueda, String) -> Unit = { tipo, query ->
        when(tipo){
            TipoBusqueda.Equipos -> navHostController.navigate("busqueda/Equipos/$query")
            TipoBusqueda.Ligas -> navHostController.navigate("busqueda/Ligas/$query")
            TipoBusqueda.Partidos -> navHostController.navigate("busqueda/Partidos/$query")
            TipoBusqueda.Jugadores -> navHostController.navigate("busqueda/jugadores/$query")
        }
    }
    val onTeamNavigate: (Team, Venue) -> Unit = { team, venue ->
        val teamJson = URLEncoder.encode(Gson().toJson(team), StandardCharsets.UTF_8.toString())
        val venueJson = URLEncoder.encode(Gson().toJson(venue), StandardCharsets.UTF_8.toString())
        navHostController.navigate("teamScreen/$teamJson/$venueJson")
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
            HomeScreenWithDrawer(
                navigateToEditProfile = { navHostController.navigate("edit_profile") },
                navigateToInitial = {
                    navHostController.navigate("initial") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onSearchNavigate = onSearchNavigate
            )
        }

        // Nueva ruta ↓↓↓
        composable("edit_profile") {
            EditProfileScreen(
                onBack = { navHostController.popBackStack() }
            )
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
                "jugadores" -> TipoBusqueda.Jugadores
                else -> TipoBusqueda.Equipos
            }

            SearchScreen(
                tipo = tipo,
                query = query,
                onSearchNavigate = onSearchNavigate,
                HomeNavigate = {
                    navHostController.popBackStack("home", inclusive = false)
                },
                onTeamNavigate = onTeamNavigate)
        }


        composable("teamScreen/{teamJson}/{venueJson}") { backStackEntry ->
            val teamjson = URLDecoder.decode(backStackEntry.arguments?.getString("teamJson") ?: "", StandardCharsets.UTF_8.toString())
            val venuejson = URLDecoder.decode(backStackEntry.arguments?.getString("venueJson") ?: "", StandardCharsets.UTF_8.toString())
            val team = Gson().fromJson(teamjson, Team::class.java)
            val venue = Gson().fromJson(venuejson, Venue::class.java)
            TeamScreen(team = team, venue = venue, onBackClick = { navHostController.popBackStack()})
        }
    }
}

