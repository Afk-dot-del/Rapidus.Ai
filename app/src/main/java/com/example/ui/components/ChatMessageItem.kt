package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ChatMessage
import com.example.data.model.MessageStatus
import com.example.ui.theme.Cyan100
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Cyan600
import com.example.ui.theme.Cyan700
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Red500
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    userInitials: String = "DR",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    if (message.isUser) {
        // User Message Bubble (Right aligned with Clean Minimalism cyan-600 background & shadow)
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .widthIn(max = 320.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 4.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
                    color = Cyan600,
                    shadowElevation = 2.dp,
                    modifier = Modifier.testTag("user_message_bubble")
                ) {
                    Text(
                        text = message.text,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                    )
                }

                Text(
                    text = "SENT • " + formatTime(message.timestamp).uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Slate400,
                    modifier = Modifier.padding(top = 4.dp, end = 8.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // User Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Cyan100)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userInitials,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cyan700
                )
            }
        }
    } else {
        // AI Rapidus Message Bubble (Left aligned with Clean Minimalism white background, border-slate-100 & shadow-sm)
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            // Rapidus avatar
            Image(
                painter = painterResource(id = R.drawable.ic_rapidus_logo),
                contentDescription = "Rapidus AI",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(1.dp, Slate200, CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .widthIn(max = 340.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp, start = 2.dp)
                ) {
                    Text(
                        text = "Rapidus AI Ultra",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = formatTime(message.timestamp),
                            fontSize = 10.sp,
                            color = Slate400
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy message",
                            tint = Slate400,
                            modifier = Modifier
                                .size(14.dp)
                                .clickable {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Rapidus AI Text", message.text)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                }
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(topStart = 4.dp, topEnd = 24.dp, bottomStart = 24.dp, bottomEnd = 24.dp),
                    color = Color.White,
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .border(1.dp, Slate100, RoundedCornerShape(topStart = 4.dp, topEnd = 24.dp, bottomStart = 24.dp, bottomEnd = 24.dp))
                        .testTag("ai_message_bubble")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (message.status == MessageStatus.ERROR) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Error",
                                    tint = Red500,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Notice",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Red500
                                )
                            }
                        }

                        // Render text with code block support
                        RenderMarkdownText(text = message.text)
                    }
                }
            }
        }
    }
}

@Composable
fun RenderMarkdownText(text: String) {
    val lines = text.split("\n")
    var inCodeBlock = false
    val currentCodeLines = mutableListOf<String>()

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (line in lines) {
            if (line.trim().startsWith("```")) {
                if (inCodeBlock) {
                    // End of code block
                    val codeContent = currentCodeLines.joinToString("\n")
                    CodeBlock(code = codeContent)
                    currentCodeLines.clear()
                    inCodeBlock = false
                } else {
                    // Start of code block
                    inCodeBlock = true
                }
            } else if (inCodeBlock) {
                currentCodeLines.add(line)
            } else {
                if (line.isBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                } else if (line.trim().startsWith("- ") || line.trim().startsWith("* ")) {
                    Row(modifier = Modifier.padding(start = 4.dp, top = 2.dp)) {
                        Text(
                            text = "•",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Cyan600,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = cleanFormatting(line.trim().substring(2)),
                            fontSize = 13.sp,
                            color = Slate800,
                            lineHeight = 18.sp
                        )
                    }
                } else {
                    Text(
                        text = cleanFormatting(line),
                        fontSize = 13.sp,
                        color = Slate800,
                        lineHeight = 19.sp
                    )
                }
            }
        }

        if (inCodeBlock && currentCodeLines.isNotEmpty()) {
            CodeBlock(code = currentCodeLines.joinToString("\n"))
        }
    }
}

@Composable
fun CodeBlock(code: String) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Slate900,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CODE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Cyan500,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Copy",
                    fontSize = 10.sp,
                    color = Slate400,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Code snippet", code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Code copied", Toast.LENGTH_SHORT).show()
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = code,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                color = Slate100,
                lineHeight = 16.sp
            )
        }
    }
}

private fun cleanFormatting(text: String): String {
    return text
        .replace("**", "")
        .replace("`", "")
}

private fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
