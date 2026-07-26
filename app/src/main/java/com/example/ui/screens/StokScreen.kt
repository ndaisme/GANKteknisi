package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.Sparepart
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.components.NeoTextField
import com.example.ui.theme.*

@Composable
fun StokScreen(viewModel: MainViewModel) {
    val spareparts by viewModel.spareparts.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedSparepart by remember { mutableStateOf<Sparepart?>(null) }

    Scaffold(
        containerColor = BlackPrimary,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = AccentRed,
                contentColor = White
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text("Inventori Sparepart", style = MaterialTheme.typography.headlineLarge, color = White, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(spareparts) { part ->
                    NeoCard(
                        modifier = Modifier.fillMaxWidth().clickable { selectedSparepart = part },
                        backgroundColor = White,
                        shadowColor = AccentRed,
                        borderColor = BlackPrimary,
                        cornerRadius = 16.dp,
                        strokeWidth = 4.dp
                    ) {
                        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(part.nama, fontWeight = FontWeight.Black, color = BlackPrimary, style = MaterialTheme.typography.titleMedium)
                                Text(part.kategori, color = BlackSurfaceVariant, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Modal: Rp ${String.format("%,d", part.hargaModal.toLong())} | Jual: Rp ${String.format("%,d", part.hargaJual.toLong())}", color = AccentBlue, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Stok", color = GrayText, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Text("${part.stok}", color = if (part.stok > 2) BlackPrimary else AccentRed, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog || selectedSparepart != null) {
        var nama by remember { mutableStateOf(selectedSparepart?.nama ?: "") }
        var kategori by remember { mutableStateOf(selectedSparepart?.kategori ?: "") }
        var hargaModal by remember { mutableStateOf(if (selectedSparepart != null) selectedSparepart!!.hargaModal.toLong().toString() else "") }
        var hargaJual by remember { mutableStateOf(if (selectedSparepart != null) selectedSparepart!!.hargaJual.toLong().toString() else "") }
        var stok by remember { mutableStateOf(if (selectedSparepart != null) selectedSparepart!!.stok.toString() else "") }

        androidx.compose.ui.window.Dialog(onDismissRequest = { 
            showAddDialog = false
            selectedSparepart = null
        }) {
            NeoCard(
                backgroundColor = White,
                shadowColor = AccentRed,
                borderColor = BlackPrimary,
                cornerRadius = 24.dp,
                strokeWidth = 4.dp
            ) {
                LazyColumn(modifier = Modifier.padding(24.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        Text(if (selectedSparepart == null) "Tambah Sparepart" else "Edit Sparepart", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = BlackPrimary)
                    }
                    item { NeoTextField(value = nama, onValueChange = { nama = it }, label = "Nama Sparepart") }
                    item { NeoTextField(value = kategori, onValueChange = { kategori = it }, label = "Kategori") }
                    item { NeoTextField(value = hargaModal, onValueChange = { hargaModal = it }, label = "Harga Modal", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) }
                    item { NeoTextField(value = hargaJual, onValueChange = { hargaJual = it }, label = "Harga Jual", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) }
                    item { NeoTextField(value = stok, onValueChange = { stok = it }, label = "Jumlah Stok", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            if (selectedSparepart != null) {
                                TextButton(onClick = { 
                                    viewModel.deleteSparepart(selectedSparepart!!.id)
                                    selectedSparepart = null
                                }) {
                                    Text("Hapus", color = AccentRed, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            
                            Row {
                                TextButton(onClick = { 
                                    showAddDialog = false
                                    selectedSparepart = null
                                }) {
                                    Text("Batal", color = GrayText, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                NeoButton(
                                    onClick = { 
                                        if (selectedSparepart == null) {
                                            viewModel.addSparepart(nama, kategori, hargaModal.toDoubleOrNull() ?: 0.0, hargaJual.toDoubleOrNull() ?: 0.0, stok.toIntOrNull() ?: 0)
                                        } else {
                                            viewModel.updateSparepart(selectedSparepart!!.id, nama, kategori, hargaModal.toDoubleOrNull() ?: 0.0, hargaJual.toDoubleOrNull() ?: 0.0, stok.toIntOrNull() ?: 0)
                                        }
                                        showAddDialog = false
                                        selectedSparepart = null
                                    },
                                    backgroundColor = AccentRed
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
}
