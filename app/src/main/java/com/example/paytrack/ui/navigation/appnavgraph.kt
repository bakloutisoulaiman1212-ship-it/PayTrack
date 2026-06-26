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
import com.example.paytrack.ui.screens.NotificationsScreen
import com.example.paytrack.ui.viewmodel.UserViewModel
import com.example.paytrack.ui.screens.ProfileScreen
import com.example.paytrack.ui.screens.transaction.HistoryScreen
import com.example.paytrack.ui.screens.transaction.PaymentScreen
import com.example.paytrack.ui.screens.transaction.TransferScreen
import com.example.paytrack.ui.viewmodel.BudgetViewModel
import com.example.paytrack.ui.viewmodel.NotificationViewModel
import com.example.paytrack.ui.viewmodel.TransactionViewModel
@Composable
fun AppNavGraph(
    accountViewModel: AccountViewModel,
    userViewModel: UserViewModel,
    budgetViewModel: BudgetViewModel,
    transactionViewModel: TransactionViewModel ,
    notificationViewModel: NotificationViewModel ,
    darkMode: Boolean,
    onToggleDarkMode: (Boolean) -> Unit

) {

    val navController = rememberNavController()

    val startDestination = "splash"
    NavHost(
        navController = navController,
        startDestination = startDestination,
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

        composable("login") {
            LoginScreen(navController, userViewModel)
        }

        composable("signup") {
            SignUpScreen(navController, userViewModel)

        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                userViewModel = userViewModel )
        }
        composable("notifications") {
            NotificationsScreen(notificationViewModel, navController)
        }

        composable("home/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            HomeScreen(navController, username ,notificationViewModel ,
                darkMode = darkMode,
                onToggleDarkMode = onToggleDarkMode
            )
        }
        // ✅ LIST
        composable("list") {
            AccountListScreen(
                accountViewModel,
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
            AddAccountScreen(accountViewModel, navController)
        }

        // ✅ EDIT
        composable("edit/{accountId}") { backStackEntry ->

            val id = backStackEntry.arguments?.getString("accountId")?.toLong()

            if (id != null) {
                EditAccountScreen(accountViewModel, id, navController)
            }
        }
        composable("dashboard") {
            DashboardScreen(accountViewModel, transactionViewModel ,budgetViewModel, navController)
        }

        composable("transfer") {
            TransferScreen(accountViewModel , navController )
        }

        composable("payment") {
            PaymentScreen(accountViewModel , navController )
        }

        composable("history") {
            HistoryScreen(transactionViewModel , accountViewModel ,navController)
        }

    }
}