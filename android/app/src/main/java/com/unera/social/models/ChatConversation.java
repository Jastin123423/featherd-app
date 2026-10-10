package com.unera.social.models;

public class ChatConversation {
    public String id;
    public String name;
    public String avatarUrl;
    public String lastMessage;
    public String time;
    public boolean isOnline;

    public ChatConversation(String id, String name, String avatarUrl, String lastMessage, String time, boolean isOnline) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.lastMessage = lastMessage;
        this.time = time;
        this.isOnline = isOnline;
    }
}
