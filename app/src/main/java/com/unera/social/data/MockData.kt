package com.unera.social.data

import com.unera.social.model.*

object MockData {
    val currentUser = User(
        id = 101L,
        name = "Jastin Jovian",
        username = "jastin_jovian",
        profileImageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
        coverImageUrl = "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?w=1200",
        bio = "Tech creator & digital explorer 🌍 Building next-gen social networks. Connect, Create, Move! ✨",
        location = "Dar es Salaam, Tanzania",
        followersCount = 3840,
        followingCount = 485,
        isVerified = true,
        isOnline = true
    )

    val users = listOf(
        currentUser,
        User(
            id = 102L,
            name = "Amina Mwangi",
            username = "amina_m",
            profileImageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=400",
            coverImageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=1200",
            bio = "Visual storyteller & coffee enthusiast ☕",
            location = "Dar es Salaam, Tanzania",
            followersCount = 5200,
            followingCount = 310,
            isVerified = true,
            isOnline = true,
            birthDate = "1998-10-11",
            isFollowing = false
        ),
        User(
            id = 103L,
            name = "David Kimaro",
            username = "david_k",
            profileImageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400",
            coverImageUrl = "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?w=1200",
            bio = "Wildlife photographer & conservation advocate 🦁",
            location = "Arusha, Tanzania",
            followersCount = 14200,
            followingCount = 190,
            isVerified = true,
            isOnline = false,
            birthDate = "1995-10-11",
            isFollowing = false
        ),
        User(
            id = 104L,
            name = "Zahara Hassan",
            username = "zahara_style",
            profileImageUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400",
            coverImageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=1200",
            bio = "Fashion designer & lifestyle blogger ✨",
            location = "Dar es Salaam, Tanzania",
            followersCount = 8900,
            followingCount = 420,
            isVerified = true,
            isOnline = true,
            birthDate = "1999-10-15",
            isFollowing = false
        ),
        User(
            id = 105L,
            name = "Samuel Adebayo",
            username = "sam_ade",
            profileImageUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400",
            coverImageUrl = "https://images.unsplash.com/photo-1516251193007-45ef944ab0c6?w=1200",
            bio = "Afrobeats music producer & sound designer 🎵",
            location = "Lagos, Nigeria",
            followersCount = 21000,
            followingCount = 612,
            isVerified = true,
            isOnline = false,
            birthDate = "1997-10-22",
            isFollowing = false
        ),
        User(
            id = 106L,
            name = "Grace Ndlovu",
            username = "grace_tech",
            profileImageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400",
            coverImageUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=1200",
            bio = "Frontend Architect & UI/UX explorer 🚀",
            location = "Johannesburg, South Africa",
            followersCount = 9450,
            followingCount = 280,
            isVerified = true,
            isOnline = true,
            birthDate = "1996-11-04",
            isFollowing = false
        ),
        User(
            id = 107L,
            name = "Kwame Mensah",
            username = "kwame_creative",
            profileImageUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=400",
            coverImageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1200",
            bio = "Digital animator & 3D artist 🎨",
            location = "Accra, Ghana",
            followersCount = 11300,
            followingCount = 340,
            isVerified = true,
            isOnline = true,
            birthDate = "1998-10-18",
            isFollowing = false
        )
    )

    val stories = listOf(
        Story(
            id = 1L,
            author = currentUser,
            mediaUrl = "https://images.unsplash.com/photo-1516251193007-45ef944ab0c6?w=800",
            timestamp = "Just now",
            type = "image",
            musicTitle = "Asake - Lonely At The Top",
            viewsCount = 34,
            reactionsCount = 12,
            commentsCount = 4,
            viewers = listOf(users[1], users[2], users[3])
        ),
        Story(
            id = 2L,
            author = users[1],
            mediaUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800",
            timestamp = "2h ago",
            type = "image",
            viewsCount = 58,
            reactionsCount = 19,
            commentsCount = 6,
            viewers = listOf(currentUser, users[2], users[4])
        ),
        Story(
            id = 3L,
            author = users[2],
            mediaUrl = "",
            timestamp = "4h ago",
            type = "text",
            textContent = "Building something amazing with modern tech! Never stop shipping. 🚀✨",
            backgroundStyle = "linear-gradient(45deg, #1877F2, #0055FF)",
            musicTitle = "Burna Boy - City Boys",
            viewsCount = 82,
            reactionsCount = 27,
            commentsCount = 8,
            viewers = listOf(currentUser, users[1], users[3])
        ),
        Story(
            id = 4L,
            author = users[3],
            mediaUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=800",
            timestamp = "6h ago",
            type = "video",
            musicTitle = "Rema - Calm Down",
            viewsCount = 120,
            reactionsCount = 45,
            commentsCount = 14,
            viewers = listOf(currentUser, users[1], users[2], users[4])
        ),
        Story(
            id = 5L,
            author = users[4],
            mediaUrl = "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=800",
            timestamp = "8h ago",
            type = "image",
            viewsCount = 94,
            reactionsCount = 31,
            commentsCount = 5,
            viewers = listOf(currentUser, users[2])
        )
    )

