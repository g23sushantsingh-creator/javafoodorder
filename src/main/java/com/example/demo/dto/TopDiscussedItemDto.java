package com.example.demo.dto;

public class TopDiscussedItemDto {

    private Long foodItemId;
    private String name;
    private String category;
    private String imageUrl;
    private long reviewCount;
    private double averageRating;

    public TopDiscussedItemDto() {
    }

    public TopDiscussedItemDto(Long foodItemId, String name, String category, String imageUrl,
                               long reviewCount, double averageRating) {
        this.foodItemId = foodItemId;
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
        this.reviewCount = reviewCount;
        this.averageRating = averageRating;
    }

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
