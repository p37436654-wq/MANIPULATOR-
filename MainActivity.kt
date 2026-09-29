package com.manipulator.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ManipulatorApp() }
    }
}

@Composable
private fun ManipulatorApp() {
    var username by remember { mutableStateOf("Boss") }
    var listening by remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF050912)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(28.dp))
                    Text(
                        "MANIPULATOR",
                        color = Color(0xFF00E5FF),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "PERSONAL AI ASSISTANT",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(180.dp)
                            .background(
                                if (listening) Color(0xFF003B46)
                                else Color(0xFF071722),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (listening) "LISTENING" else "READY",
                            color = Color(0xFF00E5FF),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(28.dp))

                    Text(
                        "Hello, $username",
                        color = Color.White,
                        fontSize = 22.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        if (listening) "I'm listening for your command."
                        else "Tap the button to activate MANIPULATOR.",
                        color = Color.Gray
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { listening = !listening },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00A9C0)
                        )
                    ) {
                        Text(if (listening) "STOP LISTENING" else "ACTIVATE")
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            username = if (username == "Boss") "User" else "Boss"
                        }
                    ) {
                        Text("Change username")
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        "AI connection: Not configured",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
