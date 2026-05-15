package com.example.shalenammapride.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

@Composable
fun StudentStarsScreen(viewModel: FirebaseViewModel) {
    val stars by viewModel.stars.collectAsState()
    val language by viewModel.language.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()
    
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var achievement by remember { mutableStateOf("") }
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
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD600))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (language == AppLanguage.KANNADA) "ಶಾಲಾ ನಕ್ಷತ್ರಗಳು" else "Student Stars",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            
            // Admin Add Button - Only visible to Admin
            if (isAdmin) {
                IconButton(
                    onClick = { showDialog = true },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Star")
                }
            }
        }
        
        Text(
            if (language == AppLanguage.KANNADA) "ನಮ್ಮ ಪುಟ್ಟ ಸಾಧಕರನ್ನು ಸಂಭ್ರಮಿಸೋಣ!" else "Celebrating our little achievers!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        if (stars.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (language == AppLanguage.KANNADA) "ಇನ್ನೂ ಯಾವುದೇ ಸಾಧನೆಗಳನ್ನು ಅಪ್‌ಲೋಡ್ ಮಾಡಿಲ್ಲ." else "No stars posted yet. Stay tuned!", 
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stars) { star ->
                    StarCelebrationCard(
                        name = star.name, 
                        achievement = star.achievement,
                        imageUrl = star.imageUrl,
                        isAdmin = isAdmin,
                        onDelete = { viewModel.deleteStudentStar(star.id) }
                    )
                }
            }
        }
    }

    // Add Star Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (language == AppLanguage.KANNADA) "ಹೊಸ ಸಾಧಕರನ್ನು ಸೇರಿಸಿ" else "Add New Achiever") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Photo Picker
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
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
                    Text(if (language == AppLanguage.KANNADA) "ಫೋಟೋ ಸೇರಿಸಿ" else "Add Photo", style = MaterialTheme.typography.labelSmall)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(if (language == AppLanguage.KANNADA) "ಹೆಸರು" else "Student Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = achievement,
                        onValueChange = { achievement = it },
                        label = { Text(if (language == AppLanguage.KANNADA) "ಸಾಧನೆ" else "Achievement") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (name.isNotBlank() && achievement.isNotBlank()) {
                        viewModel.addStudentStar(name, achievement, selectedImageUri)
                        name = ""
                        achievement = ""
                        selectedImageUri = null
                        showDialog = false
                    }
                }) {
                    Text(if (language == AppLanguage.KANNADA) "ಸಲ್ಲಿಸಿ" else "Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDialog = false
                    selectedImageUri = null
                }) {
                    Text(if (language == AppLanguage.KANNADA) "ರದ್ದುಮಾಡಿ" else "Cancel")
                }
            }
        )
    }
}

@Composable
fun StarCelebrationCard(
    name: String, 
    achievement: String, 
    imageUrl: String,
    isAdmin: Boolean,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFFFE082).copy(alpha = 0.3f), Color.White)
                        )
                    )
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFFFFF9C4), CircleShape)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                name.take(1),
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFFBC02D)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Surface(
                    color = Color(0xFFFFF8E1),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        achievement,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color(0xFFE65100),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            if (isAdmin) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.align(Alignment.TopEnd).size(32.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
