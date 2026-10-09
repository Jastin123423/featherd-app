package com.unera.social.models;

public class Story {
    public String id;
    public String userName;
    public String avatarUrl;
    public String mediaUrl;

    public Story(String id, String userName, String avatarUrl, String mediaUrl) {
        this.id = id;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.mediaUrl = mediaUrl;
    }
}
