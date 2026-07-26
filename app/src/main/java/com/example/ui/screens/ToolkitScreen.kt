package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.*
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.components.NeoTextField
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.BuildConfig

data class ChatMessage(val text: String, val isUser: Boolean)

@Composable
fun ToolkitScreen() {
    var prompt by remember { mutableStateOf("") }
    var chatHistory by remember { mutableStateOf(listOf(
        ChatMessage("Halo! Saya AI Assistant GANK SERVICE. Ada kendala kerusakan HP apa yang ingin Anda konsultasikan?", false)
    )) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(containerColor = BlackPrimary) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text(
                "AI Agent Diagnosa", 
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Text(
                "Konsultasikan gejala kerusakan untuk analisa", 
                style = MaterialTheme.typography.titleMedium,
                color = Silver
            )
            Spacer(modifier = Modifier.height(16.dp))

            NeoCard(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                backgroundColor = White,
                shadowColor = AccentBlue,
                borderColor = BlackPrimary,
                cornerRadius = 20.dp,
                strokeWidth = 4.dp,
                shadowOffset = 6.dp
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chatHistory) { message ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .background(
                                        color = if (message.isUser) AccentBlue else AccentSilver,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = BlackPrimary,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Text(
                                    message.text,
                                    color = if (message.isUser) White else BlackPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    if (isLoading) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                                Text("Berpikir...", color = GrayText, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    NeoTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = "Tanya sesuatu...",
                        singleLine = false
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                NeoButton(
                    onClick = {
                        if (prompt.isNotBlank() && !isLoading) {
                            val userText = prompt
                            chatHistory = chatHistory + ChatMessage(userText, true)
                            prompt = ""
                            isLoading = true
                            coroutineScope.launch {
                                val reply = fetchAiResponse(userText)
                                chatHistory = chatHistory + ChatMessage(reply, false)
                                isLoading = false
                            }
                        }
                    },
                    backgroundColor = AccentBlue,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text("Kirim", color = White, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

suspend fun fetchAiResponse(prompt: String): String = withContext(Dispatchers.IO) {
    try {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "API Key Gemini belum diatur. Silakan atur di Secrets Panel (atau .env.example untuk lokal)."
        }
        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = Content(parts = listOf(Part(text = "Kamu adalah AI Assistant ahli dari GANK SERVICE. Tugasmu adalah menganalisa gejala kerusakan smartphone yang diberikan pengguna (teknisi). Berikan probabilitas kerusakan, komponen yang harus dicek, dan estimasi biaya perbaikan di Indonesia secara ringkas, profesional, dan dalam bahasa Indonesia. Selalu gunakan format yang mudah dibaca dengan poin-poin.")))
        )
        val response = RetrofitClient.service.generateContent(apiKey, request)
        response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "Tidak ada respons."
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}
