package com.example.shalenammapride.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shalenammapride.data.model.Report
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

@Composable
fun ReportsScreen(viewModel: FirebaseViewModel) {
    val language by viewModel.language.collectAsState()
    val reports by viewModel.reports.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var titleEn by remember { mutableStateOf("") }
    var titleKn by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (language == AppLanguage.KANNADA) "ಶಾಲಾ ವರದಿಗಳು" else "School Reports",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            
            if (isAdmin) {
                IconButton(
                    onClick = { showAddDialog = true },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Report")
                }
            }
        }
        
        Text(
            if (language == AppLanguage.KANNADA) "ಪಾರದರ್ಶಕತೆಗಾಗಿ ಶಾಲಾ ದಾಖಲೆಗಳು" else "School records for transparency",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (reports.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(if (language == AppLanguage.KANNADA) "ಯಾವುದೇ ವರದಿಗಳಿಲ್ಲ" else "No reports available", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(reports) { report ->
                    ReportCard(report, language)
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(if (language == AppLanguage.KANNADA) "ಹೊಸ ವರದಿ" else "Add New Report") },
            text = {
                Column {
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
                    Spacer(Modifier.height(16.dp))
                    
                    Button(
                        onClick = { launcher.launch("application/pdf") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (selectedFileUri != null) "File Selected" else "Select PDF File")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (titleEn.isNotBlank() && titleKn.isNotBlank()) {
                        viewModel.addReport(titleEn, titleKn, selectedFileUri)
                        showAddDialog = false
                        titleEn = ""
                        titleKn = ""
                        selectedFileUri = null
                    }
                }) {
                    Text(if (language == AppLanguage.KANNADA) "ಸಲ್ಲಿಸಿ" else "Submit")
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
fun ReportCard(report: Report, language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Description, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
            
            Spacer(Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    if (language == AppLanguage.KANNADA) report.titleKn else report.titleEn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("${report.format} • ${report.size}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            
            if (report.fileUrl.isNotEmpty()) {
                IconButton(onClick = { /* Open URL */ }) {
                    Icon(Icons.Default.Download, contentDescription = "Download", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
