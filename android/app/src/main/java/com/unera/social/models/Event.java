package com.unera.social.models;

public class Event {
    public String id;
    public String title;
    public String date;
    public String location;
    public String imageUrl;
    public int interestedCount;
    public boolean isInterested;

    public Event(String id, String title, String date, String location, String imageUrl, int interestedCount) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.location = location;
        this.imageUrl = imageUrl;
        this.interestedCount = interestedCount;
        this.isInterested = false;
    }
}
