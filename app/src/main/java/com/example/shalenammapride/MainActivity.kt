package com.example.shalenammapride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shalenammapride.ui.screens.MainScreen
import com.example.shalenammapride.ui.theme.ShaleNammaPrideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShaleNammaPrideTheme {
                MainScreen()
            }
        }
    }
}
