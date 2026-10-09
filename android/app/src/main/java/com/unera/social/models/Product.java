package com.unera.social.models;

public class Product {
    public String id;
    public String title;
    public String price;
    public String location;
    public String imageUrl;
    public String description;

    public Product(String id, String title, String price, String location, String imageUrl, String description) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.location = location;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
