package com.unera.social.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.unera.social.model.Post
import com.unera.social.model.Story
import com.unera.social.model.User
import com.unera.social.ui.theme.*

@Composable
fun FeedScreen(
    currentUser: User,
    stories: List<Story>,
    posts: List<Post>,
    onOpenCreatePost: () -> Unit,
    onStoryClick: (Story) -> Unit,
    onLikePost: (Long) -> Unit,
    onCommentPost: (Post) -> Unit,
    onSavePost: (Long) -> Unit,
    onUserClick: (User) -> Unit,
    onAddStoryClick: () -> Unit = onOpenCreatePost,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
            .testTag("feed_screen_list"),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // Stories Row
        item {
            StoriesSection(
                currentUser = currentUser,
                stories = stories,
                onStoryClick = onStoryClick,
                onAddStoryClick = onAddStoryClick
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // What's on your mind? Card
        item {
            CreatePostCard(
                currentUser = currentUser,
                onClick = onOpenCreatePost
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Posts
        items(posts, key = { it.id }) { post ->
            PostCard(
                post = post,
                onLike = { onLikePost(post.id) },
                onComment = { onCommentPost(post) },
                onSave = { onSavePost(post.id) },
                onUserClick = { onUserClick(post.author) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun StoriesSection(
    currentUser: User,
    stories: List<Story>,
    onStoryClick: (Story) -> Unit,
    onAddStoryClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A))
            .padding(vertical = 12.dp)
            .testTag("stories_section"),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Web Create Story Card (110x200dp with creator profile image, blue plus circle, and "Create Story" label)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
                modifier = Modifier
                    .width(112.dp)
                    .height(190.dp)
                    .clickable(onClick = onAddStoryClick)
                    .testTag("add_story_btn")
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = currentUser.profileImageUrl,
                        contentDescription = "Create",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.72f)
                    )
                    // Bottom label bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.28f)
                            .align(Alignment.BottomCenter)
                            .background(Color(0xFF0F172A)),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Text(
                            text = "Create Story",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF8FAFC),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }

                    // Floating blue plus icon
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 36.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF166FE5), Color(0xFF1877F2))
                                )
                            )
                            .border(3.dp, Color(0xFF0F172A), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Story",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Other Story Cards (Facebook/Web style card with gradient or photo, author avatar with blue ring, and name)
        items(stories, key = { it.id }) { story ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B)),
                modifier = Modifier
                    .width(112.dp)
                    .height(190.dp)
                    .clickable { onStoryClick(story) }
                    .testTag("story_item_${story.id}")
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (story.type == "text" || story.mediaUrl.isBlank()) {
                        // Text story card
                        val gradientColors = remember(story.backgroundStyle) {
                            when {
                                story.backgroundStyle.contains("#F3425F") -> listOf(Color(0xFFF3425F), Color(0xFFE41E3F))
                                story.backgroundStyle.contains("#45BD62") -> listOf(Color(0xFF45BD62), Color(0xFF31A24C))
                                story.backgroundStyle.contains("#F7B928") -> listOf(Color(0xFFF7B928), Color(0xFFE3A300))
                                story.backgroundStyle.contains("#A033FF") -> listOf(Color(0xFFA033FF), Color(0xFF7B1FA2))
                                else -> listOf(Color(0xFF1877F2), Color(0xFF0055FF))
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.linearGradient(gradientColors))
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = story.textContent.ifBlank { "Story" },
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 4,
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    } else {
                        // Photo or video story card
                        AsyncImage(
                            model = story.mediaUrl,
                            contentDescription = story.author.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Card dark vignette
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.35f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.75f)
                                    )
                                )
                            )
                    )

                    // Top-left author avatar with blue ring
                    AsyncImage(
                        model = story.author.profileImageUrl,
                        contentDescription = story.author.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color(0xFF1877F2), CircleShape)
                    )

                    // Bottom-left author name
                    Text(
                        text = story.author.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CreatePostCard(
    currentUser: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick)
            .testTag("create_post_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Navy800)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = currentUser.profileImageUrl,
                    contentDescription = currentUser.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(RoundedCornerShape(21.dp))
                        .background(Navy700)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "What's on your mind, ${currentUser.name.split(" ").firstOrNull() ?: ""}?",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Navy700, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Photo",
                        tint = Color(0xFF45BD62),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Photo/Video", color = TextSecondary, fontSize = 12.sp)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mood,
                        contentDescription = "Feeling",
                        tint = Color(0xFFF7B928),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Feeling/Activity", color = TextSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onSave: () -> Unit,
    onUserClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("post_card_${post.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Navy800)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Author Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = post.author.profileImageUrl,
                    contentDescription = post.author.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onUserClick)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.author.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        if (post.author.isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Verified",
                                tint = VerifiedBlue,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        if (post.feeling != null) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "is ${post.feeling}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Text(
                        text = "${post.author.username} • ${post.timestamp}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                IconButton(
                    onClick = onSave,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("save_post_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save Post",
                        tint = if (post.isSaved) Carrot500 else TextSecondary
                    )
                }
            }

            // Post Text
            if (post.content.isNotBlank()) {
                Text(
                    text = post.content,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 2.dp)
                )
            }

            // Media Image
            if (!post.mediaUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = post.mediaUrl,
                    contentDescription = "Post image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Post Engagement Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = LikeRed,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = post.likesCount.toString(),
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Text(
                    text = "${post.commentsCount} comments • ${post.sharesCount} shares",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Navy700, thickness = 1.dp)

            // Interaction Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Like Button
                TextButton(
                    onClick = onLike,
                    modifier = Modifier.testTag("like_post_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = if (post.isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (post.isLiked) LikeRed else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Like",
                        color = if (post.isLiked) LikeRed else TextSecondary,
                        fontSize = 12.sp
                    )
                }

                // Comment Button
                TextButton(
                    onClick = onComment,
                    modifier = Modifier.testTag("comment_post_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comment",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Comment",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                // Share Button
                TextButton(
                    onClick = { /* Share */ },
                    modifier = Modifier.testTag("share_post_btn_${post.id}")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Share",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
