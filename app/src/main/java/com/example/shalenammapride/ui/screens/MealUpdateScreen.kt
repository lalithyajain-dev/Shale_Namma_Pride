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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restaurant
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
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

@Composable
fun MealUpdateScreen(viewModel: FirebaseViewModel) {
    val meals by viewModel.meals.collectAsState()
    val language by viewModel.language.collectAsState()
    val todayMeal by viewModel.todayMeal.collectAsState()
    val isAdmin by viewModel.isAdmin.collectAsState()
    
    var isEditing by remember { mutableStateOf(false) }
    var menuEn by remember { mutableStateOf("") }
    var menuKn by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(todayMeal, isEditing) {
        if (todayMeal != null && isEditing) {
            menuEn = todayMeal!!.menuEn
            menuKn = todayMeal!!.menuKn
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            if (language == AppLanguage.KANNADA) "ಬಿಸಿಯೂಟದ ವಿವರ" else "Meal Updates",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        if (isAdmin && (todayMeal == null || isEditing)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        if (isEditing) 
                            (if (language == AppLanguage.KANNADA) "ಮೆನು ತಿದ್ದುಪಡಿ ಮಾಡಿ" else "Edit Today's Menu")
                        else 
                            (if (language == AppLanguage.KANNADA) "ಇಂದಿನ ಮೆನು ಅಪ್‌ಲೋಡ್ ಮಾಡಿ" else "Post Today's Menu"),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.5f))
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
                        } else if (todayMeal?.imageUrl?.isNotEmpty() == true && isEditing) {
                            AsyncImage(
                                model = todayMeal!!.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                                Text(if (language == AppLanguage.KANNADA) "ಫೋಟೋ ಸೇರಿಸಿ" else "Add Photo", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = menuKn,
                        onValueChange = { menuKn = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(if (language == AppLanguage.KANNADA) "ಕನ್ನಡದಲ್ಲಿ ಮೆನು" else "Menu in Kannada") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = menuEn,
                        onValueChange = { menuEn = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(if (language == AppLanguage.KANNADA) "ಇಂಗ್ಲಿಷ್‌ನಲ್ಲಿ ಮೆನು" else "Menu in English") }
                    )
                    
                    Row(modifier = Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (isEditing) {
                            OutlinedButton(onClick = { isEditing = false }, modifier = Modifier.weight(1f)) {
                                Text(if (language == AppLanguage.KANNADA) "ರದ್ದು" else "Cancel")
                            }
                        }
                        Button(
                            onClick = {
                                if (menuEn.isNotBlank() || menuKn.isNotBlank()) {
                                    viewModel.addMeal(menuEn, menuKn, selectedImageUri)
                                    menuEn = ""
                                    menuKn = ""
                                    selectedImageUri = null
                                    isEditing = false
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(if (language == AppLanguage.KANNADA) "ಸಲ್ಲಿಸಿ" else "Post Update")
                        }
                    }
                }
            }
        } else if (todayMeal != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column {
                    if (todayMeal!!.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = todayMeal!!.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color(0xFF43A047))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(if (language == AppLanguage.KANNADA) "ಇಂದಿನ ಮೆನು ಪೋಸ್ಟ್ ಮಾಡಲಾಗಿದೆ" else "Today's Menu is Live", fontWeight = FontWeight.Bold)
                            Text(if (language == AppLanguage.KANNADA) todayMeal!!.menuKn else todayMeal!!.menuEn)
                        }
                        
                        if (isAdmin) {
                            IconButton(onClick = { isEditing = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { viewModel.deleteMeal(todayMeal!!) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            if (language == AppLanguage.KANNADA) "ಹಿಂದಿನ ವರದಿಗಳು" else "History",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(meals) { meal ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column {
                        if (meal.imageUrl.isNotEmpty()) {
                            AsyncImage(
                                model = meal.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Restaurant, contentDescription = null, tint = Color.LightGray)
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                val displayText = if (language == AppLanguage.KANNADA) {
                                    if (meal.menuKn.isNotBlank()) meal.menuKn else meal.menuEn
                                } else {
                                    if (meal.menuEn.isNotBlank()) meal.menuEn else meal.menuKn
                                }
                                Text(text = displayText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(meal.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}
