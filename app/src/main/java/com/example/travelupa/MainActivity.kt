package com.example.travelupa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.travelupa.ui.theme.TravelupaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelupaTheme {
                GreetingScreen()
            }
        }
    }
}

@Composable
fun GreetingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = "Selamat Datang di Travelupa!",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Solusi buat kamu yang lupa kemana-mana",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(360.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Mulai")
        }
    }
}