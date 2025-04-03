package com.wikifut.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.google.firebase.auth.FirebaseAuth
import com.wikifut.app.presentation.Home.HomePartidosViewModel
import com.wikifut.app.presentation.Home.HomePartidosScreen
import com.wikifut.app.presentation.initial.InitialScreen
import com.wikifut.app.presentation.login.LoginScreen
import com.wikifut.app.presentation.signup.SignUpScreen
import com.wikifut.app.presentation.Teams.TeamsScreen
import com.wikifut.app.presentation.Teams.TeamsViewModel
import android.util.Log
import com.google.gson.Gson
import com.wikifut.app.model.Team
import com.wikifut.app.presentation.LigaDetalle.LigaDetalleScreen
import com.wikifut.app.presentation.Ligas.LigasScreen
import com.wikifut.app.presentation.Ligas.LigasViewModel


@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {
    val isUserLoggedIn = auth.currentUser != null
    //val startDestination = "Teams"
    val startDestination = if (isUserLoggedIn) "home" else "initial"

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
            val homePartidosViewModel: HomePartidosViewModel = hiltViewModel()
            HomePartidosScreen(
                viewModel = homePartidosViewModel,
                navigateToTeam = {teamJson -> navHostController.navigate("teamScreen/$teamJson")},
                navigateToInitial = { navHostController.navigate("initial") {
                    popUpTo("home") { inclusive = true }
                }


            })
        }
        composable("teamScreen/{teamJson}") { backStackEntry ->
            val json = backStackEntry.arguments?.getString("teamJson") ?: ""
            val team = Gson().fromJson(json, Team::class.java) // Deserializa el objeto

            TeamsScreen(team) {
                navHostController.popBackStack() // Función para volver atrás
            }
        }
        composable("ligas") {
            val ligasViewModel = hiltViewModel<LigasViewModel>()
            LigasScreen(viewModel = ligasViewModel, navController = navHostController)
        }
        composable("ligaDetalle/{leagueId}/{season}") { backStackEntry ->
            val leagueId = backStackEntry.arguments?.getString("leagueId")?.toIntOrNull() ?: return@composable
            val season = backStackEntry.arguments?.getString("season")?.toIntOrNull() ?: return@composable
            LigaDetalleScreen(
                leagueId = leagueId,
                season = season,
                navController = navHostController // 👈 importante para el botón "Atrás"
            )
        }

    }
}