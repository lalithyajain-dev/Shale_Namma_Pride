package com.example.shalenammapride.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

@Composable
fun FeedbackBoxScreen(viewModel: FirebaseViewModel) {
    val feedbacks by viewModel.feedbacks.collectAsState()
    val language by viewModel.language.collectAsState()
    
    var comment by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(true) }
    var parentName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Feedback, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (language == AppLanguage.KANNADA) "ಅಭಿಪ್ರಾಯ ಪೆಟ್ಟಿಗೆ" else "Feedback Box",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        
        Text(
            if (language == AppLanguage.KANNADA) "ನಿಮ್ಮ ಸಲಹೆಗಳನ್ನು ಇಲ್ಲಿ ಹಂಚಿಕೊಳ್ಳಿ" else "Share your thoughts or suggestions",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(if (language == AppLanguage.KANNADA) "ನಿಮ್ಮ ಸಂದೇಶ" else "Your Message") },
                    minLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isAnonymous,
                        onCheckedChange = { isAnonymous = it }
                    )
                    Text(if (language == AppLanguage.KANNADA) "ಅನಾಮಧೇಯವಾಗಿ ಕಳುಹಿಸಿ" else "Post Anonymously")
                }

                if (!isAnonymous) {
                    OutlinedTextField(
                        value = parentName,
                        onValueChange = { parentName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(if (language == AppLanguage.KANNADA) "ನಿಮ್ಮ ಹೆಸರು" else "Your Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                Button(
                    onClick = {
                        if (comment.isNotBlank()) {
                            viewModel.addFeedback(comment, isAnonymous, parentName)
                            comment = ""
                            parentName = ""
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (language == AppLanguage.KANNADA) "ಸಲ್ಲಿಸಿ" else "Submit Feedback")
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

        Text(
            if (language == AppLanguage.KANNADA) "ಇತ್ತೀಚಿನ ಪ್ರತಿಕ್ರಿಯೆಗಳು" else "Recent Feedback",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(feedbacks) { feedback ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (feedback.anonymous) Icons.Default.PersonOff else Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val userLabel = if (feedback.anonymous) {
                                if (language == AppLanguage.KANNADA) "ಅನಾಮಧೇಯ ಪೋಷಕರು" else "Anonymous Parent"
                            } else feedback.user
                            
                            Text(userLabel, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(feedback.comment, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}
