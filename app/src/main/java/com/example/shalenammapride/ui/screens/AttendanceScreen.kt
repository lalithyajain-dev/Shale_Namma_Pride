package com.example.shalenammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shalenammapride.data.model.StudentAttendance
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

@Composable
fun AttendanceScreen(viewModel: FirebaseViewModel) {
    val language by viewModel.language.collectAsState()
    val studentAttendance by viewModel.studentAttendance.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var newStudentName by remember { mutableStateOf("") }
    var newGrade by remember { mutableStateOf("Grade 5") }
    var newStatus by remember { mutableStateOf("Present") }

    // Calculate dynamic percentage
    val totalStudents = studentAttendance.size
    val presentCount = studentAttendance.count { it.status == "Present" }
    val attendancePercentage = if (totalStudents > 0) (presentCount.toFloat() / totalStudents * 100).toInt() else 0

    val displayList = if (studentAttendance.isEmpty()) {
        listOf(
            StudentAttendance(id = "1", studentName = "Arun Kumar", grade = "Grade 5", status = "Present", date = "2024-03-20"),
            StudentAttendance(id = "2", studentName = "Deepa M.", grade = "Grade 5", status = "Absent", date = "2024-03-20")
        )
    } else {
        studentAttendance
    }

    val filteredList = displayList.filter {
        it.studentName.contains(searchQuery, ignoreCase = true) || 
        it.grade.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (language == AppLanguage.KANNADA) "ಹಾಜರಾತಿ ವರದಿ" else "Attendance Report",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            if (language == AppLanguage.KANNADA) "ಒಟ್ಟು ಹಾಜರಾತಿ" else "Total Attendance",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            if (studentAttendance.isEmpty()) "92%" else "$attendancePercentage%",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.TrendingUp,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(if (language == AppLanguage.KANNADA) "ವಿದ್ಯಾರ್ಥಿ ಹುಡುಕಿ" else "Search Student") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredList) { attendance ->
                    StudentAttendanceCard(attendance, language)
                }
            }
        }

        // Admin FAB
        if (isAdmin) {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Attendance")
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(if (language == AppLanguage.KANNADA) "ಹಾಜರಾತಿ ಸೇರಿಸಿ" else "Add Attendance") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newStudentName,
                        onValueChange = { newStudentName = it },
                        label = { Text("Student Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newGrade,
                        onValueChange = { newGrade = it },
                        label = { Text("Grade (e.g. Grade 5)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = newStatus == "Present", onClick = { newStatus = "Present" })
                        Text("Present")
                        Spacer(Modifier.width(16.dp))
                        RadioButton(selected = newStatus == "Absent", onClick = { newStatus = "Absent" })
                        Text("Absent")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newStudentName.isNotBlank()) {
                        viewModel.addAttendance(newStudentName, newGrade, newStatus)
                        showAddDialog = false
                        newStudentName = ""
                    }
                }) {
                    Text("Add")
                }
            }
        )
    }
}

@Composable
fun StudentAttendanceCard(attendance: StudentAttendance, language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(attendance.studentName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(attendance.grade, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            
            Surface(
                color = if (attendance.status == "Present") Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (attendance.status == "Present") 
                        (if (language == AppLanguage.KANNADA) "ಹಾಜರು" else "Present")
                    else 
                        (if (language == AppLanguage.KANNADA) "ಗೈರು" else "Absent"),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (attendance.status == "Present") Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
