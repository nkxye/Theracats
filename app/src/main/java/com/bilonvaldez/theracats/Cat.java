package com.bilonvaldez.theracats;

public class Cat {
    private String imageUrl;
    private String imageId;
    private String createdAt;
    private String uniqueId;

    public Cat() {}

    public Cat(String imageUrl, String imageId, String createdAt) {
        this.imageUrl = imageUrl;
        this.imageId = imageId;
        this.createdAt = createdAt;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
