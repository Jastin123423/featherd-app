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
    STORY_FEED,
    MUSIC
}

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    EVENT,
    MESSAGE,
    REACTION,
    SHARE,
    GROUP_INVITE,
    BIRTHDAY,
    MENTION,
    MUSIC
}

data class Group(
    val id: Long,
    val name: String,
    val description: String = "",
    val coverImageUrl: String = "",
    val membersCount: Int = 0,
    val isMember: Boolean = false,
    val isPrivate: Boolean = false
)

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
    val organizer: User,
    val time: String = "",
    val visibility: String = "worldwide",
    val groupId: Long? = null,
    val groupName: String? = null,
    val createdAt: String = "Just now"
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
    val timeAgo: String = "Just now",
    val type: NotificationType = NotificationType.LIKE,
    val isRead: Boolean = false,
    val rawType: String = "",
    val reactionType: String = "",
    val entityType: String = "",
    val entityId: Long? = null,
    val groupId: Long? = null,
    val groupName: String? = null,
    val inviteId: Long? = null,
    val previewText: String? = null,
    val previewImage: String? = null,
    val actorsCount: Int = 1,
    val actors: List<User> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class SongStats(
    val plays: Long = 0,
    val likes: Long = 0,
    val shares: Long = 0,
    val downloads: Long = 0,
    val reelsUse: Long = 0
)

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val cover: String = "https://media.unera.social/task_01kftb3024ed7bm84gy6j485fh_1769336848_img_0.webp",
    val audioUrl: String = "",
    val duration: String = "3:24",
    val durationSeconds: Int = 204,
    val uploaderId: Long = 1,
    val uploadDate: String = "2026-03-15T12:00:00Z",
    val genre: String = "Afrobeats",
    val album: String = "Single",
    val isVerified: Boolean = false,
    val stats: SongStats = SongStats()
)

data class AudioTrack(
    val id: String,
    val title: String,
    val artist: String,
    val duration: Int = 180,
    val url: String = "",
    val uploaderId: Long = 1,
    val cover: String = "https://media.unera.social/task_01kftb3024ed7bm84gy6j485fh_1769336848_img_0.webp",
    val type: String = "music",
    val isVerified: Boolean = false,
    val likesCount: Long = 0
)

data class Album(
    val name: String,
    val songs: List<Song>,
    val cover: String,
    val artist: String,
    val totalTracks: Int
)

data class SongComment(
    val id: Long,
    val songId: String,
    val userId: Long,
    val userName: String,
    val userAvatar: String,
    val text: String,
    val createdAt: Long = System.currentTimeMillis(),
    val likesCount: Long = 0,
    val isHidden: Boolean = false,
    val hiddenBy: Long? = null
)
