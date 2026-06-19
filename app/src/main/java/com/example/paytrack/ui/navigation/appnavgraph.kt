package com.example.paytrack.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.paytrack.ui.screens.LoginScreen
import com.example.paytrack.ui.screens.SignUpScreen
import com.example.paytrack.ui.screens.SplashScreen
import com.example.paytrack.ui.screens.HomeScreen
import com.example.paytrack.ui.screens.account.AccountListScreen
import com.example.paytrack.ui.screens.account.AddAccountScreen
import com.example.paytrack.ui.screens.account.EditAccountScreen
import com.example.paytrack.ui.screens.dashboard.DashboardScreen
import com.example.paytrack.ui.viewmodel.AccountViewModel
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

@Composable
fun AppNavGraph(viewModel: AccountViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash",
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            )
        }
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") {
            SignUpScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        // ✅ LIST
        composable("list") {
            AccountListScreen(
                viewModel,
                onAddClick = { navController.navigate("add") },
                onAccountClick = { account ->
                    navController.navigate("edit/${account.id}")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // ✅ ADD
        composable(route = "add") {
            AddAccountScreen(viewModel, navController)
        }

        // ✅ EDIT
        composable("edit/{accountId}") { backStackEntry ->

            val id = backStackEntry.arguments?.getString("accountId")?.toLong()

            if (id != null) {
                EditAccountScreen(viewModel, id, navController)
            }
        }
        composable("dashboard") {
            DashboardScreen(viewModel, navController)
        }
    }
}