    val posts = listOf(
        Post(
            id = 1001L,
            author = users[1],
            content = "Golden hour by the coast! There's nothing quite like the ocean breeze to recharge creative energy after a long week of work. Wishing everyone an inspired weekend! 🌊✨",
            mediaUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=800",
            timestamp = "2 hours ago",
            likesCount = 342,
            commentsCount = 28,
            sharesCount = 14,
            isLiked = false,
            isSaved = true,
            feeling = "relaxed 😌",
            comments = listOf(
                Comment(
                    id = 1L,
                    author = currentUser,
                    text = "Stunning view Amina! That lighting is unreal.",
                    timestamp = "1h ago",
                    likesCount = 8
                ),
                Comment(
                    id = 2L,
                    author = users[2],
                    text = "Pure peace. Love the tones in this shot!",
                    timestamp = "45m ago",
                    likesCount = 3
                )
            )
        ),
        Post(
            id = 1002L,
            author = users[2],
            content = "Sunrise safari in the Serengeti. Managed to capture this majestic pride resting just after dawn. Africa's wilderness never fails to leave me speechless. 🦁🌅",
            mediaUrl = "https://images.unsplash.com/photo-1516426122078-c23e76319801?w=800",
            timestamp = "5 hours ago",
            likesCount = 890,
            commentsCount = 64,
            sharesCount = 52,
            isLiked = true,
            isSaved = true,
            feeling = "grateful 🙏",
            comments = listOf(
                Comment(
                    id = 3L,
                    author = users[3],
                    text = "Incredible capture David! You have a true gift.",
                    timestamp = "3h ago",
                    likesCount = 12
                )
            )
        ),
        Post(
            id = 1003L,
            author = users[4],
            content = "Late studio session cookup! 🎹 Dropping a brand new summer sound next Friday. Who's ready for some fresh Afro-fusion rhythm? Let me know what you think in the comments! 🎧🔥",
            mediaUrl = "https://images.unsplash.com/photo-1598488035139-bdbb2231ce04?w=800",
            timestamp = "Yesterday",
            likesCount = 520,
            commentsCount = 47,
            sharesCount = 19,
            isLiked = false,
            isSaved = true,
            feeling = "excited 🚀"
        ),
        Post(
            id = 1004L,
            author = currentUser,
            content = "Proud to announce the new mobile experience of Feathered Social! Native performance, smoother feed, and instant connectivity. Let us know your feedback! 🚀⚡",
            mediaUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=800",
            timestamp = "2 days ago",
            likesCount = 1240,
            commentsCount = 115,
            sharesCount = 88,
            isLiked = true
        )
    )

    val reels = listOf(
        Reel(
            id = 201L,
            author = users[3],
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=800",
            caption = "Behind the scenes of our new summer couture collection 👗✨",
            musicTitle = "Zahara - Ocean Waves (Original)",
            likesCount = 2840,
            commentsCount = 192,
            sharesCount = 84,
            isLiked = false,
            isSaved = true
        ),
        Reel(
            id = 202L,
            author = users[2],
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1547471080-7cc2caa01a7e?w=800",
            caption = "Wild cheetah sprinting at top speed across the plains 🐆💨",
            musicTitle = "African Rhythms & Nature Sounds",
            likesCount = 9410,
            commentsCount = 630,
            sharesCount = 420,
            isLiked = true
        ),
        Reel(
            id = 203L,
            author = users[4],
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800",
            caption = "Making drums bounce in 10 seconds 🥁🔥 Listen with headphones!",
            musicTitle = "Adebayo - Beat Break Vol. 4",
            likesCount = 5120,
            commentsCount = 380,
            sharesCount = 210,
            isLiked = false
        )
    )

    val products = listOf(
        Product(
            id = 301L,
            title = "Sony Alpha a7 IV Mirrorless Camera (Like New)",
            price = 4500000.0,
            currency = "TZS",
            category = "Electronics",
            location = "Dar es Salaam, Tanzania",
            imageUrl = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800",
            description = "Barely used Sony a7 IV body with original box, extra batteries and 64GB SD card. Shutter count under 3,000.",
            seller = users[2]
        ),
        Product(
            id = 302L,
            title = "Handcrafted African Print Bomber Jacket",
            price = 95000.0,
            currency = "TZS",
            category = "Clothing",
            location = "Zanzibar, Tanzania",
            imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=800",
            description = "100% authentic Kitenge fabric handmade bomber jacket. Unisex size M & L available. Vibrant colors.",
            seller = users[3]
        ),
        Product(
            id = 303L,
            title = "Apple MacBook Pro M3 (16GB / 512GB SSD)",
            price = 3800000.0,
            currency = "TZS",
            category = "Electronics",
            location = "Nairobi, Kenya",
            imageUrl = "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800",
            description = "Space Gray MacBook Pro with M3 chip, pristine condition with 100% battery health and original 70W charger.",
            seller = currentUser
        ),
        Product(
            id = 304L,
            title = "Acoustic Solid Mahogany Guitar with Gig Bag",
            price = 350000.0,
            currency = "TZS",
            category = "Music",
            location = "Lagos, Nigeria",
            imageUrl = "https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=800",
            description = "Warm resonance, great intonation, set up with fresh D'Addario strings. Perfect for both beginners and performers.",
            seller = users[4]
        )
    )

