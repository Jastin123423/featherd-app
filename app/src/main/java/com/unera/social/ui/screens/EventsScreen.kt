package com.unera.social.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.unera.social.model.Event
import com.unera.social.ui.theme.*

@Composable
fun EventsScreen(
    events: List<Event>,
    onToggleInterested: (Long) -> Unit,
    onToggleGoing: (Long) -> Unit,
    onOpenCreateEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("All") }
    val tabs = listOf("All", "Going", "Interested")

    val filteredEvents = when (selectedTab) {
        "Going" -> events.filter { it.isGoing }
        "Interested" -> events.filter { it.isInterested }
        else -> events
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
            .testTag("events_screen")
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Events",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Button(
                onClick = onOpenCreateEvent,
                colors = ButtonDefaults.buttonColors(containerColor = Carrot500),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.testTag("create_event_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Create",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // Filter Tabs
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            items(tabs) { tab ->
                val isSelected = tab == selectedTab
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedTab = tab },
                    label = {
                        Text(
                            text = tab,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) TextPrimary else TextSecondary
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Carrot500,
                        containerColor = Navy800
                    ),
                    border = null,
                    modifier = Modifier.testTag("events_tab_$tab")
                )
            }
        }

        // Events List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag("events_list")
        ) {
            items(filteredEvents, key = { it.id }) { event ->
                EventCard(
                    event = event,
                    onToggleInterested = { onToggleInterested(event.id) },
                    onToggleGoing = { onToggleGoing(event.id) }
                )
            }
        }
    }
}

@Composable
fun EventCard(
    event: Event,
    onToggleInterested: () -> Unit,
    onToggleGoing: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("event_card_${event.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Navy800)
    ) {
        Column {
            // Event Cover Image
            AsyncImage(
                model = event.imageUrl,
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            )

            Column(modifier = Modifier.padding(14.dp)) {
                // Date & Time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Date",
                        tint = Carrot500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = event.date,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = Carrot500
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Title
                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Location
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Location",
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = event.location,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = event.description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 2,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Social Attendance
                Text(
                    text = "${event.goingCount} going • ${event.interestedCount} interested",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Navy700, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Interested button
                    OutlinedButton(
                        onClick = onToggleInterested,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("event_interested_btn_${event.id}"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (event.isInterested) Navy700 else Navy900
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = if (event.isInterested) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = if (event.isInterested) Carrot500 else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (event.isInterested) "Interested" else "Interested?",
                            color = if (event.isInterested) Carrot500 else TextPrimary,
                            fontSize = 12.sp
                        )
                    }

                    // Going button
                    Button(
                        onClick = onToggleGoing,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("event_going_btn_${event.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (event.isGoing) ShareGreen else Carrot500
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (event.isGoing) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = TextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = if (event.isGoing) "Going!" else "Join / Go",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
