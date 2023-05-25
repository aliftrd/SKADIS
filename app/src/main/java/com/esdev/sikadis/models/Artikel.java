package com.esdev.sikadis.models;

public class Artikel {
    private String title;

    private String slug;
    private String createdAt;
    private String thumbnail;
    private String content;

    public Artikel() {
        // Default constructor
    }

    public Artikel(String title,String slug, String createdAt, String thumbnail, String content) {
        this.title = title;
        this.slug = slug;
        this.createdAt = createdAt;
        this.thumbnail = thumbnail;
        this.content = content;
    }

    // Getter and setter methods for the attributes

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}