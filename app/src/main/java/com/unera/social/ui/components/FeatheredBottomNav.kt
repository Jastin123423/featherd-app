package com.unera.social.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unera.social.model.Screen

data class WebNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun FeatheredBottomNav(
    currentScreen: Screen,
    unreadMessagesCount: Int,
    onTabSelected: (Screen) -> Unit
) {
    val items = listOf(
        WebNavItem(
            screen = Screen.FEED,
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "nav_tab_feed"
        ),
        WebNavItem(
            screen = Screen.REELS,
            label = "Videos",
            selectedIcon = Icons.Filled.PlayCircle,
            unselectedIcon = Icons.Outlined.PlayCircle,
            testTag = "nav_tab_reels"
        ),
        WebNavItem(
            screen = Screen.MARKETPLACE,
            label = "MarketPoint",
            selectedIcon = Icons.Filled.Storefront,
            unselectedIcon = Icons.Outlined.Storefront,
            testTag = "nav_tab_marketplace"
        ),
        WebNavItem(
            screen = Screen.EVENTS,
            label = "Events",
            selectedIcon = Icons.Filled.CalendarMonth,
            unselectedIcon = Icons.Outlined.CalendarMonth,
            testTag = "nav_tab_events"
        ),
        WebNavItem(
            screen = Screen.MESSAGES,
            label = "Messages",
            selectedIcon = Icons.Filled.ChatBubble,
            unselectedIcon = Icons.Outlined.ChatBubbleOutline,
            testTag = "nav_tab_messages"
        )
    )

    Surface(
        color = Color(0xFF0B1120),
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("feathered_bottom_navigation")
    ) {
        Column {
            HorizontalDivider(color = Color(0xFF1E293B), thickness = 1.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentScreen == item.screen

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onTabSelected(item.screen) }
                            .testTag(item.testTag)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                tint = if (isSelected) Color(0xFF1877F2) else Color(0xFF94A3B8),
                                modifier = Modifier.size(22.dp)
                            )

                            if (item.screen == Screen.MESSAGES && unreadMessagesCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 6.dp, y = (-4).dp)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFEF4444)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = unreadMessagesCount.toString(),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = item.label,
                            fontSize = 10.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFF1877F2) else Color(0xFF94A3B8)
                        )

                        if (isSelected) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .width(6.dp)
                                    .height(3.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF1877F2))
                            )
                        } else {
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
            }
        }
    }
}
