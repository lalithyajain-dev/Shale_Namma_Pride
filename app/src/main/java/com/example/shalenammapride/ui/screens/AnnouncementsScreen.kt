package com.example.shalenammapride.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shalenammapride.data.model.Announcement
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnnouncementsScreen(viewModel: FirebaseViewModel) {
    val announcements by viewModel.announcements.collectAsState()
    val language by viewModel.language.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    var showAddDialog by remember { mutableStateOf(false) }
    var titleEn by remember { mutableStateOf("") }
    var titleKn by remember { mutableStateOf("") }
    var contentEn by remember { mutableStateOf("") }
    var contentKn by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("General") }

    // Demo data to show if list is empty
    val demoAnnouncements = listOf(
        Announcement(
            id = "demo1",
            titleEn = "Welcome to our School!",
            titleKn = "ನಮ್ಮ ಶಾಲೆಗೆ ಸ್ವಾಗತ!",
            contentEn = "Stay tuned for daily updates and school activities.",
            contentKn = "ದೈನಂದಿನ ಅಪ್‌ಡೇಟ್‌ಗಳು ಮತ್ತು ಶಾಲಾ ಚಟುವಟಿಕೆಗಳಿಗಾಗಿ ಇಲ್ಲೇ ಇರಿ.",
            type = "General",
            timestamp = System.currentTimeMillis()
        ),
        Announcement(
            id = "demo2",
            titleEn = "PTA Meeting Next Saturday",
            titleKn = "ಮುಂದಿನ ಶನಿವಾರ ಪೋಷಕ ಶಿಕ್ಷಕರ ಸಭೆ",
            contentEn = "All parents are requested to attend the meeting at 10:00 AM.",
            contentKn = "ಎಲ್ಲಾ ಪೋಷಕರು ಬೆಳಿಗ್ಗೆ 10:00 ಗಂಟೆಗೆ ಸಭೆಗೆ ಹಾಜರಾಗಲು ವಿನಂತಿಸಲಾಗಿದೆ.",
            type = "Meeting",
            timestamp = System.currentTimeMillis() - 86400000
        )
    )

    val displayList = if (announcements.isEmpty()) demoAnnouncements else announcements

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (language == AppLanguage.KANNADA) "ಶಾಲಾ ಪ್ರಕಟಣೆಗಳು" else "Notice Board",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            
            if (isAdmin) {
                IconButton(
                    onClick = { showAddDialog = true },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Announcement")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(displayList) { ann ->
                AnnouncementCard(ann, language, sdf.format(Date(ann.timestamp)))
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(if (language == AppLanguage.KANNADA) "ಹೊಸ ಪ್ರಕಟಣೆ" else "Add Announcement") },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = titleKn,
                        onValueChange = { titleKn = it },
                        label = { Text("Title (Kannada)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = titleEn,
                        onValueChange = { titleEn = it },
                        label = { Text("Title (English)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = contentKn,
                        onValueChange = { contentKn = it },
                        label = { Text("Content (Kannada)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = contentEn,
                        onValueChange = { contentEn = it },
                        label = { Text("Content (English)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Type / ವಿಧ:")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(selected = type == "General", onClick = { type = "General" })
                            Text("General", style = MaterialTheme.typography.labelSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(selected = type == "Holiday", onClick = { type = "Holiday" })
                            Text("Holiday", style = MaterialTheme.typography.labelSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(selected = type == "Exam", onClick = { type = "Exam" })
                            Text("Exam", style = MaterialTheme.typography.labelSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(selected = type == "Urgent", onClick = { type = "Urgent" })
                            Text("Urgent", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (titleEn.isNotBlank() || titleKn.isNotBlank()) {
                        viewModel.addAnnouncement(titleEn, titleKn, contentEn, contentKn, type)
                        showAddDialog = false
                        titleEn = ""
                        titleKn = ""
                        contentEn = ""
                        contentKn = ""
                    }
                }) {
                    Text(if (language == AppLanguage.KANNADA) "ಸಲ್ಲಿಸಿ" else "Post")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(if (language == AppLanguage.KANNADA) "ರದ್ದು" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun AnnouncementCard(ann: Announcement, language: AppLanguage, date: String) {
    val (bgColor, contentColor, icon) = when (ann.type) {
        "Urgent" -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), Icons.Default.Warning)
        "Holiday" -> Triple(Color(0xFFE8F5E9), Color(0xFF2E7D32), Icons.Default.Event)
        "Exam" -> Triple(Color(0xFFE3F2FD), Color(0xFF1565C0), Icons.Default.Assignment)
        "Meeting" -> Triple(Color(0xFFFFF3E0), Color(0xFFE65100), Icons.Default.Groups)
        else -> Triple(Color(0xFFF5F5F5), Color.Black, Icons.Default.Notifications)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = bgColor,
                    shape = CircleShape,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = contentColor)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.KANNADA) 
                            (if (ann.titleKn.isNotBlank()) ann.titleKn else ann.titleEn) 
                            else (if (ann.titleEn.isNotBlank()) ann.titleEn else ann.titleKn),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
                
                if (ann.type == "Urgent") {
                    Surface(
                        color = Color.Red,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.KANNADA) "ತುರ್ತು" else "URGENT",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = if (language == AppLanguage.KANNADA) 
                    (if (ann.contentKn.isNotBlank()) ann.contentKn else ann.contentEn) 
                    else (if (ann.contentEn.isNotBlank()) ann.contentEn else ann.contentKn),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
    }
}
