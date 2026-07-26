package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.NeoCard
import com.example.ui.theme.*

@Composable
fun ProfilScreen() {
    Scaffold(containerColor = BlackPrimary) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NeoCard(
                backgroundColor = White,
                shadowColor = AccentBlue,
                borderColor = BlackPrimary,
                cornerRadius = 20.dp,
                strokeWidth = 4.dp,
                shadowOffset = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Profil Teknisi", style = MaterialTheme.typography.headlineMedium, color = BlackPrimary, fontWeight = FontWeight.Black)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Pengaturan profil sedang dalam pengembangan.", color = BlackSurfaceVariant, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
