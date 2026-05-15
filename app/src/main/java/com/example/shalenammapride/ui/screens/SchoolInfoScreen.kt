package com.example.shalenammapride.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shalenammapride.data.model.SchoolInfo
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

@Composable
fun SchoolInfoScreen(viewModel: FirebaseViewModel) {
    val language by viewModel.language.collectAsState()
    val info = SchoolInfo()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // School Header Image Placeholder
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    Icons.Default.School,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                )
                Text(
                    if (language == AppLanguage.KANNADA) "ಸರ್ಕಾರಿ ಶಾಲೆ" else "Government School",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (language == AppLanguage.KANNADA) "ನಮ್ಮ ಬಗ್ಗೆ" else "About Our School",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        InfoItem(Icons.Default.Person, if (language == AppLanguage.KANNADA) "ಮುಖ್ಯೋಪಾಧ್ಯಾಯರು" else "Head Master", info.hmName)
        InfoItem(Icons.Default.LocationOn, if (language == AppLanguage.KANNADA) "ವಿಳಾಸ" else "Address", if (language == AppLanguage.KANNADA) info.addressKn else info.addressEn)
        InfoItem(Icons.Default.AccessTime, if (language == AppLanguage.KANNADA) "ಶಾಲಾ ಸಮಯ" else "Timings", if (language == AppLanguage.KANNADA) info.timingsKn else info.timingsEn)
        
        Spacer(modifier = Modifier.height(24.dp))

        // Emergency Contacts Section
        Text(
            text = if (language == AppLanguage.KANNADA) "ತುರ್ತು ಸಂಪರ್ಕಗಳು" else "Emergency Contacts",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFD32F2F)
        )
        Spacer(modifier = Modifier.height(12.dp))

        ContactCard(
            name = if (language == AppLanguage.KANNADA) "ಮುಖ್ಯೋಪಾಧ್ಯಾಯರು (HM)" else "Head Master (HM)",
            phone = info.contact,
            icon = Icons.Default.Call,
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${info.contact}"))
                context.startActivity(intent)
            }
        )
        ContactCard(
            name = if (language == AppLanguage.KANNADA) "ಶಾಲಾ ಕಚೇರಿ" else "School Office",
            phone = "08172-234567",
            icon = Icons.Default.Phone,
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:08172234567"))
                context.startActivity(intent)
            }
        )
        ContactCard(
            name = if (language == AppLanguage.KANNADA) "ಅಂಬ್ಯುಲೆನ್ಸ್" else "Ambulance",
            phone = "108",
            icon = Icons.Default.MedicalServices,
            color = Color(0xFFF44336),
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
                context.startActivity(intent)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (language == AppLanguage.KANNADA) "ನಮ್ಮ ಗುರಿ" else "Our Mission",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (language == AppLanguage.KANNADA) info.missionKn else info.missionEn,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun InfoItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ContactCard(name: String, phone: String, icon: ImageVector, color: Color = Color(0xFF1976D2), onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = color.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(text = phone, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.LightGray)
        }
    }
}
