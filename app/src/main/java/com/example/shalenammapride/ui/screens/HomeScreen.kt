package com.example.shalenammapride.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shalenammapride.data.model.Teacher
import com.example.shalenammapride.ui.navigation.Screen
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel
import java.util.*

@Composable
fun HomeScreen(
    onNavigateToSection: (String) -> Unit,
    viewModel: FirebaseViewModel = viewModel()
) {
    val todayMeal by viewModel.todayMeal.collectAsState()
    val language by viewModel.language.collectAsState()

    val teachers = listOf(
        Teacher("Dr. Manjunath B.S.", "Head Master", "ಮುಖ್ಯೋಪಾಧ್ಯಾಯರು", "Kannada, Social Science", "ಕನ್ನಡ, ಸಮಾಜ ವಿಜ್ಞಾನ"),
        Teacher("Mrs. Suma R.", "Asst. Teacher", "ಸಹ ಶಿಕ್ಷಕರು", "English, Maths", "ಇಂಗ್ಲಿಷ್, ಗಣಿತ"),
        Teacher("Mr. Ravi Kumar", "Asst. Teacher", "ಸಹ ಶಿಕ್ಷಕರು", "Science, EVS", "ವಿಜ್ಞಾನ, ಪರಿಸರ ಅಧ್ಯಯನ"),
        Teacher("Mrs. Lakshmi N.", "Asst. Teacher", "ಸಹ ಶಿಕ್ಷಕರು", "Hindi, Art", "ಹಿಂದಿ, ಚಿತ್ರಕಲೆ")
    )

    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> if (language == AppLanguage.KANNADA) "ಶುಭೋದಯ" else "Good Morning"
        in 12..16 -> if (language == AppLanguage.KANNADA) "ಶುಭ ಮಧ್ಯಾಹ್ನ" else "Good Afternoon"
        else -> if (language == AppLanguage.KANNADA) "ಶುಭ ಸಂಜೆ" else "Good Evening"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {
        // School Banner / Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                    ),
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = greeting,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (language == AppLanguage.KANNADA) "ಸರ್ಕಾರಿ ಹಿರಿಯ ಪ್ರಾಥಮಿಕ ಶಾಲೆ" else "Govt Higher Primary School",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (language == AppLanguage.KANNADA) "ನಮ್ಮ ಹೆಮ್ಮೆ - ನಮ್ಮ ಶಾಲೆ" else "Namma Pride - Our School",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Quote of the Day
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FormatQuote, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.KANNADA) 
                            "\"ಶಿಕ್ಷಣವೇ ಜೀವನದ ಬೆಳಕು.\"" 
                            else "\"Education is the light of life.\"",
                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Stats
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                HomeStatCard(
                    title = if (language == AppLanguage.KANNADA) "ಹಾಜರಾತಿ" else "Attendance",
                    value = "94%",
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFFE8F5E9),
                    modifier = Modifier.weight(1f)
                )
                HomeStatCard(
                    title = if (language == AppLanguage.KANNADA) "ಶಿಕ್ಷಕರು" else "Teachers",
                    value = "12/12",
                    icon = Icons.Default.Group,
                    color = Color(0xFFE3F2FD),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Today's Meal
            Text(
                text = if (language == AppLanguage.KANNADA) "ಇಂದಿನ ವಿಶೇಷ ಅಡುಗೆ" else "Today's Meal Special",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSection(Screen.MealUpdate.route) },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFF176)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color(0xFFF57F17), modifier = Modifier.size(32.dp))
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        if (todayMeal != null) {
                            Text(
                                text = if (language == AppLanguage.KANNADA) todayMeal!!.menuKn else todayMeal!!.menuEn,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = if (language == AppLanguage.KANNADA) "ಮೆನು ಶೀಘ್ರದಲ್ಲೇ ಬರಲಿದೆ" else "Menu Coming Soon",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = if (language == AppLanguage.KANNADA) "ಸಮಯ: ಮಧ್ಯಾಹ್ನ 1:00" else "Time: 1:00 PM",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Our Teachers
            Text(
                text = if (language == AppLanguage.KANNADA) "ನಮ್ಮ ಶಿಕ್ಷಕರು" else "Our Teachers",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(teachers) { teacher ->
                    TeacherCard(teacher, language)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Links
            Text(
                text = if (language == AppLanguage.KANNADA) "ತ್ವರಿತ ಲಿಂಕ್‌ಗಳು" else "Quick Links",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ShortcutButton(
                    if (language == AppLanguage.KANNADA) "ಸಾಧಕರು" else "Stars", 
                    Icons.Default.AutoAwesome, 
                    Color(0xFFFCE4EC), 
                    Modifier.weight(1f)
                ) { onNavigateToSection(Screen.StudentStars.route) }
                
                ShortcutButton(
                    if (language == AppLanguage.KANNADA) "ಮಾಹಿತಿ" else "Info", 
                    Icons.Default.Info, 
                    Color(0xFFF3E5F5), 
                    Modifier.weight(1f)
                ) { onNavigateToSection(Screen.SchoolInfo.route) }
                
                ShortcutButton(
                    if (language == AppLanguage.KANNADA) "ವರದಿಗಳು" else "Reports", 
                    Icons.Default.Description, 
                    Color(0xFFE0F2F1), 
                    Modifier.weight(1f)
                ) { onNavigateToSection(Screen.Reports.route) }
            }
        }
    }
}

@Composable
fun TeacherCard(teacher: Teacher, language: AppLanguage) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(text = teacher.name, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(
                text = if (language == AppLanguage.KANNADA) teacher.designationKn else teacher.designationEn,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = if (language == AppLanguage.KANNADA) teacher.subjectKn else teacher.subjectEn,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun HomeStatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = Color.DarkGray)
        }
    }
}

@Composable
fun ShortcutButton(title: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(4.dp))
            Text(text = title, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
        }
    }
}
