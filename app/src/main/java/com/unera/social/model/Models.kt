package com.unera.social.model

enum class Screen {
    FEED,
    REELS,
    MARKETPLACE,
    EVENTS,
    MESSAGES,
    PROFILE,
    DISCOVER_PEOPLE,
    BIRTHDAYS,
    MEMORIES,
    SAVED_POSTS,
    STORY_FEED
}

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    EVENT,
    MESSAGE
}

data class User(
    val id: Long,
    val name: String,
    val username: String,
    val profileImageUrl: String,
    val coverImageUrl: String = "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?w=1200",
    val bio: String = "",
    val location: String = "Dar es Salaam, Tanzania",
    val work: String = "Software Engineer at TechCorp",
    val education: String = "University of Dar es Salaam",
    val website: String = "https://unera.social",
    val followersCount: Int = 1240,
    val followingCount: Int = 412,
    val isVerified: Boolean = false,
    val isOnline: Boolean = false,
    val birthDate: String? = null,
    val isFollowing: Boolean = false,
    val role: String = "user", // "user", "creator", "moderator", "admin"
    val suspendedUntil: String? = null,
    val suspensionReason: String? = null,
    val isDeleted: Boolean = false
)

data class Comment(
    val id: Long,
    val author: User,
    val text: String,
    val timestamp: String,
    val likesCount: Int = 0,
    val isLiked: Boolean = false
)

data class Post(
    val id: Long,
    val author: User,
    val content: String,
    val mediaUrl: String? = null,
    val timestamp: String = "Just now",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val feeling: String? = null,
    val comments: List<Comment> = emptyList()
)

data class Story(
    val id: Long,
    val author: User,
    val mediaUrl: String = "",
    val timestamp: String = "2h ago",
    val isViewed: Boolean = false,
    val type: String = "image", // "image", "video", "text"
    val textContent: String = "",
    val backgroundStyle: String = "linear-gradient(45deg, #1877F2, #0055FF)",
    val musicTitle: String? = null,
    val musicUrl: String? = null,
    val viewsCount: Int = 18,
    val reactionsCount: Int = 5,
    val commentsCount: Int = 2,
    val sharesCount: Int = 1,
    val myReaction: String? = null,
    val isLiked: Boolean = false,
    val viewers: List<User> = emptyList()
)

data class Reel(
    val id: Long,
    val author: User,
    val videoUrl: String,
    val thumbnailUrl: String,
    val caption: String,
    val musicTitle: String,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int = 12,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false
)

data class SavedPostItem(
    val id: Long,
    val type: String = "normal", // "video" or "normal"
    val savedAt: Long = System.currentTimeMillis(),
    val post: Post? = null,
    val reel: Reel? = null
) {
    val author: User
        get() = post?.author ?: reel?.author ?: User(0L, "User", "user", "")

    val content: String
        get() = post?.content ?: reel?.caption ?: ""

    val mediaUrl: String?
        get() = post?.mediaUrl ?: reel?.thumbnailUrl

    val likesCount: Int
        get() = post?.likesCount ?: reel?.likesCount ?: 0

    val commentsCount: Int
        get() = post?.commentsCount ?: reel?.commentsCount ?: 0

    val sharesCount: Int
        get() = post?.sharesCount ?: reel?.sharesCount ?: 0

    val isLiked: Boolean
        get() = post?.isLiked ?: reel?.isLiked ?: false

    val isSaved: Boolean
        get() = post?.isSaved ?: reel?.isSaved ?: true
}

data class Product(
    val id: Long,
    val title: String,
    val price: Double,
    val discountPrice: Double? = null,
    val currency: String = "TZS",
    val category: String,
    val location: String,
    val country: String = "TZ",
    val imageUrl: String,
    val images: List<String> = emptyList(),
    val description: String,
    val seller: User,
    val phoneNumber: String = "+255 712 345 678",
    val isSaved: Boolean = false,
    val isSponsored: Boolean = false,
    val isFresh: Boolean = true,
    val quantity: Int = 1,
    val createdAt: String = ""
)

data class Event(
    val id: Long,
    val title: String,
    val date: String,
    val location: String,
    val imageUrl: String,
    val description: String,
    val interestedCount: Int = 0,
    val goingCount: Int = 0,
    val isInterested: Boolean = false,
    val isGoing: Boolean = false,
    val organizer: User
)

data class ChatMessage(
    val id: String,
    val senderId: Long,
    val text: String,
    val timestamp: String,
    val isMine: Boolean
)

data class Conversation(
    val id: String,
    val participant: User,
    val lastMessage: String,
    val lastTimestamp: String,
    val unreadCount: Int = 0,
    val messages: List<ChatMessage> = emptyList()
)

data class Notification(
    val id: String,
    val user: User,
    val content: String,
    val timeAgo: String,
    val type: NotificationType,
    val isRead: Boolean = false
)
