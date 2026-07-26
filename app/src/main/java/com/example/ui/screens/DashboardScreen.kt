package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.data.ServiceJob
import com.example.ui.MainViewModel
import com.example.ui.components.NeoCard
import com.example.ui.theme.*

@Composable
fun DashboardScreen(viewModel: MainViewModel, navController: NavController) {
    val serviceJobs by viewModel.serviceJobs.collectAsStateWithLifecycle()
    
    val activeJobs = serviceJobs.count { it.status == "Menunggu" || it.status == "Pengerjaan" }
    val completedJobs = serviceJobs.count { it.status == "Selesai" }
    
    // Calculate profit from completed jobs
    val profit = serviceJobs.filter { it.status == "Selesai" }.sumOf { it.estimasiBiaya }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BlackPrimary)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp)
    ) {
        item {
            Text(
                "GANK SERVICE", 
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Text(
                "Dashboard Teknisi", 
                style = MaterialTheme.typography.titleMedium,
                color = Silver
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard("Servis Aktif", activeJobs.toString(), AccentBlue, Modifier.weight(1f))
                StatCard("Selesai", completedJobs.toString(), AccentSilver, Modifier.weight(1f))
            }
        }
        
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard("Profit", "Rp ${String.format("%,d", profit.toLong())}", White, Modifier.weight(1f))
                StatCard("Stok Menipis", "3 Item", AccentRed, Modifier.weight(1f))
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Servis Terbaru", 
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = White
            )
        }

        items(serviceJobs.take(5)) { job ->
            ServiceJobCard(job)
        }
        
        if (serviceJobs.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("Belum ada data servis.", color = GrayText)
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, accentColor: Color, modifier: Modifier = Modifier) {
    NeoCard(
        modifier = modifier.height(100.dp),
        backgroundColor = White,
        shadowColor = accentColor,
        borderColor = BlackPrimary,
        cornerRadius = 16.dp,
        strokeWidth = 4.dp,
        shadowOffset = 6.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = BlackPrimary)
            Text(title, style = MaterialTheme.typography.bodyMedium, color = BlackPrimary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ServiceJobCard(job: ServiceJob) {
    var showChecklist by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    NeoCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = White,
        shadowColor = Silver,
        borderColor = BlackPrimary,
        cornerRadius = 16.dp,
        strokeWidth = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("${job.brand} ${job.model}", fontWeight = FontWeight.Black, color = BlackPrimary, style = MaterialTheme.typography.titleMedium)
                StatusBadge(job.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Keluhan: ${job.keluhan}", color = BlackSurfaceVariant, fontWeight = FontWeight.Medium)
            Text("Diagnosa: ${job.diagnosa}", color = BlackSurfaceVariant, fontWeight = FontWeight.Medium)
            
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { /* TODO: Open Notes */ }) {
                    Text("Catatan", color = BlackPrimary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                com.example.ui.components.NeoButton(
                    onClick = { showChecklist = true },
                    backgroundColor = White,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    cornerRadius = 12.dp,
                    strokeWidth = 2.dp,
                    shadowOffset = 2.dp
                ) {
                    Text("Checklist", color = BlackPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
    
    if (showChecklist) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showChecklist = false }) {
            NeoCard(
                backgroundColor = White,
                shadowColor = AccentSilver,
                borderColor = BlackPrimary,
                cornerRadius = 24.dp,
                strokeWidth = 4.dp
            ) {
                Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                    Text("Checklist Teknisi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = BlackPrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val items = listOf("LCD/Touchscreen", "Baterai", "Konektor Cas", "Kamera Depan", "Kamera Belakang", "Speaker", "Mic", "Sinyal")
                    items.forEach { item ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            var checked by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
                            Checkbox(
                                checked = checked, 
                                onCheckedChange = { checked = it },
                                colors = CheckboxDefaults.colors(checkedColor = AccentBlue, checkmarkColor = White, uncheckedColor = GrayText)
                            )
                            Text(item, color = BlackPrimary, fontWeight = FontWeight.Medium)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        com.example.ui.components.NeoButton(
                            onClick = { showChecklist = false },
                            backgroundColor = AccentSilver,
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                        ) {
                            Text("Simpan", color = BlackPrimary, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val color = when(status) {
        "Selesai" -> AccentSilver
        "Pengerjaan" -> AccentBlue
        else -> White
    }
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(8.dp))
            .border(2.dp, BlackPrimary, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(status, color = BlackPrimary, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
    }
}
