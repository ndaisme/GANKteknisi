package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
            ServiceJobCard(job, viewModel)
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
fun ServiceJobCard(job: ServiceJob, viewModel: MainViewModel) {
    var showChecklist by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var showStatusDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var showWarrantyDialog by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    NeoCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = White,
        shadowColor = Silver,
        borderColor = BlackPrimary,
        cornerRadius = 16.dp,
        strokeWidth = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("${job.brand} ${job.model}", fontWeight = FontWeight.Black, color = BlackPrimary, style = MaterialTheme.typography.titleMedium)
                Box(modifier = Modifier.clickable { showStatusDialog = true }) {
                    StatusBadge(job.status)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (job.noService.isNotEmpty()) {
                Text("No Servis: ${job.noService}", color = AccentBlue, fontWeight = FontWeight.Bold)
            }
            Text("Keluhan: ${job.keluhan}", color = BlackSurfaceVariant, fontWeight = FontWeight.Medium)
            Text("Diagnosa: ${job.diagnosa}", color = BlackSurfaceVariant, fontWeight = FontWeight.Medium)
            if (job.garansiJenis.isNotEmpty()) {
                val hariBerjalan = ((System.currentTimeMillis() - job.garansiTanggalMulai) / (1000 * 60 * 60 * 24)).toInt()
                val sisaHari = (job.garansiDurasiHari - hariBerjalan).coerceAtLeast(0)
                val textSisa = if (sisaHari > 0) "Sisa: $sisaHari hari" else "Habis"
                val colorGaransi = if (sisaHari > 0) AccentBlue else AccentRed
                Text("Garansi: ${job.garansiJenis} ($textSisa)", color = colorGaransi, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (job.status == "Selesai" || job.status == "Diambil") {
                    TextButton(onClick = { showWarrantyDialog = true }) {
                        Text("Garansi", color = AccentBlue, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
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
                            onClick = { 
                                showChecklist = false
                                if (job.status == "Menunggu") {
                                    viewModel.updateServiceStatus(job.id, "Diagnosa")
                                }
                            },
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

    if (showStatusDialog) {
        val nextStatuses = when(job.status) {
            "Menunggu" -> listOf("Diagnosa", "Batal")
            "Diagnosa" -> listOf("Menunggu Sparepart", "Pengerjaan", "Batal")
            "Menunggu Sparepart" -> listOf("Pengerjaan", "Batal")
            "Pengerjaan" -> listOf("QC", "Selesai", "Batal")
            "QC" -> listOf("Selesai", "Pengerjaan")
            "Selesai" -> listOf("Diambil")
            "Diambil" -> listOf()
            "Batal" -> listOf()
            else -> listOf("Menunggu", "Diagnosa", "Menunggu Sparepart", "Pengerjaan", "QC", "Selesai", "Diambil", "Batal")
        }
        
        if (nextStatuses.isNotEmpty()) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { showStatusDialog = false }) {
                NeoCard(
                    backgroundColor = White,
                    shadowColor = AccentBlue,
                    borderColor = BlackPrimary,
                    cornerRadius = 24.dp,
                    strokeWidth = 4.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
                        Text("Update Status (Saat Ini: ${job.status})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = BlackPrimary)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                            items(nextStatuses) { status ->
                                TextButton(
                                    onClick = {
                                        viewModel.updateServiceStatus(job.id, status)
                                        showStatusDialog = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Ubah ke $status", color = AccentBlue, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            showStatusDialog = false
        }
    }

    if (showWarrantyDialog) {
        var jenis by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(job.garansiJenis) }
        var durasi by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(if (job.garansiDurasiHari > 0) job.garansiDurasiHari.toString() else "") }
        var riwayat by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(job.garansiRiwayatKlaim) }

        androidx.compose.ui.window.Dialog(onDismissRequest = { showWarrantyDialog = false }) {
            NeoCard(
                backgroundColor = White,
                shadowColor = AccentBlue,
                borderColor = BlackPrimary,
                cornerRadius = 24.dp,
                strokeWidth = 4.dp
            ) {
                LazyColumn(modifier = Modifier.padding(24.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        Text("Pengaturan Garansi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = BlackPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    item { com.example.ui.components.NeoTextField(value = jenis, onValueChange = { jenis = it }, label = "Jenis Garansi (e.g. LCD, Baterai)") }
                    item { 
                        com.example.ui.components.NeoTextField(
                            value = durasi, 
                            onValueChange = { durasi = it }, 
                            label = "Durasi (Hari)", 
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
                        ) 
                    }
                    item { com.example.ui.components.NeoTextField(value = riwayat, onValueChange = { riwayat = it }, label = "Riwayat Klaim", singleLine = false) }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { showWarrantyDialog = false }) {
                                Text("Batal", color = GrayText, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            com.example.ui.components.NeoButton(
                                onClick = { 
                                    viewModel.updateWarranty(
                                        id = job.id,
                                        tanggalMulai = if (job.garansiTanggalMulai == 0L) System.currentTimeMillis() else job.garansiTanggalMulai,
                                        jenis = jenis,
                                        durasiHari = durasi.toIntOrNull() ?: 0,
                                        riwayatKlaim = riwayat
                                    )
                                    showWarrantyDialog = false
                                },
                                backgroundColor = AccentBlue
                            ) {
                                Text("Simpan", color = White, fontWeight = FontWeight.Black)
                            }
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
