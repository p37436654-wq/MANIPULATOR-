package com.manipulator.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private const val API_URL = "https://manipulator-8r07kl4py-p37436654-wq.vercel.app/api/chat"

data class ChatMessage(val role: String, val text: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { Surface(Modifier.fillMaxSize()) { ManipulatorChat() } } }
    }
}

@Composable
private fun ManipulatorChat() {
    val messages = remember { mutableStateListOf(ChatMessage("assistant", "MANIPULATOR online. How can I help you, Boss?")) }
    var input by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("MANIPULATOR", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        LazyColumn(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(messages) { msg -> Text(if (msg.role == "user") "You: ${msg.text}" else "MANIPULATOR: ${msg.text}") }
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                enabled = !loading,
                placeholder = { Text("Type a command...") },
                singleLine = true
            )
            Button(enabled = input.isNotBlank() && !loading, onClick = {
                val text = input.trim()
                input = ""
                messages.add(ChatMessage("user", text))
                loading = true
            }) { Text(if (loading) "..." else "Send") }
        }
    }

    LaunchedEffect(messages.size, loading) {
        if (loading && messages.lastOrNull()?.role == "user") {
            val reply = sendMessage(messages.last().text)
            messages.add(ChatMessage("assistant", reply))
            loading = false
        }
    }
}

private suspend fun sendMessage(message: String): String = withContext(Dispatchers.IO) {
    try {
        val connection = (URL(API_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 15000
            readTimeout = 30000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
        }
        val body = JSONObject().put("message", message).toString()
        connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
        val code = connection.responseCode
        val stream = if (code in 200..299) connection.inputStream else connection.errorStream
        val response = stream?.bufferedReader()?.use { it.readText() } ?: "{}"
        connection.disconnect()
        val json = JSONObject(response)
        if (code !in 200..299) "Server error: ${json.optString("error", "Request failed")}" else json.optString("reply", "No reply received.")
    } catch (e: Exception) {
        "Connection error: ${e.message ?: "Unable to reach server"}"
    }
}
