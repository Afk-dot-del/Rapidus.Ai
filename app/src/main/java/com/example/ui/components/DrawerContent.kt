package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Conversation
import com.example.data.model.UserProfile
import com.example.ui.theme.Cyan100
import com.example.ui.theme.Cyan50
import com.example.ui.theme.Cyan500
import com.example.ui.theme.Cyan600
import com.example.ui.theme.Cyan700
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Emerald600
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.viewmodel.RapidusTab

@Composable
fun DrawerContent(
    currentTab: RapidusTab,
    onTabSelected: (RapidusTab) -> Unit,
    conversations: List<Conversation>,
    activeConversationId: String,
    onConversationSelected: (Conversation) -> Unit,
    onNewConversation: () -> Unit,
    userProfile: UserProfile,
    onGoogleSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onCloseDrawer: () -> Unit,
    showCloseButton: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Slate50,
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .border(width = 1.dp, color = Slate200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Top App Branding
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_rapidus_logo),
                            contentDescription = "Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, Cyan100, RoundedCornerShape(10.dp))
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Rapidus Ultra",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate900,
                                letterSpacing = (-0.3).sp
                            )
                            Text(
                                text = "SUPREME AI",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Cyan600,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }

                    if (showCloseButton) {
                        IconButton(
                            onClick = onCloseDrawer,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("close_drawer_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Menu",
                                tint = Slate400,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // New Conversation Button
                Button(
                    onClick = {
                        onNewConversation()
                        if (showCloseButton) onCloseDrawer()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Slate900,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("new_conversation_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "New Conversation",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Navigation Tabs
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    DrawerNavItem(
                        title = "AI Chat Studio",
                        icon = Icons.Default.Forum,
                        isSelected = currentTab == RapidusTab.CHAT,
                        onClick = {
                            onTabSelected(RapidusTab.CHAT)
                            if (showCloseButton) onCloseDrawer()
                        },
                        tag = "nav_chat"
                    )

                    DrawerNavItem(
                        title = "Advanced Tools",
                        icon = Icons.Default.Handyman,
                        isSelected = currentTab == RapidusTab.TOOLS,
                        onClick = {
                            onTabSelected(RapidusTab.TOOLS)
                            if (showCloseButton) onCloseDrawer()
                        },
                        tag = "nav_tools"
                    )

                    DrawerNavItem(
                        title = "Creators & Info",
                        icon = Icons.Default.Info,
                        isSelected = currentTab == RapidusTab.ABOUT,
                        onClick = {
                            onTabSelected(RapidusTab.ABOUT)
                            if (showCloseButton) onCloseDrawer()
                        },
                        tag = "nav_about"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Slate200, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "RECENT SESSIONS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate400,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )

                // Sessions List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(conversations) { convo ->
                        val isCurrent = convo.id == activeConversationId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isCurrent) Slate200.copy(alpha = 0.6f) else Color.Transparent)
                                .clickable {
                                    onConversationSelected(convo)
                                    onTabSelected(RapidusTab.CHAT)
                                    if (showCloseButton) onCloseDrawer()
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = null,
                                tint = if (isCurrent) Cyan700 else Slate400,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = convo.title,
                                fontSize = 11.sp,
                                fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isCurrent) Slate900 else Slate700,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // User Footer in Sidebar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                HorizontalDivider(color = Slate200, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Cyan100),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProfile.displayName.take(2).uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Cyan700
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = userProfile.displayName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate800,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(Emerald500)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Secure Node Active",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Emerald600
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onGoogleSignIn,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("auth_action_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Login,
                            contentDescription = "Google Sign In",
                            tint = Cyan600,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerNavItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Cyan50 else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) Cyan700 else Slate600,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) Cyan700 else Slate600
        )
    }
}
