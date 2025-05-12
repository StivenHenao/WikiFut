package com.wikifut.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.google.firebase.auth.FirebaseAuth
import com.wikifut.app.presentation.initial.InitialScreen
import com.wikifut.app.presentation.login.LoginScreen
import com.wikifut.app.presentation.signup.SignUpScreen
import com.wikifut.app.presentation.editprofile.EditProfileScreen
import com.wikifut.app.presentation.Home.HomeScreenWithDrawer
import com.wikifut.app.presentation.LigaDetalle.LigaDetalleScreen

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
            HomeScreenWithDrawer(
                navigateToEditProfile = { navHostController.navigate("edit_profile") },
                navigateToInitial = {
                    navHostController.navigate("initial") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // Nueva ruta ↓↓↓
        composable("edit_profile") {
            EditProfileScreen(
                onBack = { navHostController.popBackStack() }
            )
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

