package com.example.shalenammapride.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object MealUpdate : Screen("meal", "Meals", Icons.Default.Restaurant)
    object FacilityTour : Screen("facility", "Facilities", Icons.Default.Business)
    object StudentStars : Screen("stars", "Stars", Icons.Default.Star)
    object Feedback : Screen("feedback", "Feedback", Icons.Default.Feedback)
    object Timetable : Screen("timetable", "Timetable", Icons.Default.CalendarMonth)
    object Announcements : Screen("announcements", "News", Icons.Default.Notifications)
    object SchoolInfo : Screen("info", "About", Icons.Default.Info)
    object Attendance : Screen("attendance", "Attendance", Icons.Default.CheckCircle)
    object Events : Screen("events", "Events", Icons.Default.PhotoLibrary)
    object Splash : Screen("splash", "Splash", Icons.Default.School)
    object Reports : Screen("reports", "Reports", Icons.Default.Description)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Announcements,
    Screen.Attendance,
    Screen.Events,
    Screen.Feedback
)
