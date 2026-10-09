package com.unera.social.models;

public class Comment {
    public String id;
    public String authorName;
    public String authorAvatar;
    public String text;
    public String timestamp;

    public Comment(String id, String authorName, String authorAvatar, String text, String timestamp) {
        this.id = id;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.text = text;
        this.timestamp = timestamp;
    }
}
