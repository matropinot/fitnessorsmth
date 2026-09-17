package com.homefit.app.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private sealed class Destination(val route: String, val label: String) {
    data object Home : Destination("home", "Home")
    data object Workouts : Destination("workouts", "Workouts")
    data object Exercises : Destination("exercises", "Exercises")
    data object Progress : Destination("progress", "Progress")
    data object Settings : Destination("settings", "Settings")
}

private val destinations = listOf(
    Destination.Home,
    Destination.Workouts,
    Destination.Exercises,
    Destination.Progress,
    Destination.Settings,
)

@Composable
fun HomeFitApp(viewModel: HomeFitViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val currentRoute = currentDestination?.route ?: Destination.Home.route

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val large = maxWidth >= 700.dp
        Scaffold(
            bottomBar = {
                if (!large && !currentRoute.startsWith("player")) {
                    NavigationBar(modifier = Modifier.navigationBarsPadding()) {
                        destinations.forEach { destination ->
                            NavigationBarItem(
                                selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                                onClick = {
                                    navController.navigate(destination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Text(destination.label.take(1)) },
                                label = { Text(destination.label) },
                            )
                        }
                    }
                }
            },
        ) { padding ->
            androidx.compose.foundation.layout.Row(modifier = Modifier.fillMaxSize()) {
                if (large && !currentRoute.startsWith("player")) {
                    NavigationRail(modifier = Modifier.width(88.dp).padding(padding)) {
                        destinations.forEach { destination ->
                            NavigationRailItem(
                                selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                                onClick = {
                                    navController.navigate(destination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Text(destination.label.take(1)) },
                                label = { Text(destination.label) },
                            )
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = Destination.Home.route,
                    modifier = Modifier.fillMaxSize().padding(if (large && !currentRoute.startsWith("player")) padding else androidx.compose.ui.unit.PaddingValues()),
                ) {
                    composable(Destination.Home.route) {
                        HomeScreen(
                            viewModel = viewModel,
                            onStartWorkout = { workout ->
                                navController.navigate("player/${workout.id}")
                            },
                        )
                    }
                    composable(Destination.Workouts.route) {
                        WorkoutsScreen(onStartWorkout = { workout ->
                            navController.navigate("player/${workout.id}")
                        })
                    }
                    composable(Destination.Exercises.route) {
                        ExercisesScreen()
                    }
                    composable(Destination.Progress.route) {
                        ProgressScreen(viewModel.stats)
                    }
                    composable(Destination.Settings.route) {
                        SettingsScreen()
                    }
                    composable("player/{workoutId}") { entry ->
                        val workout = WorkoutScreenCatalog.find(entry.arguments?.getString("workoutId"))
                            ?: viewModel.recommended
                        WorkoutPlayerScreen(
                            workout = workout,
                            onFinished = {
                                viewModel.markWorkoutComplete(workout)
                                navController.popBackStack()
                            },
                            onClose = { navController.popBackStack() },
                        )
                    }
                }
            }
        }
    }
}
