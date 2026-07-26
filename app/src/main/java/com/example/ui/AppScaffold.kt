package com.example.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.PelangganScreen
import com.example.ui.screens.ServisScreen
import com.example.ui.screens.StokScreen
import com.example.ui.screens.ProfilScreen
import com.example.ui.screens.ToolkitScreen
import com.example.ui.theme.*

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object Servis : Screen("servis", "Servis", Icons.Default.Build)
    object Toolkit : Screen("toolkit", "Toolkit", Icons.Default.Handyman)
    object Stok : Screen("stok", "Stok", Icons.Default.Inventory)
    object Profil : Screen("profil", "Profil", Icons.Default.Person)
}

val items = listOf(
    Screen.Dashboard,
    Screen.Servis,
    Screen.Toolkit,
    Screen.Stok,
    Screen.Profil
)

@Composable
fun AppScaffold(viewModel: MainViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = White,
                contentColor = BlackPrimary,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title, fontWeight = FontWeight.Bold) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BlackPrimary,
                            selectedTextColor = BlackPrimary,
                            indicatorColor = Silver,
                            unselectedIconColor = GrayText,
                            unselectedTextColor = GrayText
                        ),
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Dashboard.route, Modifier.padding(innerPadding)) {
            composable(Screen.Dashboard.route) { DashboardScreen(viewModel, navController) }
            composable(Screen.Servis.route) { ServisScreen(viewModel) }
            composable(Screen.Toolkit.route) { ToolkitScreen() }
            composable(Screen.Stok.route) { StokScreen() }
            composable(Screen.Profil.route) { ProfilScreen() }
        }
    }
}
