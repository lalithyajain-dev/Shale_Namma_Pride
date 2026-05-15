package com.example.shalenammapride.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shalenammapride.data.model.GalleryImage
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

@Composable
fun EventsScreen(viewModel: FirebaseViewModel) {
    val language by viewModel.language.collectAsState()
    val gallery by viewModel.gallery.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var descEn by remember { mutableStateOf("") }
    var descKn by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (language == AppLanguage.KANNADA) "ಶಾಲಾ ಗ್ಯಾಲರಿ" else "School Gallery",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            
            if (isAdmin) {
                IconButton(
                    onClick = { showAddDialog = true },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Photo")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (gallery.isEmpty()) {
            // Demo data if empty
            val demoList = listOf(
                GalleryImage(id="1", descriptionEn = "Independence Day", descriptionKn = "ಸ್ವಾತಂತ್ರ್ಯ ದಿನಾಚರಣೆ"),
                GalleryImage(id="2", descriptionEn = "Sports Day", descriptionKn = "ಕ್ರೀಡಾ ದಿನ")
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(demoList) { item ->
                    GalleryCard(item, language)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(gallery) { item ->
                    GalleryCard(item, language)
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(if (language == AppLanguage.KANNADA) "ಹೊಸ ಚಿತ್ರ ಸೇರಿಸಿ" else "Add New Photo") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { launcher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null)
                        }
                    }
                    Text(if (language == AppLanguage.KANNADA) "ಫೋಟೋ ಆರಿಸಿ" else "Select Photo", style = MaterialTheme.typography.labelSmall)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = descKn,
                        onValueChange = { descKn = it },
                        label = { Text("Description (Kannada)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = descEn,
                        onValueChange = { descEn = it },
                        label = { Text("Description (English)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (selectedImageUri != null) {
                        viewModel.addGalleryImage(descEn, descKn, selectedImageUri!!)
                        showAddDialog = false
                        descEn = ""
                        descKn = ""
                        selectedImageUri = null
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
fun GalleryCard(item: GalleryImage, language: AppLanguage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                if (item.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray.copy(alpha = 0.3f))
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == AppLanguage.KANNADA) item.descriptionKn else item.descriptionEn,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
