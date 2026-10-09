package com.unera.social.models;

public class Post {
    public String id;
    public String authorName;
    public String authorAvatar;
    public String timestamp;
    public String content;
    public String imageUrl;
    public int likesCount;
    public int commentsCount;
    public boolean isLiked;

    public Post(String id, String authorName, String authorAvatar, String timestamp, String content, String imageUrl, int likesCount, int commentsCount) {
        this.id = id;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.timestamp = timestamp;
        this.content = content;
        this.imageUrl = imageUrl;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.isLiked = false;
    }
}
