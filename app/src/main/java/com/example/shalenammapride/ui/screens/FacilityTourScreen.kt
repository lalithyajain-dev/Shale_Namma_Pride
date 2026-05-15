package com.example.shalenammapride.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shalenammapride.ui.viewmodel.AppLanguage
import com.example.shalenammapride.ui.viewmodel.FirebaseViewModel

data class Facility(
    val nameKn: String, 
    val nameEn: String, 
    val descriptionEn: String, 
    val descriptionKn: String, 
    val color: Color,
    val icon: ImageVector,
    val statusEn: String = "Well Maintained",
    val statusKn: String = "ಉತ್ತಮ ನಿರ್ವಹಣೆ"
)

@Composable
fun FacilityTourScreen(viewModel: FirebaseViewModel) {
    val language by viewModel.language.collectAsState()
    
    val facilities = listOf(
        Facility("ಸ್ಮಾರ್ಟ್ ತರಗತಿ", "Smart Classroom", "Modern digital learning with interactive boards.", "ಸಂವಾದಾತ್ಮಕ ಬೋರ್ಡ್‌ಗಳೊಂದಿಗೆ ಆಧುನಿಕ ಡಿಜಿಟಲ್ ಕಲಿಕೆ.", Color(0xFFE3F2FD), Icons.Default.CastConnected),
        Facility("ವಿಜ್ಞಾನ ಪ್ರಯೋಗಾಲಯ", "Science Lab", "Full equipment for physics, chemistry, and biology.", "ಭೌತಶಾಸ್ತ್ರ, ರಸಾಯನಶಾಸ್ತ್ರ ಮತ್ತು ಜೀವಶಾಸ್ತ್ರಕ್ಕಾಗಿ ಪೂರ್ಣ ಉಪಕರಣಗಳು.", Color(0xFFF3E5F5), Icons.Default.Science),
        Facility("ಗಣಕಯಂತ್ರ ಕೊಠಡಿ", "Computer Lab", "20+ computers with high-speed internet.", "ಅತಿ ವೇಗದ ಇಂಟರ್ನೆಟ್ ಹೊಂದಿರುವ 20+ ಕಂಪ್ಯೂಟರ್‌ಗಳು.", Color(0xFFE8F5E9), Icons.Default.Computer),
        Facility("ಗ್ರಂಥಾಲಯ", "Library", "A vast collection of 5000+ books and journals.", "5000+ ಪುಸ್ತಕಗಳು ಮತ್ತು ನಿಯತಕಾಲಿಕೆಗಳ ಬೃಹತ್ ಸಂಗ್ರಹ.", Color(0xFFFFF3E0), Icons.Default.MenuBook),
        Facility("ಆಟದ ಮೈದಾನ", "Playground", "Large ground for sports like kho-kho and kabaddi.", "ಖೋ-ಖೋ ಮತ್ತು ಕಬಡ್ಡಿಯಂತಹ ಕ್ರೀಡೆಗಳಿಗಾಗಿ ವಿಶಾಲವಾದ ಮೈದಾನ.", Color(0xFFF1F8E9), Icons.Default.SportsBasketball),
        Facility("ಕುಡಿಯುವ ನೀರು", "Drinking Water", "RO purified drinking water facility for all.", "ಎಲ್ಲರಿಗೂ RO ಶುದ್ಧೀಕರಿಸಿದ ಕುಡಿಯುವ ನೀರಿನ ಸೌಲಭ್ಯ.", Color(0xFFE0F7FA), Icons.Default.WaterDrop),
        Facility("ಬಿಸಿಯೂಟದ ಅಡುಗೆ ಮನೆ", "Midday Meal Kitchen", "Clean and hygienic kitchen for quality meals.", "ಗುಣಮಟ್ಟದ ಊಟಕ್ಕಾಗಿ ಸ್ವಚ್ಛ ಮತ್ತು ನೈರ್ಮಲ್ಯದ ಅಡುಗೆ ಮನೆ.", Color(0xFFFFFDE7), Icons.Default.SoupKitchen),
        Facility("ಶೌಚಾಲಯ ಸೌಲಭ್ಯ", "Washroom Facility", "Separate and clean washrooms for boys and girls.", "ಬಾಲಕ ಮತ್ತು ಬಾಲಕಿಯರಿಗಾಗಿ ಪ್ರತ್ಯೇಕ ಮತ್ತು ಸ್ವಚ್ಛ ಶೌಚಾಲಯಗಳು.", Color(0xFFFFEBEE), Icons.Default.Wc)
    )

    val pagerState = rememberPagerState(pageCount = { facilities.size })

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Collections, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (language == AppLanguage.KANNADA) "ಶಾಲಾ ಸೌಲಭ್ಯಗಳು" else "School Facilities",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp
        ) { page ->
            val facility = facilities[page]
            Card(
                modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = facility.color),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(28.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            facility.icon,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.KANNADA) facility.statusKn else facility.statusEn,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        if (language == AppLanguage.KANNADA) facility.nameKn else facility.nameEn, 
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        if (language == AppLanguage.KANNADA) facility.descriptionKn else facility.descriptionEn, 
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = Color.DarkGray
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            Modifier.fillMaxWidth().height(20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(facilities.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(10.dp)
                        .background(color, RoundedCornerShape(5.dp))
                )
            }
        }
    }
}
