package com.wikifut.app

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.wikifut.app.presentation.Home.HomePartidosViewModel
import com.wikifut.app.presentation.Home.HomePartidosScreen
import com.wikifut.app.presentation.initial.InitialScreen
import com.wikifut.app.presentation.login.LoginScreen
import com.wikifut.app.presentation.match.MatchScreen
import com.wikifut.app.presentation.match.MatchViewModel
import com.wikifut.app.presentation.signup.SignUpScreen


@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {
    val isUserLoggedIn = auth.currentUser != null
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
            HomePartidosScreen(viewModel = homePartidosViewModel, navigateToInitial = {
                navHostController.navigate("initial") {
                    popUpTo("home") { inclusive = true }
                }
            },
                navigateToMatchDetail = { matchId ->
                    navHostController.navigate("matchDetail/$matchId")
                }

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
    }
}