package com.unera.social.models;

public class Reel {
    public String id;
    public String creatorName;
    public String creatorAvatar;
    public String caption;
    public String mediaUrl;
    public String soundTitle;
    public int likesCount;
    public int commentsCount;
    public boolean isLiked;

    public Reel(String id, String creatorName, String creatorAvatar, String caption, String mediaUrl, String soundTitle, int likesCount, int commentsCount) {
        this.id = id;
        this.creatorName = creatorName;
        this.creatorAvatar = creatorAvatar;
        this.caption = caption;
        this.mediaUrl = mediaUrl;
        this.soundTitle = soundTitle;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.isLiked = false;
    }
}
