package com.example.shalenammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shalenammapride.ui.navigation.Screen
import com.example.shalenammapride.ui.navigation.bottomNavItems
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val viewModel: FirebaseViewModel = viewModel()
    val language by viewModel.language.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var showLoginDialog by remember { mutableStateOf(false) }
    var adminPassword by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentRoute != Screen.Splash.route) {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(if (language == AppLanguage.KANNADA) "ಶಾಲಾ - ನಮ್ಮ ಹೆಮ್ಮೆ" else "Shale - Namma Pride")
                    },
                    navigationIcon = {
                        IconButton(onClick = { 
                            if (isAdmin) viewModel.logout() else showLoginDialog = true 
                        }) {
                            Icon(
                                if (isAdmin) Icons.Default.LockOpen else Icons.Default.Lock, 
                                contentDescription = "Admin",
                                tint = if (isAdmin) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                    },
                    actions = {
                        TextButton(onClick = { viewModel.toggleLanguage() }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Language, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text(if (language == AppLanguage.KANNADA) "ENGLISH" else "ಕನ್ನಡ")
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentRoute != Screen.Splash.route) {
                NavigationBar {
                    val currentDestination = navBackStackEntry?.destination
                    bottomNavItems.forEach { screen ->
                        val label = when (screen) {
                            Screen.Home -> if (language == AppLanguage.KANNADA) "ಮುಖಪುಟ" else "Home"
                            Screen.Announcements -> if (language == AppLanguage.KANNADA) "ಸುದ್ದಿ" else "News"
                            Screen.Attendance -> if (language == AppLanguage.KANNADA) "ಹಾಜರಾತಿ" else "Attendance"
                            Screen.Events -> if (language == AppLanguage.KANNADA) "ಗ್ಯಾಲರಿ" else "Gallery"
                            Screen.Feedback -> if (language == AppLanguage.KANNADA) "ಅಭಿಪ್ರಾಯ" else "Feedback"
                            else -> screen.title
                        }
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(onSplashFinished = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    })
                }
                composable(Screen.Home.route) { HomeScreen(onNavigateToSection = { navController.navigate(it) }, viewModel = viewModel) }
                composable(Screen.Announcements.route) { AnnouncementsScreen(viewModel) }
                composable(Screen.Attendance.route) { AttendanceScreen(viewModel) }
                composable(Screen.Events.route) { EventsScreen(viewModel) }
                composable(Screen.Feedback.route) { FeedbackBoxScreen(viewModel) }
                composable(Screen.MealUpdate.route) { MealUpdateScreen(viewModel) }
                composable(Screen.StudentStars.route) { StudentStarsScreen(viewModel) }
                composable(Screen.SchoolInfo.route) { SchoolInfoScreen(viewModel) }
                composable(Screen.Timetable.route) { TimetableScreen(viewModel) }
                composable(Screen.FacilityTour.route) { FacilityTourScreen(viewModel) }
                composable(Screen.Reports.route) { ReportsScreen(viewModel) }
            }

            if (isUploading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(Modifier.height(16.dp))
                            Text(if (language == AppLanguage.KANNADA) "ಅಪ್‌ಲೋಡ್ ಆಗುತ್ತಿದೆ..." else "Uploading to Firebase...")
                        }
                    }
                }
            }
        }
    }

    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text(if (language == AppLanguage.KANNADA) "ನಿರ್ವಾಹಕ ಲಾಗಿನ್" else "Admin Login") },
            text = {
                Column {
                    OutlinedTextField(
                        value = adminPassword,
                        onValueChange = { adminPassword = it },
                        label = { Text("Password (admin123)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (loginError.isNotEmpty()) Text(loginError, color = Color.Red)
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.adminLogin(adminPassword, onSuccess = { showLoginDialog = false; adminPassword = ""; loginError = "" }, onError = { loginError = it })
                }) { Text("Login") }
            }
        )
    }
}
