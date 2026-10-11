package com.unera.social.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.unera.social.model.Notification
import com.unera.social.model.NotificationType
import com.unera.social.ui.theme.*

@Composable
fun NotificationsScreen(
    notifications: List<Notification>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
            .testTag("notifications_screen")
    ) {
        // Header
        Surface(color = Navy850, tonalElevation = 4.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("notifications_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Notifications",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        // List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("notifications_list")
        ) {
            items(notifications, key = { it.id }) { notif ->
                NotificationItem(notification = notif)
                HorizontalDivider(color = Navy800, thickness = 1.dp)
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    val icon = when (notification.type) {
        NotificationType.LIKE -> Icons.Default.Favorite
        NotificationType.COMMENT -> Icons.Default.ChatBubble
        NotificationType.FOLLOW -> Icons.Default.PersonAdd
        NotificationType.EVENT -> Icons.Default.Event
        NotificationType.MESSAGE -> Icons.Default.Mail
    }
    val iconColor = when (notification.type) {
        NotificationType.LIKE -> LikeRed
        NotificationType.COMMENT -> Carrot500
        NotificationType.FOLLOW -> UneraBlue
        NotificationType.EVENT -> Carrot500
        NotificationType.MESSAGE -> ShareGreen
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (!notification.isRead) Navy800.copy(alpha = 0.5f) else Navy900)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            AsyncImage(
                model = notification.user.profileImageUrl,
                contentDescription = notification.user.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(iconColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(11.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${notification.user.name} ${notification.content}",
                fontSize = 13.sp,
                color = TextPrimary,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = notification.timeAgo,
                fontSize = 11.sp,
                color = TextSecondary
            )
        }
    }
}
