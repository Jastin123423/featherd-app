package com.unera.social.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unera.social.data.MarketplaceApiService
import com.unera.social.data.MockData
import com.unera.social.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FeatheredUiState(
    val currentScreen: Screen = Screen.FEED,
    val currentUser: User = MockData.currentUser,
    val users: List<User> = MockData.users,
    val posts: List<Post> = MockData.posts,
    val stories: List<Story> = MockData.stories,
    val reels: List<Reel> = MockData.reels,
    val products: List<Product> = MockData.products,
    val isProductsLoading: Boolean = false,
    val selectedCategory: String = "all",
    val selectedCountry: String = "all",
    val marketMode: String = "for_you", // "for_you", "local", "worldwide"
    val events: List<Event> = MockData.events,
    val conversations: List<Conversation> = MockData.conversations,
    val activeConversationId: String? = null,
    val notifications: List<Notification> = MockData.notifications,
    val searchQuery: String = "",
    val showCreatePostDialog: Boolean = false,
    val selectedPostForComments: Post? = null,
    val showSearchDialog: Boolean = false,
    val showNotificationsOverlay: Boolean = false,
    val showCreateEventDialog: Boolean = false,
    val showCreateProductDialog: Boolean = false,
    val selectedProductForDetail: Product? = null,
    val selectedStory: Story? = null,
    val showCreateStoryDialog: Boolean = false,
    val showMenuOverlay: Boolean = false,
    val selectedUserProfile: User? = null
)

class FeatheredViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeatheredUiState())
    val uiState: StateFlow<FeatheredUiState> = _uiState.asStateFlow()

    init {
        refreshLiveProducts()
    }

    fun refreshLiveProducts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProductsLoading = true) }
            val liveProducts = MarketplaceApiService.fetchLiveProducts()
            _uiState.update { state ->
                val combined = if (liveProducts.isNotEmpty()) {
                    val liveIds = liveProducts.map { it.id }.toSet()
                    val fallbackRemaining = state.products.filter { it.id !in liveIds }
                    liveProducts + fallbackRemaining
                } else {
                    state.products
                }
                state.copy(
                    products = combined,
                    isProductsLoading = false
                )
            }
        }
    }

    fun setScreen(screen: Screen) {
        _uiState.update {
            it.copy(
                currentScreen = screen,
                activeConversationId = if (screen != Screen.MESSAGES) null else it.activeConversationId
            )
        }
        if (screen == Screen.MARKETPLACE) {
            refreshLiveProducts()
        }
    }

    fun setMarketMode(mode: String) {
        _uiState.update { it.copy(marketMode = mode) }
    }

    fun setSelectedCountry(country: String) {
        _uiState.update { it.copy(selectedCountry = country) }
    }

    fun setSelectedCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun toggleLikePost(postId: Long) {
        _uiState.update { state ->
            val updatedPosts = state.posts.map { post ->
                if (post.id == postId) {
                    val newLiked = !post.isLiked
                    val newCount = if (newLiked) post.likesCount + 1 else (post.likesCount - 1).coerceAtLeast(0)
                    post.copy(isLiked = newLiked, likesCount = newCount)
                } else post
            }
            state.copy(
                posts = updatedPosts,
                selectedPostForComments = if (state.selectedPostForComments?.id == postId) {
                    updatedPosts.find { it.id == postId }
                } else state.selectedPostForComments
            )
        }
    }

    fun toggleSavePost(postId: Long) {
        _uiState.update { state ->
            state.copy(
                posts = state.posts.map {
                    if (it.id == postId) it.copy(isSaved = !it.isSaved) else it
                }
            )
        }
    }

    fun toggleSaveReel(reelId: Long) {
        _uiState.update { state ->
            state.copy(
                reels = state.reels.map {
                    if (it.id == reelId) it.copy(isSaved = !it.isSaved) else it
                }
            )
        }
    }

    fun removeSavedItem(itemId: Long) {
        _uiState.update { state ->
            state.copy(
                posts = state.posts.map {
                    if (it.id == itemId) it.copy(isSaved = false) else it
                },
                reels = state.reels.map {
                    if (it.id == itemId) it.copy(isSaved = false) else it
                }
            )
        }
    }

    fun toggleFollowUser(userId: Long) {
        _uiState.update { state ->
            val updatedUsers = state.users.map { user ->
                if (user.id == userId) {
                    val newFollowing = !user.isFollowing
                    val newCount = if (newFollowing) user.followersCount + 1 else (user.followersCount - 1).coerceAtLeast(0)
                    user.copy(isFollowing = newFollowing, followersCount = newCount)
                } else user
            }
            state.copy(users = updatedUsers)
        }
    }

    fun addComment(postId: Long, commentText: String) {
        if (commentText.isBlank()) return
        val newComment = Comment(
            id = System.currentTimeMillis(),
            author = _uiState.value.currentUser,
            text = commentText.trim(),
            timestamp = "Just now"
        )
        _uiState.update { state ->
            val updatedPosts = state.posts.map { post ->
                if (post.id == postId) {
                    post.copy(
                        comments = listOf(newComment) + post.comments,
                        commentsCount = post.commentsCount + 1
                    )
                } else post
            }
            state.copy(
                posts = updatedPosts,
                selectedPostForComments = updatedPosts.find { it.id == postId }
            )
        }
    }

    fun createPost(content: String, mediaUrl: String? = null, feeling: String? = null) {
        if (content.isBlank() && mediaUrl.isNullOrBlank()) return
        val newPost = Post(
            id = System.currentTimeMillis(),
            author = _uiState.value.currentUser,
            content = content.trim(),
            mediaUrl = mediaUrl,
            timestamp = "Just now",
            feeling = feeling,
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0
        )
        _uiState.update { state ->
            state.copy(
                posts = listOf(newPost) + state.posts,
                showCreatePostDialog = false
            )
        }
    }

    fun sharePost(postId: Long) {
        _uiState.update { state ->
            val updatedPosts = state.posts.map { post ->
                if (post.id == postId) {
                    post.copy(sharesCount = post.sharesCount + 1)
                } else post
            }
            state.copy(posts = updatedPosts)
        }
    }

    fun toggleLikeReel(reelId: Long) {
        _uiState.update { state ->
            state.copy(
                reels = state.reels.map { reel ->
                    if (reel.id == reelId) {
                        val newLiked = !reel.isLiked
                        val newLikes = if (newLiked) reel.likesCount + 1 else (reel.likesCount - 1).coerceAtLeast(0)
                        reel.copy(isLiked = newLiked, likesCount = newLikes)
                    } else reel
                }
            )
        }
    }

    fun toggleSaveProduct(productId: Long) {
        _uiState.update { state ->
            state.copy(
                products = state.products.map {
                    if (it.id == productId) it.copy(isSaved = !it.isSaved) else it
                }
            )
        }
    }

    fun createProduct(
        title: String,
        price: Double,
        discountPrice: Double? = null,
        category: String,
        location: String,
        country: String = "TZ",
        description: String,
        imageUrl: String,
        images: List<String> = emptyList(),
        phone: String = ""
    ) {
        val allImages = if (images.isNotEmpty()) images else if (imageUrl.isNotBlank()) listOf(imageUrl) else listOf("https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=800")
        val newProduct = Product(
            id = System.currentTimeMillis(),
            title = title,
            price = price,
            discountPrice = discountPrice,
            category = category,
            location = location,
            country = country,
            currency = if (country.equals("TZ", ignoreCase = true)) "TZS" else "USD",
            imageUrl = allImages.first(),
            images = allImages,
            description = description,
            seller = _uiState.value.currentUser,
            phoneNumber = phone.ifBlank { "+255 712 345 678" },
            isFresh = true
        )

        _uiState.update { state ->
            state.copy(
                products = listOf(newProduct) + state.products,
                showCreateProductDialog = false
            )
        }

        // Asynchronously post to live API
        viewModelScope.launch {
            MarketplaceApiService.publishProduct(newProduct)
        }
    }

    fun toggleInterestedEvent(eventId: Long) {
        _uiState.update { state ->
            state.copy(
                events = state.events.map { event ->
                    if (event.id == eventId) {
                        val isInt = !event.isInterested
                        val newInterestedCount = if (isInt) event.interestedCount + 1 else (event.interestedCount - 1).coerceAtLeast(0)
                        event.copy(isInterested = isInt, interestedCount = newInterestedCount)
                    } else event
                }
            )
        }
    }

    fun toggleGoingEvent(eventId: Long) {
        _uiState.update { state ->
            state.copy(
                events = state.events.map { event ->
                    if (event.id == eventId) {
                        val isGoing = !event.isGoing
                        val newGoingCount = if (isGoing) event.goingCount + 1 else (event.goingCount - 1).coerceAtLeast(0)
                        event.copy(isGoing = isGoing, goingCount = newGoingCount)
                    } else event
                }
            )
        }
    }

    fun createEvent(title: String, date: String, location: String, description: String, imageUrl: String) {
        val newEvent = Event(
            id = System.currentTimeMillis(),
            title = title,
            date = date,
            location = location,
            description = description,
            imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1511795409834-ef04bbd61622?w=800" },
            organizer = _uiState.value.currentUser
        )
        _uiState.update { state ->
            state.copy(
                events = listOf(newEvent) + state.events,
                showCreateEventDialog = false
            )
        }
    }

    fun openConversation(convId: String) {
        _uiState.update { state ->
            val updated = state.conversations.map {
                if (it.id == convId) it.copy(unreadCount = 0) else it
            }
            state.copy(
                conversations = updated,
                activeConversationId = convId
            )
        }
    }

    fun closeConversation() {
        _uiState.update { it.copy(activeConversationId = null) }
    }

    fun sendMessage(convId: String, text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            senderId = _uiState.value.currentUser.id,
            text = text.trim(),
            timestamp = "Just now",
            isMine = true
        )
        _uiState.update { state ->
            val updated = state.conversations.map { conv ->
                if (conv.id == convId) {
                    conv.copy(
                        lastMessage = text.trim(),
                        lastTimestamp = "Just now",
                        messages = conv.messages + newMsg
                    )
                } else conv
            }
            state.copy(conversations = updated)
        }
    }

    fun startConversationWithUser(user: User, product: Product? = null) {
        val existing = _uiState.value.conversations.find { it.participant.id == user.id }
        val introMessage = if (product != null) "Hi, is this still available: ${product.title}?" else null
        
        if (existing != null) {
            if (introMessage != null) {
                sendMessage(existing.id, introMessage)
            }
            openConversation(existing.id)
            setScreen(Screen.MESSAGES)
        } else {
            val initialMsgs = if (introMessage != null) {
                listOf(
                    ChatMessage(
                        id = System.currentTimeMillis().toString(),
                        senderId = _uiState.value.currentUser.id,
                        text = introMessage,
                        timestamp = "Just now",
                        isMine = true
                    )
                )
            } else emptyList()

            val newConv = Conversation(
                id = "c_${System.currentTimeMillis()}",
                participant = user,
                lastMessage = introMessage ?: "Started a conversation",
                lastTimestamp = "Just now",
                unreadCount = 0,
                messages = initialMsgs
            )
            _uiState.update { state ->
                state.copy(
                    conversations = listOf(newConv) + state.conversations,
                    activeConversationId = newConv.id,
                    currentScreen = Screen.MESSAGES,
                    selectedProductForDetail = null
                )
            }
        }
    }

    fun markNotificationsAsRead() {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map { it.copy(isRead = true) }
            )
        }
    }

    fun updateBio(newBio: String) {
        _uiState.update { state ->
            val updatedUser = state.currentUser.copy(bio = newBio.trim())
            state.copy(
                currentUser = updatedUser,
                selectedUserProfile = if (state.selectedUserProfile?.id == updatedUser.id) updatedUser else state.selectedUserProfile
            )
        }
    }

    fun updateUserDetails(
        bio: String,
        work: String,
        education: String,
        location: String,
        website: String,
        coverImageUrl: String? = null,
        profileImageUrl: String? = null
    ) {
        _uiState.update { state ->
            val updatedUser = state.currentUser.copy(
                bio = bio.trim(),
                work = work.trim(),
                education = education.trim(),
                location = location.trim(),
                website = website.trim(),
                coverImageUrl = coverImageUrl ?: state.currentUser.coverImageUrl,
                profileImageUrl = profileImageUrl ?: state.currentUser.profileImageUrl
            )
            state.copy(
                currentUser = updatedUser,
                selectedUserProfile = if (state.selectedUserProfile?.id == updatedUser.id) updatedUser else state.selectedUserProfile
            )
        }
    }

    fun openUserProfile(user: User) {
        _uiState.update { state ->
            state.copy(
                selectedUserProfile = user,
                currentScreen = Screen.PROFILE
            )
        }
    }

    fun openCurrentUserProfile() {
        _uiState.update { state ->
            state.copy(
                selectedUserProfile = state.currentUser,
                currentScreen = Screen.PROFILE
            )
        }
    }

    fun toggleVerifyUser(userId: Long) {
        _uiState.update { state ->
            val updatedUsers = state.users.map { u ->
                if (u.id == userId) u.copy(isVerified = !u.isVerified) else u
            }
            val updatedCurrent = if (state.currentUser.id == userId) state.currentUser.copy(isVerified = !state.currentUser.isVerified) else state.currentUser
            val updatedSelected = if (state.selectedUserProfile?.id == userId) state.selectedUserProfile.copy(isVerified = !state.selectedUserProfile.isVerified) else state.selectedUserProfile
            state.copy(users = updatedUsers, currentUser = updatedCurrent, selectedUserProfile = updatedSelected)
        }
    }

    fun suspendUser(userId: Long, duration: String, reason: String) {
        val until = when (duration) {
            "week" -> "In 7 days"
            "month" -> "In 30 days"
            else -> "Indefinitely"
        }
        _uiState.update { state ->
            val updatedUsers = state.users.map { u ->
                if (u.id == userId) u.copy(suspendedUntil = until, suspensionReason = reason) else u
            }
            val updatedSelected = if (state.selectedUserProfile?.id == userId) state.selectedUserProfile.copy(suspendedUntil = until, suspensionReason = reason) else state.selectedUserProfile
            state.copy(users = updatedUsers, selectedUserProfile = updatedSelected)
        }
    }

    fun unsuspendUser(userId: Long) {
        _uiState.update { state ->
            val updatedUsers = state.users.map { u ->
                if (u.id == userId) u.copy(suspendedUntil = null, suspensionReason = null) else u
            }
            val updatedSelected = if (state.selectedUserProfile?.id == userId) state.selectedUserProfile.copy(suspendedUntil = null, suspensionReason = null) else state.selectedUserProfile
            state.copy(users = updatedUsers, selectedUserProfile = updatedSelected)
        }
    }

    fun toggleModeratorRole(userId: Long) {
        _uiState.update { state ->
            val updatedUsers = state.users.map { u ->
                if (u.id == userId) {
                    val nextRole = if (u.role == "moderator") "user" else "moderator"
                    u.copy(role = nextRole)
                } else u
            }
            val updatedSelected = if (state.selectedUserProfile?.id == userId) {
                val nextRole = if (state.selectedUserProfile.role == "moderator") "user" else "moderator"
                state.selectedUserProfile.copy(role = nextRole)
            } else state.selectedUserProfile
            state.copy(users = updatedUsers, selectedUserProfile = updatedSelected)
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setShowCreatePostDialog(show: Boolean) {
        _uiState.update { it.copy(showCreatePostDialog = show) }
    }

    fun setSelectedPostForComments(post: Post?) {
        _uiState.update { it.copy(selectedPostForComments = post) }
    }

    fun setShowSearchDialog(show: Boolean) {
        _uiState.update { it.copy(showSearchDialog = show) }
    }

    fun setShowNotificationsOverlay(show: Boolean) {
        _uiState.update { it.copy(showNotificationsOverlay = show) }
        if (show) markNotificationsAsRead()
    }

    fun setShowCreateEventDialog(show: Boolean) {
        _uiState.update { it.copy(showCreateEventDialog = show) }
    }

    fun setShowCreateProductDialog(show: Boolean) {
        _uiState.update { it.copy(showCreateProductDialog = show) }
    }

    fun setSelectedProductForDetail(product: Product?) {
        _uiState.update { it.copy(selectedProductForDetail = product) }
    }

    fun setSelectedStory(story: Story?) {
        _uiState.update { it.copy(selectedStory = story) }
    }

    fun setShowCreateStoryDialog(show: Boolean) {
        _uiState.update { it.copy(showCreateStoryDialog = show) }
    }

    fun createStory(
        mediaUrl: String,
        type: String = "image",
        textContent: String = "",
        backgroundStyle: String = "linear-gradient(45deg, #1877F2, #0055FF)",
        musicTitle: String? = null
    ) {
        val newStory = Story(
            id = System.currentTimeMillis(),
            author = _uiState.value.currentUser,
            mediaUrl = mediaUrl,
            timestamp = "Just now",
            isViewed = false,
            type = type,
            textContent = textContent,
            backgroundStyle = backgroundStyle,
            musicTitle = musicTitle,
            viewsCount = 1,
            reactionsCount = 0,
            commentsCount = 0,
            sharesCount = 0
        )
        _uiState.update { state ->
            state.copy(
                stories = listOf(newStory) + state.stories,
                showCreateStoryDialog = false
            )
        }
    }

    fun deleteStory(storyId: Long) {
        _uiState.update { state ->
            val updated = state.stories.filter { it.id != storyId }
            state.copy(
                stories = updated,
                selectedStory = if (state.selectedStory?.id == storyId) null else state.selectedStory
            )
        }
    }

    fun reactToStory(storyId: Long, reaction: String) {
        _uiState.update { state ->
            val updated = state.stories.map { s ->
                if (s.id == storyId) {
                    val isSame = s.myReaction == reaction
                    s.copy(
                        myReaction = if (isSame) null else reaction,
                        reactionsCount = if (isSame) maxOf(0, s.reactionsCount - 1) else if (s.myReaction == null) s.reactionsCount + 1 else s.reactionsCount
                    )
                } else s
            }
            state.copy(
                stories = updated,
                selectedStory = state.selectedStory?.let { sel ->
                    if (sel.id == storyId) {
                        val isSame = sel.myReaction == reaction
                        sel.copy(
                            myReaction = if (isSame) null else reaction,
                            reactionsCount = if (isSame) maxOf(0, sel.reactionsCount - 1) else if (sel.myReaction == null) sel.reactionsCount + 1 else sel.reactionsCount
                        )
                    } else sel
                }
            )
        }
    }

    fun setShowMenuOverlay(show: Boolean) {
        _uiState.update { it.copy(showMenuOverlay = show) }
    }
}
