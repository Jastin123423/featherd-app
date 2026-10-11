package com.unera.social.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.unera.social.model.User

data class WebMenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val color: Color
)

val WEB_MENU_ITEMS = listOf(
    WebMenuItem("marketplace", "MarketPoint", Icons.Default.Storefront, Color(0xFF1877F2)),
    WebMenuItem("saved-posts", "Saved Posts", Icons.Default.Bookmark, Color(0xFFF59E0B)),
    WebMenuItem("events", "Events", Icons.Default.CalendarMonth, Color(0xFF2563EB)),
    WebMenuItem("profiles", "Profiles", Icons.Default.People, Color(0xFF1877F2)),
    WebMenuItem("groups", "Groups", Icons.Default.Groups, Color(0xFF38BDF8)),
    WebMenuItem("music", "F-Music", Icons.Default.MusicNote, Color(0xFF1877F2)),
    WebMenuItem("tools", "Feathered Tools", Icons.Default.Work, Color(0xFF2DD4BF)),
    WebMenuItem("reels", "Videos", Icons.Default.PlayCircle, Color(0xFF1877F2)),
    WebMenuItem("birthdays", "Birthdays", Icons.Default.Cake, Color(0xFFFBBF24)),
    WebMenuItem("memories", "Memories", Icons.Default.History, Color(0xFF818CF8)),
    WebMenuItem("story-feed", "Story Feed", Icons.Default.Layers, Color(0xFF34D399)),
    WebMenuItem("notifications", "Notifications", Icons.Default.Notifications, Color(0xFF1877F2)),
    WebMenuItem("ads", "Ad Manager", Icons.Default.TrendingUp, Color(0xFF10B981))
)

data class WebBottomItem(
    val id: String,
    val title: String,
    val icon: ImageVector
)

val WEB_BOTTOM_ITEMS = listOf(
    WebBottomItem("settings", "Settings & Privacy", Icons.Default.Settings),
    WebBottomItem("privacy", "Privacy Policy", Icons.Default.Security),
    WebBottomItem("help", "Help & Support", Icons.Default.HelpOutline),
    WebBottomItem("terms", "Terms of Service", Icons.Default.Description)
)

@Composable
fun MenuOverlay(
    currentUser: User,
    onClose: () -> Unit,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B1120))
                .statusBarsPadding()
                .testTag("menu_overlay")
        ) {
            // Header matching web: `h-16 px-4 flex items-center justify-between border-b border-[#1E293B]`
            Surface(
                color = Color(0xFF0B1120),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFF166FE5), Color(0xFF1877F2))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = "Menu",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAFC)
                        )
                    }

                    // Close button: w-9 h-9 bg-[#162137]/65 border border-[#1E293B]/60 rounded-xl
                    Surface(
                        onClick = onClose,
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF162137).copy(alpha = 0.65f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B).copy(alpha = 0.6f)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close menu",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFF1E293B), thickness = 1.dp)

            // Scrollable Content
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // User Profile Card matching web
                item(span = { GridItemSpan(2) }) {
                    Surface(
                        onClick = {
                            onNavigate("profile")
                            onClose()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF162137).copy(alpha = 0.65f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B).copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = currentUser.profileImageUrl,
                                contentDescription = currentUser.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, Color(0xFF1877F2).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentUser.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF8FAFC)
                                )
                                Text(
                                    text = "View your profile",
                                    fontSize = 13.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Grid of 13 Services
                items(WEB_MENU_ITEMS, key = { it.id }) { item ->
                    Surface(
                        onClick = {
                            onNavigate(item.id)
                            onClose()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF162137).copy(alpha = 0.65f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B).copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(item.color.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = item.color,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Text(
                                text = item.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF8FAFC)
                            )
                        }
                    }
                }

                // Bottom Settings & Privacy Section
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Settings & Support",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                    )
                }

                items(WEB_BOTTOM_ITEMS, key = { it.id }) { item ->
                    Surface(
                        onClick = {
                            onNavigate(item.id)
                            onClose()
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF162137).copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B).copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = item.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFF8FAFC)
                            )
                        }
                    }
                }

                // Logout Button
                item(span = { GridItemSpan(2) }) {
                    Surface(
                        onClick = {
                            onLogout()
                            onClose()
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEF4444).copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Log Out",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Log Out",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF4444)
                            )
                        }
                    }
                }
            }
        }
    }
}
