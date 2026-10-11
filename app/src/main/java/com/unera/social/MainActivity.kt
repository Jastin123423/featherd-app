package com.unera.social

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unera.social.data.MockData
import com.unera.social.model.Screen
import com.unera.social.ui.FeatheredViewModel
import com.unera.social.ui.components.FeatheredBottomNav
import com.unera.social.ui.components.FeatheredTopBar
import com.unera.social.ui.components.GlobalAudioPlayer
import com.unera.social.ui.components.MenuOverlay
import com.unera.social.ui.components.MusicCommentsSheet
import com.unera.social.ui.components.MusicShareSheet
import com.unera.social.ui.screens.*
import com.unera.social.ui.theme.FeatheredTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FeatheredTheme {
                FeatheredApp()
            }
        }
    }
}

@Composable
fun FeatheredApp(
    viewModel: FeatheredViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val unreadNotificationsCount = uiState.notifications.count { !it.isRead }
    val unreadMessagesCount = uiState.conversations.sumOf { it.unreadCount }

    // System Back Handler
    BackHandler(enabled = uiState.currentScreen != Screen.FEED || uiState.showNotificationsOverlay || uiState.showMenuOverlay) {
        if (uiState.showMenuOverlay) {
            viewModel.setShowMenuOverlay(false)
        } else if (uiState.showNotificationsOverlay) {
            viewModel.setShowNotificationsOverlay(false)
        } else if (uiState.activeConversationId != null) {
            viewModel.closeConversation()
        } else {
            viewModel.setScreen(Screen.FEED)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val hasCustomTopBar = uiState.currentScreen in listOf(
                Screen.MARKETPLACE,
                Screen.DISCOVER_PEOPLE,
                Screen.BIRTHDAYS,
                Screen.MEMORIES,
                Screen.SAVED_POSTS,
                Screen.STORY_FEED,
                Screen.REELS,
                Screen.MUSIC
            )
            if (!uiState.showNotificationsOverlay && uiState.activeConversationId == null && !hasCustomTopBar) {
                FeatheredTopBar(
                    currentUser = uiState.currentUser,
                    unreadNotificationsCount = unreadNotificationsCount,
                    onSearchClick = { viewModel.setShowSearchDialog(true) },
                    onNotificationsClick = { viewModel.setShowNotificationsOverlay(true) },
                    onProfileClick = { viewModel.setScreen(Screen.PROFILE) },
                    onMenuClick = { viewModel.setShowMenuOverlay(true) }
                )
            }
        },
        bottomBar = {
            if (!uiState.showNotificationsOverlay && uiState.activeConversationId == null) {
                Column {
                    if (uiState.currentAudioTrack != null) {
                        GlobalAudioPlayer(
                            currentTrack = uiState.currentAudioTrack,
                            isPlaying = uiState.isPlayingAudio,
                            positionSeconds = uiState.audioPositionSeconds,
                            durationSeconds = uiState.audioDurationSeconds,
                            isExpanded = uiState.isPlayerExpanded,
                            isLiked = "music:${uiState.currentAudioTrack?.id}" in uiState.likedTrackKeys,
                            isShuffle = uiState.isShuffleEnabled,
                            isRepeat = uiState.isRepeatEnabled,
                            volume = uiState.audioVolume,
                            isDownloading = uiState.downloadingTrackId == uiState.currentAudioTrack?.id,
                            downloadProgress = uiState.downloadProgress,
                            downloadToast = uiState.downloadToast,
                            uploaderProfile = uiState.users.find { it.id == uiState.currentAudioTrack?.uploaderId },
                            totalPlays = uiState.trackPlays["music:${uiState.currentAudioTrack?.id}"] ?: 0L,
                            onTogglePlay = { viewModel.togglePlayAudio() },
                            onNext = { viewModel.nextTrack() },
                            onPrevious = { viewModel.prevTrack() },
                            onSeek = { viewModel.seekAudio(it) },
                            onStop = { viewModel.stopAudio() },
                            onClose = { viewModel.closeAudioPlayer() },
                            onExpand = { viewModel.setPlayerExpanded(true) },
                            onCollapse = { viewModel.setPlayerExpanded(false) },
                            onToggleLike = { uiState.currentAudioTrack?.let { viewModel.toggleLikeTrack(it.id) } },
                            onToggleShuffle = { viewModel.toggleShuffle() },
                            onToggleRepeat = { viewModel.toggleRepeat() },
                            onVolumeChange = { viewModel.setAudioVolume(it) },
                            onDownload = { uiState.currentAudioTrack?.let { viewModel.downloadTrack(it.id) } },
                            onOpenDiscussions = { uiState.currentAudioTrack?.let { viewModel.openSongDiscussions(it) } },
                            onOpenShare = { uiState.currentAudioTrack?.let { viewModel.openShareTrack(it) } },
                            onArtistClick = { artistId ->
                                val targetUser = uiState.users.find { it.id == artistId }
                                if (targetUser != null) viewModel.openUserProfile(targetUser)
                            }
                        )
                    }
                    FeatheredBottomNav(
                        currentScreen = uiState.currentScreen,
                        unreadMessagesCount = unreadMessagesCount,
                        onTabSelected = { viewModel.setScreen(it) }
                    )
                }
            }
        }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        if (uiState.showNotificationsOverlay) {
            NotificationsScreen(
                notifications = uiState.notifications,
                users = uiState.users,
                currentUser = uiState.currentUser,
                onBack = { viewModel.setShowNotificationsOverlay(false) },
                onProfileClick = { userId ->
                    viewModel.setShowNotificationsOverlay(false)
                    viewModel.openUserProfileById(userId)
                },
                onMarkAllAsRead = { viewModel.markNotificationsAsRead() },
                onDeleteNotification = { notifId -> viewModel.deleteNotification(notifId) },
                onToggleRead = { notifId, isRead -> viewModel.markNotificationRead(notifId, isRead) },
                onAcceptGroupInvite = { notifId, groupId -> viewModel.acceptGroupInvite(notifId, groupId) },
                onDeclineGroupInvite = { notifId, groupId -> viewModel.declineGroupInvite(notifId, groupId) },
                modifier = screenModifier
            )
        } else {
            when (uiState.currentScreen) {
                Screen.FEED -> {
                    FeedScreen(
                        currentUser = uiState.currentUser,
                        stories = uiState.stories,
                        posts = uiState.posts,
                        onOpenCreatePost = { viewModel.setShowCreatePostDialog(true) },
                        onStoryClick = { viewModel.setSelectedStory(it) },
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onCommentPost = { viewModel.setSelectedPostForComments(it) },
                        onSavePost = { viewModel.toggleSavePost(it) },
                        onUserClick = { viewModel.startConversationWithUser(it) },
                        onAddStoryClick = { viewModel.setShowCreateStoryDialog(true) },
                        modifier = screenModifier
                    )
                }
                Screen.REELS -> {
                    ReelsScreen(
                        reels = uiState.reels,
                        posts = uiState.posts,
                        currentUser = uiState.currentUser,
                        onLikeReel = { viewModel.toggleLikeReel(it) },
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onCommentPost = { viewModel.setSelectedPostForComments(it) },
                        onSavePost = { viewModel.toggleSavePost(it) },
                        onUserClick = { viewModel.startConversationWithUser(it) },
                        onBack = { viewModel.setScreen(Screen.FEED) },
                        modifier = screenModifier
                    )
                }
                Screen.MARKETPLACE -> {
                    MarketplaceScreen(
                        products = uiState.products,
                        selectedCategory = uiState.selectedCategory,
                        selectedCountry = uiState.selectedCountry,
                        marketMode = uiState.marketMode,
                        isLoading = uiState.isProductsLoading,
                        onCategorySelected = { viewModel.setSelectedCategory(it) },
                        onCountrySelected = { viewModel.setSelectedCountry(it) },
                        onMarketModeSelected = { viewModel.setMarketMode(it) },
                        onProductClick = { viewModel.setSelectedProductForDetail(it) },
                        onOpenCreateProduct = { viewModel.setShowCreateProductDialog(true) },
                        onToggleSaveProduct = { viewModel.toggleSaveProduct(it) },
                        onRefresh = { viewModel.refreshLiveProducts() },
                        onNavigateBack = { viewModel.setScreen(Screen.FEED) },
                        modifier = screenModifier
                    )
                }
                Screen.EVENTS -> {
                    EventsScreen(
                        events = uiState.events,
                        onToggleInterested = { viewModel.toggleInterestedEvent(it) },
                        onToggleGoing = { viewModel.toggleGoingEvent(it) },
                        onOpenCreateEvent = { viewModel.setShowCreateEventDialog(true) },
                        onProfileClick = { userId ->
                            val target = uiState.users.find { it.id == userId }
                            if (target != null) viewModel.openUserProfile(target)
                        },
                        modifier = screenModifier
                    )
                }
                Screen.MESSAGES -> {
                    MessagesScreen(
                        conversations = uiState.conversations,
                        activeConversationId = uiState.activeConversationId,
                        currentUser = uiState.currentUser,
                        onOpenConversation = { viewModel.openConversation(it) },
                        onCloseConversation = { viewModel.closeConversation() },
                        onSendMessage = { convId, text -> viewModel.sendMessage(convId, text) },
                        modifier = screenModifier
                    )
                }
                Screen.PROFILE -> {
                    val activeUser = uiState.selectedUserProfile ?: uiState.currentUser
                    ProfileScreen(
                        user = activeUser,
                        currentUser = uiState.currentUser,
                        allUsers = uiState.users,
                        allPosts = uiState.posts,
                        allReels = uiState.reels,
                        allStories = uiState.stories,
                        allProducts = uiState.products,
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onCommentPost = { viewModel.setSelectedPostForComments(it) },
                        onSavePost = { viewModel.toggleSavePost(it) },
                        onSharePost = { viewModel.sharePost(it) },
                        onUpdateUserDetails = { bio, work, edu, loc, web ->
                            viewModel.updateUserDetails(bio, work, edu, loc, web)
                        },
                        onFollowUser = { viewModel.toggleFollowUser(it) },
                        onProfileClick = { targetUser ->
                            viewModel.openUserProfile(targetUser)
                        },
                        onMessageUser = { targetUser ->
                            viewModel.startConversationWithUser(targetUser)
                        },
                        onAddStoryClick = {
                            viewModel.setShowCreateStoryDialog(true)
                        },
                        onVerifyUser = { viewModel.toggleVerifyUser(it) },
                        onSuspendUser = { userId, duration, reason ->
                            viewModel.suspendUser(userId, duration, reason)
                        },
                        onUnsuspendUser = { viewModel.unsuspendUser(it) },
                        onToggleModeratorRole = { viewModel.toggleModeratorRole(it) },
                        onBack = if (activeUser.id != uiState.currentUser.id) {
                            { viewModel.openCurrentUserProfile() }
                        } else null,
                        modifier = screenModifier
                    )
                }
                Screen.DISCOVER_PEOPLE -> {
                    SuggestedProfilesPage(
                        currentUser = uiState.currentUser,
                        users = uiState.users,
                        onFollow = { viewModel.toggleFollowUser(it) },
                        onProfileClick = { userId ->
                            val targetUser = uiState.users.find { it.id == userId }
                            if (targetUser != null) {
                                viewModel.startConversationWithUser(targetUser)
                            }
                        },
                        onBack = { viewModel.setScreen(Screen.FEED) },
                        modifier = screenModifier
                    )
                }
                Screen.BIRTHDAYS -> {
                    BirthdaysPage(
                        currentUser = uiState.currentUser,
                        users = uiState.users,
                        onMessage = { userId ->
                            val targetUser = uiState.users.find { it.id == userId }
                            if (targetUser != null) {
                                viewModel.startConversationWithUser(targetUser)
                            }
                        },
                        onProfileClick = { userId ->
                            val targetUser = uiState.users.find { it.id == userId }
                            if (targetUser != null) {
                                viewModel.startConversationWithUser(targetUser)
                            }
                        },
                        onBack = { viewModel.setScreen(Screen.FEED) },
                        modifier = screenModifier
                    )
                }
                Screen.SAVED_POSTS -> {
                    SavedPostsPage(
                        currentUser = uiState.currentUser,
                        users = uiState.users,
                        posts = uiState.posts,
                        reels = uiState.reels,
                        onProfileClick = { userId ->
                            val targetUser = uiState.users.find { it.id == userId }
                            if (targetUser != null) {
                                viewModel.openUserProfile(targetUser)
                            }
                        },
                        onVideoClick = { reel ->
                            viewModel.setScreen(Screen.REELS)
                        },
                        onOpenComments = { post ->
                            viewModel.setSelectedPostForComments(post)
                        },
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onCommentPost = { viewModel.setSelectedPostForComments(it) },
                        onSavePost = { viewModel.toggleSavePost(it) },
                        onSaveReel = { viewModel.toggleSaveReel(it) },
                        onShare = { viewModel.sharePost(it.id) },
                        onBack = { viewModel.setScreen(Screen.FEED) },
                        modifier = screenModifier
                    )
                }
                Screen.MEMORIES -> {
                    MemoriesPage(
                        currentUser = uiState.currentUser,
                        posts = uiState.posts,
                        onLikePost = { viewModel.toggleLikePost(it) },
                        onCommentPost = { viewModel.setSelectedPostForComments(it) },
                        onBack = { viewModel.setScreen(Screen.FEED) },
                        modifier = screenModifier
                    )
                }
                Screen.STORY_FEED -> {
                    StoryFeedPage(
                        currentUser = uiState.currentUser,
                        stories = uiState.stories,
                        onBack = { viewModel.setScreen(Screen.FEED) },
                        modifier = screenModifier
                    )
                }
                Screen.MUSIC -> {
                    MusicSystemScreen(
                        currentUser = uiState.currentUser,
                        users = uiState.users,
                        songs = uiState.songs,
                        currentTrack = uiState.currentAudioTrack,
                        isPlaying = uiState.isPlayingAudio,
                        likedTrackKeys = uiState.likedTrackKeys,
                        myTotalPlays = uiState.myTotalPlays,
                        trackPlays = uiState.trackPlays,
                        uploadProgress = uiState.songUploadProgress,
                        uploadStatus = uiState.songUploadStatus,
                        onPlayTrack = { viewModel.playTrack(it) },
                        onTogglePlay = { viewModel.togglePlayAudio() },
                        onToggleLikeTrack = { viewModel.toggleLikeTrack(it) },
                        onUploadSong = { title, artist, genre, album, cover, audio ->
                            viewModel.uploadSong(title, artist, genre, album, cover, audio)
                        },
                        onDeleteUploadedSong = { viewModel.deleteUploadedSong(it) },
                        onProfileClick = { userId ->
                            val target = uiState.users.find { it.id == userId }
                            if (target != null) viewModel.openUserProfile(target)
                        },
                        onBack = { viewModel.setScreen(Screen.FEED) },
                        modifier = screenModifier
                    )
                }
            }
        }
    }

    // Modal Dialogs
    if (uiState.showCreatePostDialog) {
        CreatePostDialog(
            currentUser = uiState.currentUser,
            onDismiss = { viewModel.setShowCreatePostDialog(false) },
            onSubmit = { content, mediaUrl, feeling ->
                viewModel.createPost(content, mediaUrl, feeling)
            }
        )
    }

    if (uiState.selectedPostForComments != null) {
        CommentsDialog(
            post = uiState.selectedPostForComments!!,
            onDismiss = { viewModel.setSelectedPostForComments(null) },
            onAddComment = { commentText ->
                viewModel.addComment(uiState.selectedPostForComments!!.id, commentText)
            }
        )
    }

    if (uiState.selectedProductForDetail != null) {
        ProductDetailDialog(
            product = uiState.selectedProductForDetail!!,
            onDismiss = { viewModel.setSelectedProductForDetail(null) },
            onContactSeller = { seller ->
                viewModel.startConversationWithUser(seller, uiState.selectedProductForDetail)
            }
        )
    }

    if (uiState.showCreateProductDialog) {
        CreateProductDialog(
            onDismiss = { viewModel.setShowCreateProductDialog(false) },
            onSubmit = { title, price, discountPrice, category, location, country, description, imageUrl, images, phone ->
                viewModel.createProduct(
                    title = title,
                    price = price,
                    discountPrice = discountPrice,
                    category = category,
                    location = location,
                    country = country,
                    description = description,
                    imageUrl = imageUrl,
                    images = images,
                    phone = phone
                )
            }
        )
    }

    if (uiState.showCreateEventDialog) {
        CreateEventModal(
            currentUser = uiState.currentUser,
            onClose = { viewModel.setShowCreateEventDialog(false) },
            onCreate = { title, desc, date, time, location, visibility, coverUrl, gId, gName ->
                viewModel.createEvent(
                    title = title,
                    date = if (time.isNotBlank()) "$date • $time" else date,
                    location = location,
                    description = desc,
                    imageUrl = coverUrl,
                    time = time,
                    visibility = visibility,
                    groupId = gId,
                    groupName = gName
                )
            }
        )
    }

    if (uiState.showSearchDialog) {
        SearchDialog(
            query = uiState.searchQuery,
            onQueryChange = { viewModel.setSearchQuery(it) },
            posts = uiState.posts,
            products = uiState.products,
            users = MockData.users,
            onSelectUser = { viewModel.startConversationWithUser(it) },
            onDismiss = {
                viewModel.setShowSearchDialog(false)
                viewModel.setSearchQuery("")
            }
        )
    }

    if (uiState.showMenuOverlay) {
        MenuOverlay(
            currentUser = uiState.currentUser,
            onClose = { viewModel.setShowMenuOverlay(false) },
            onNavigate = { destination ->
                viewModel.setShowMenuOverlay(false)
                when (destination) {
                    "marketplace" -> viewModel.setScreen(Screen.MARKETPLACE)
                    "events" -> viewModel.setScreen(Screen.EVENTS)
                    "reels" -> viewModel.setScreen(Screen.REELS)
                    "notifications" -> viewModel.setShowNotificationsOverlay(true)
                    "profile" -> viewModel.setScreen(Screen.PROFILE)
                    "profiles" -> viewModel.setScreen(Screen.DISCOVER_PEOPLE)
                    "birthdays" -> viewModel.setScreen(Screen.BIRTHDAYS)
                    "memories" -> viewModel.setScreen(Screen.MEMORIES)
                    "saved-posts", "saved" -> viewModel.setScreen(Screen.SAVED_POSTS)
                    "story-feed" -> viewModel.setScreen(Screen.STORY_FEED)
                    "music" -> viewModel.setScreen(Screen.MUSIC)
                    "groups" -> viewModel.setScreen(Screen.DISCOVER_PEOPLE)
                    "tools" -> viewModel.setShowSearchDialog(true)
                    "ads" -> viewModel.setScreen(Screen.MARKETPLACE)
                    else -> {}
                }
            },
            onLogout = {
                viewModel.setShowMenuOverlay(false)
            }
        )
    }

    // Story Viewer Dialog (matching Web StoryViewer in Storypage)
    uiState.selectedStory?.let { currentStory ->
        StoryViewerDialog(
            story = currentStory,
            allStories = uiState.stories,
            currentUser = uiState.currentUser,
            onClose = { viewModel.setSelectedStory(null) },
            onNextStory = {
                val stories = uiState.stories
                val idx = stories.indexOfFirst { it.id == currentStory.id }
                if (idx in 0 until stories.size - 1) {
                    viewModel.setSelectedStory(stories[idx + 1])
                } else {
                    viewModel.setSelectedStory(null)
                }
            },
            onPrevStory = {
                val stories = uiState.stories
                val idx = stories.indexOfFirst { it.id == currentStory.id }
                if (idx > 0) {
                    viewModel.setSelectedStory(stories[idx - 1])
                } else {
                    viewModel.setSelectedStory(null)
                }
            },
            onReact = { reaction ->
                viewModel.reactToStory(currentStory.id, reaction)
            },
            onDeleteStory = {
                viewModel.deleteStory(currentStory.id)
            },
            onToggleFollow = { authorId ->
                // Handled in view model or conversation
            },
            onProfileClick = { authorId ->
                viewModel.setSelectedStory(null)
                val user = uiState.users.find { it.id == authorId }
                if (user != null) {
                    viewModel.startConversationWithUser(user)
                }
            }
        )
    }

    // Create Story Modal Dialog (matching Web CreateStoryModal in Storypage)
    if (uiState.showCreateStoryDialog) {
        CreateStoryDialog(
            currentUser = uiState.currentUser,
            onDismiss = { viewModel.setShowCreateStoryDialog(false) },
            onCreateStory = { mediaUrl, type, textContent, backgroundStyle, musicTitle ->
                viewModel.createStory(
                    mediaUrl = mediaUrl,
                    type = type,
                    textContent = textContent,
                    backgroundStyle = backgroundStyle,
                    musicTitle = musicTitle
                )
            }
        )
    }

    // Music Discussions Bottom Sheet Modal
    if (uiState.activeDiscussionTrack != null) {
        MusicCommentsSheet(
            track = uiState.activeDiscussionTrack,
            currentUser = uiState.currentUser,
            comments = uiState.songComments[uiState.activeDiscussionTrack!!.id] ?: emptyList(),
            onClose = { viewModel.closeSongDiscussions() },
            onAddComment = { text -> viewModel.addSongComment(uiState.activeDiscussionTrack!!.id, text) },
            onToggleHide = { commentId -> viewModel.toggleHideSongComment(uiState.activeDiscussionTrack!!.id, commentId) },
            onDeleteComment = { commentId -> viewModel.deleteSongComment(uiState.activeDiscussionTrack!!.id, commentId) },
            onProfileClick = { userId ->
                viewModel.closeSongDiscussions()
                val target = uiState.users.find { it.id == userId }
                if (target != null) viewModel.openUserProfile(target)
            }
        )
    }

    // Music Share Bottom Sheet Modal
    if (uiState.activeShareTrack != null) {
        MusicShareSheet(
            track = uiState.activeShareTrack,
            onClose = { viewModel.closeShareTrack() },
            onShareDestination = { _ ->
                viewModel.closeShareTrack()
            }
        )
    }
}