    val events = listOf(
        Event(
            id = 401L,
            title = "East Africa Tech & Creators Summit 2026",
            date = "Oct 24, 2026 • 9:00 AM",
            location = "Julius Nyerere Int'l Convention Centre, Dar es Salaam",
            imageUrl = "https://images.unsplash.com/photo-1540575467063-178a50c2df87?w=800",
            description = "Join over 2,000 founders, creators, developers, and investors exploring the future of mobile technology and creative industries across Africa.",
            interestedCount = 450,
            goingCount = 180,
            isInterested = true,
            isGoing = false,
            organizer = currentUser
        ),
        Event(
            id = 402L,
            title = "Zanzibar Sunset Acoustic Sessions",
            date = "Nov 02, 2026 • 5:30 PM",
            location = "Kendwa Rocks Beach, Zanzibar",
            imageUrl = "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=800",
            description = "An unforgettable evening of live acoustic melodies, bonfire, and seaside vibes featuring top indie artists from around the region.",
            interestedCount = 310,
            goingCount = 95,
            isInterested = false,
            isGoing = true,
            organizer = users[3]
        ),
        Event(
            id = 403L,
            title = "Wildlife Photography Masterclass in Arusha",
            date = "Nov 15, 2026 • 8:00 AM",
            location = "Arusha National Park Gate",
            imageUrl = "https://images.unsplash.com/photo-1534567153574-2b12153a87f0?w=800",
            description = "Hands-on field workshop learning wildlife tracking, framing, fast action autofocus, and golden hour lighting techniques.",
            interestedCount = 120,
            goingCount = 40,
            isInterested = false,
            isGoing = false,
            organizer = users[2]
        )
    )

    val conversations = listOf(
        Conversation(
            id = "c1",
            participant = users[1],
            lastMessage = "Hey! Loved your latest post on Feathered!",
            lastTimestamp = "10:32 AM",
            unreadCount = 2,
            messages = listOf(
                ChatMessage("m1", users[1].id, "Hey Jastin, are you attending the summit next month?", "10:20 AM", false),
                ChatMessage("m2", currentUser.id, "Yes absolutely! Speaking on the creator panel.", "10:25 AM", true),
                ChatMessage("m3", users[1].id, "That's fantastic! Let's definitely catch up there.", "10:30 AM", false),
                ChatMessage("m4", users[1].id, "Hey! Loved your latest post on Feathered!", "10:32 AM", false)
            )
        ),
        Conversation(
            id = "c2",
            participant = users[2],
            lastMessage = "Thanks for the feedback on the camera gear!",
            lastTimestamp = "Yesterday",
            unreadCount = 0,
            messages = listOf(
                ChatMessage("m21", currentUser.id, "David, is the Sony a7 IV still available on Marketplace?", "Yesterday", true),
                ChatMessage("m22", users[2].id, "Yes it is! Body only or with the lens?", "Yesterday", false),
                ChatMessage("m23", currentUser.id, "Body only. A friend in town is interested.", "Yesterday", true),
                ChatMessage("m24", users[2].id, "Thanks for the feedback on the camera gear!", "Yesterday", false)
            )
        ),
        Conversation(
            id = "c3",
            participant = users[3],
            lastMessage = "New bomber jackets are ready for shipping! ✨",
            lastTimestamp = "Oct 08",
            unreadCount = 0,
            messages = listOf(
                ChatMessage("m31", users[3].id, "New bomber jackets are ready for shipping! ✨", "Oct 08", false)
            )
        )
    )

    val notifications = listOf(
        Notification(
            id = "n1",
            user = users[1],
            content = "liked your post: \"Proud to announce the new mobile experience...\"",
            timeAgo = "10m ago",
            type = NotificationType.LIKE,
            isRead = false
        ),
        Notification(
            id = "n2",
            user = users[2],
            content = "commented on your photo: \"Stunning view!\"",
            timeAgo = "1h ago",
            type = NotificationType.COMMENT,
            isRead = false
        ),
        Notification(
            id = "n3",
            user = users[3],
            content = "started following you.",
            timeAgo = "3h ago",
            type = NotificationType.FOLLOW,
            isRead = true
        ),
        Notification(
            id = "n4",
            user = users[4],
            content = "invited you to the event \"Zanzibar Sunset Acoustic Sessions\".",
            timeAgo = "Yesterday",
            type = NotificationType.EVENT,
            isRead = true
        )
    )
}
