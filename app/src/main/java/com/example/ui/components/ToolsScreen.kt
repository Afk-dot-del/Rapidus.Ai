package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DiagnosticItem
import com.example.ui.theme.CleanBackground
import com.example.ui.theme.Cyan100
import com.example.ui.theme.Cyan50
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Cyan600
import com.example.ui.theme.Cyan700
import com.example.ui.theme.Emerald100
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Indigo50
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Indigo700
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun ToolsScreen(
    diagnostics: List<DiagnosticItem>,
    activityLogs: List<String>,
    onRunDiagnostic: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var codeSnippet by remember {
        mutableStateOf(
            "// Rapidus Syntax Test\nfun computeEfficiency(latencyMs: Long): Boolean {\n    return latencyMs < 50\n}"
        )
    }
    var parseStatus by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CleanBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Column {
            Text(
                text = "System Diagnostic & Tools",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Text(
                text = "Real-time client telemetry, cryptographic tunnels, and code lexers",
                fontSize = 11.sp,
                color = Slate600
            )
        }

        // Diagnostic Cards
        for (item in diagnostics) {
            val icon: ImageVector = when (item.id) {
                "api-tunnel" -> Icons.Default.Security
                "code-parser" -> Icons.Default.Code
                else -> Icons.Default.Bolt
            }
            val accentColor: Color = when (item.id) {
                "api-tunnel" -> Cyan600
                "code-parser" -> Indigo600
                else -> Cyan700
            }
            val badgeBg: Color = when (item.id) {
                "api-tunnel" -> Cyan50
                "code-parser" -> Indigo50
                else -> Cyan100
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Slate100, RoundedCornerShape(20.dp))
                    .testTag("diagnostic_card_${item.id}")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = icon,
                                contentDescription = item.name,
                                tint = accentColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = badgeBg,
                            modifier = Modifier.border(1.dp, accentColor.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = item.statusText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = accentColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Text(
                        text = item.description,
                        fontSize = 12.sp,
                        color = Slate600,
                        lineHeight = 17.sp
                    )

                    if (item.detailMessage.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Slate100,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.detailMessage,
                                fontSize = 11.sp,
                                color = Slate700,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { onRunDiagnostic(item.id) },
                        enabled = !item.isRunning,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Slate800,
                            disabledContainerColor = Slate100
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                            .testTag("run_button_${item.id}")
                    ) {
                        if (item.isRunning) {
                            CircularProgressIndicator(
                                color = accentColor,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Running Diagnostic...",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate600
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.buttonText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Slate800
                            )
                        }
                    }
                }
            }
        }

        // Interactive Code Sandbox / Syntax Evaluator
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Slate100, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "Sandbox",
                        tint = Indigo600,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Interactive Syntax Sandbox",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }

                OutlinedTextField(
                    value = codeSnippet,
                    onValueChange = { codeSnippet = it },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = Slate800
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Cyan600,
                        unfocusedBorderColor = Slate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            val openBrackets = codeSnippet.count { it == '{' }
                            val closeBrackets = codeSnippet.count { it == '}' }
                            parseStatus = if (openBrackets == closeBrackets) {
                                "Syntax verified: Balanced brackets ({$openBrackets}/{$closeBrackets}). Zero anomalies detected."
                            } else {
                                "Syntax warning: Bracket mismatch ({$openBrackets} open vs {$closeBrackets} closed)."
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Slate900,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text(text = "Validate Syntax", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }

                    if (parseStatus != null) {
                        Text(
                            text = parseStatus ?: "",
                            fontSize = 10.sp,
                            color = if (parseStatus?.contains("warning") == true) Color(0xFFE11D48) else Emerald600,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f).padding(start = 10.dp)
                        )
                    }
                }
            }
        }

        // Terminal Activity Logs
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Slate900,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "Terminal",
                        tint = Cyan500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE SYSTEM TELEMETRY (FIRESTORE SYNCED)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Cyan500,
                        letterSpacing = 0.8.sp
                    )
                }

                for (log in activityLogs.take(5)) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                        Text(
                            text = "›",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Emerald500,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = log,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Slate300,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
