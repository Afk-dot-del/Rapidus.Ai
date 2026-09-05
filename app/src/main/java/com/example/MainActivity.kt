package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AboutScreen
import com.example.ui.components.ChatScreen
import com.example.ui.components.DrawerContent
import com.example.ui.components.ToolsScreen
import com.example.ui.theme.CleanBackground
import com.example.ui.theme.Cyan100
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Cyan600
import com.example.ui.theme.Cyan700
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo600
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.RapidusTab
import com.example.ui.viewmodel.RapidusViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RapidusApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RapidusApp(
    viewModel: RapidusViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Slate50,
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                DrawerContent(
                    currentTab = uiState.currentTab,
                    onTabSelected = { tab ->
                        viewModel.switchTab(tab)
                        scope.launch { drawerState.close() }
                    },
                    conversations = uiState.conversations,
                    activeConversationId = uiState.activeConversation.id,
                    onConversationSelected = { convo ->
                        // Switch active conversation
                        scope.launch { drawerState.close() }
                    },
                    onNewConversation = {
                        viewModel.startNewConversation()
                        scope.launch { drawerState.close() }
                    },
                    userProfile = uiState.userProfile,
                    onGoogleSignIn = { viewModel.signInWithGoogle() },
                    onSignOut = { viewModel.signOut() },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    },
                    showCloseButton = true
                )
            }
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                Surface(
                    color = Color.White,
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Slate100)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Slate100)
                                    .testTag("menu_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open Sidebar Navigation",
                                    tint = Slate800,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            // Rapidus Neon Green Lightning Bolt Logo
                            Image(
                                painter = painterResource(id = R.drawable.ic_rapidus_logo),
                                contentDescription = "Rapidus Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(1.dp, Slate200, RoundedCornerShape(10.dp))
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Rapidus Ultra",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate900,
                                    letterSpacing = (-0.2).sp
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 1.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(Emerald500)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "SUPREME NODE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Slate500,
                                        letterSpacing = 0.8.sp
                                    )
                                }
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Quick Action Button
                            IconButton(
                                onClick = { viewModel.startNewConversation() },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Slate100)
                                    .testTag("header_new_chat_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "New Session",
                                    tint = Slate600,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Avatar Circle
                            val initials = if (uiState.userProfile.displayName.length >= 2) {
                                uiState.userProfile.displayName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.joinToString("").take(2).uppercase()
                            } else {
                                "DR"
                            }
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Cyan100)
                                    .border(2.dp, Color.White, CircleShape)
                                    .clickable {
                                        viewModel.switchTab(RapidusTab.ABOUT)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = initials,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Cyan700
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Slate100)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CleanNavButton(
                            label = "Chat",
                            icon = Icons.Default.Forum,
                            isSelected = uiState.currentTab == RapidusTab.CHAT,
                            onClick = { viewModel.switchTab(RapidusTab.CHAT) }
                        )
                        CleanNavButton(
                            label = "Tools",
                            icon = Icons.Default.Handyman,
                            isSelected = uiState.currentTab == RapidusTab.TOOLS,
                            onClick = { viewModel.switchTab(RapidusTab.TOOLS) }
                        )
                        CleanNavButton(
                            label = "Info",
                            icon = Icons.Default.Info,
                            isSelected = uiState.currentTab == RapidusTab.ABOUT,
                            onClick = { viewModel.switchTab(RapidusTab.ABOUT) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Crossfade(
                    targetState = uiState.currentTab,
                    label = "screen_crossfade"
                ) { tab ->
                    when (tab) {
                        RapidusTab.CHAT -> {
                            ChatScreen(
                                messages = uiState.messages,
                                inputText = uiState.inputText,
                                onInputChange = { viewModel.updateInputText(it) },
                                onSendMessage = { viewModel.sendMessage() },
                                isGenerating = uiState.isGenerating,
                                statusNotice = uiState.statusNotice,
                                onDismissNotice = { viewModel.dismissNotice() },
                                userDisplayName = uiState.userProfile.displayName
                            )
                        }
                        RapidusTab.TOOLS -> {
                            ToolsScreen(
                                diagnostics = uiState.diagnostics,
                                activityLogs = uiState.activityLogs,
                                onRunDiagnostic = { toolId -> viewModel.runDiagnostic(toolId) }
                            )
                        }
                        RapidusTab.ABOUT -> {
                            AboutScreen(
                                userProfile = uiState.userProfile,
                                onGoogleSignIn = { viewModel.signInWithGoogle() },
                                onSignOut = { viewModel.signOut() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CleanNavButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Cyan600 else Slate400,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Cyan600 else Slate400
        )
    }
}

// For test and preview backward-compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
