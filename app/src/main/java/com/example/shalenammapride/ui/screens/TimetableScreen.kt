package com.example.shalenammapride.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(viewModel: FirebaseViewModel) {
    val language by viewModel.language.collectAsState()
    var selectedGrade by remember { mutableStateOf("Grade 5") }
    val grades = (1..10).map { "Grade $it" }
    var expanded by remember { mutableStateOf(false) }

    val timetableData = listOf(
        "Monday" to listOf("9:30 - 10:30" to ("Kannada" to "ಕನ್ನಡ"), "10:30 - 11:30" to ("Maths" to "ಗಣಿತ"), "11:30 - 12:30" to ("Science" to "ವಿಜ್ಞಾನ")),
        "Tuesday" to listOf("9:30 - 10:30" to ("English" to "ಇಂಗ್ಲಿಷ್"), "10:30 - 11:30" to ("Social" to "ಸಮಾಜ"), "11:30 - 12:30" to ("Hindi" to "ಹಿಂದಿ")),
        "Wednesday" to listOf("9:30 - 10:30" to ("Maths" to "ಗಣಿತ"), "10:30 - 11:30" to ("Kannada" to "ಕನ್ನಡ"), "11:30 - 12:30" to ("EVS" to "ಪರಿಸರ ಅಧ್ಯಯನ")),
        "Thursday" to listOf("9:30 - 10:30" to ("Science" to "ವಿಜ್ಞಾನ"), "10:30 - 11:30" to ("English" to "ಇಂಗ್ಲಿಷ್"), "11:30 - 12:30" to ("Maths" to "ಗಣಿತ")),
        "Friday" to listOf("9:30 - 10:30" to ("Social" to "ಸಮಾಜ"), "10:30 - 11:30" to ("Kannada" to "ಕನ್ನಡ"), "11:30 - 12:30" to ("Science" to "ವಿಜ್ಞಾನ")),
        "Saturday" to listOf("9:30 - 10:30" to ("PT" to "ದೈಹಿಕ ಶಿಕ್ಷಣ"), "10:30 - 11:30" to ("Art" to "ಚಿತ್ರಕಲೆ"), "11:30 - 12:30" to ("Library" to "ಗ್ರಂಥಾಲಯ"))
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (language == AppLanguage.KANNADA) "ತರಗತಿ ವೇಳಾಪಟ್ಟಿ" else "Class Timetable",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grade Selector
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedGrade,
                onValueChange = {},
                readOnly = true,
                label = { Text(if (language == AppLanguage.KANNADA) "ತರಗತಿಯನ್ನು ಆರಿಸಿ" else "Select Grade") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                grades.forEach { grade ->
                    DropdownMenuItem(
                        text = { Text(grade) },
                        onClick = {
                            selectedGrade = grade
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(timetableData) { (day, periods) ->
                TimetableDayCard(day, periods, language)
            }
        }
    }
}

@Composable
fun TimetableDayCard(dayEn: String, periods: List<Pair<String, Pair<String, String>>>, language: AppLanguage) {
    val dayKn = when (dayEn) {
        "Monday" -> "ಸೋಮವಾರ"
        "Tuesday" -> "ಮಂಗಳವಾರ"
        "Wednesday" -> "ಬುಧವಾರ"
        "Thursday" -> "ಗುರುವಾರ"
        "Friday" -> "ಶುಕ್ರವಾರ"
        "Saturday" -> "ಶನಿವಾರ"
        else -> dayEn
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.KANNADA) dayKn else dayEn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            periods.forEach { (time, subjects) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(time, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Text(
                        if (language == AppLanguage.KANNADA) subjects.second else subjects.first,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
