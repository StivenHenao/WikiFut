package com.wikifut.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.wikifut.app.presentation.initial.InitialScreen
import com.wikifut.app.presentation.login.LoginScreen
import com.wikifut.app.presentation.signup.SignUpScreen
import com.wikifut.app.presentation.editprofile.EditProfileScreen
import com.wikifut.app.presentation.Home.HomeScreenWithDrawer
import com.wikifut.app.model.TipoBusqueda
import com.wikifut.app.presentation.Search.SearchScreen

@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {
    val isUserLoggedIn = auth.currentUser != null
    val startDestination = if (isUserLoggedIn) "home" else "initial"

    // Función que maneja la navegación a búsqueda
    val onSearchNavigate: (TipoBusqueda, String) -> Unit = { tipo, query ->
        navHostController.navigate("busqueda/${tipo}/$query")
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
                else -> TipoBusqueda.Equipos
            }

            SearchScreen(tipo = tipo, query = query)
        }
    }
}

