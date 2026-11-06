package com.example.demo.dto;

public class WeeklyTopDishDto {

    private Long foodItemId;
    private String name;
    private String imageUrl;
    private String category;
    private double averageRating;
    private long reviewCount;
    private boolean allTimeFallback;

    public WeeklyTopDishDto() {
    }

    public WeeklyTopDishDto(Long foodItemId, String name, String imageUrl, String category,
                            double averageRating, long reviewCount, boolean allTimeFallback) {
        this.foodItemId = foodItemId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.allTimeFallback = allTimeFallback;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isAllTimeFallback() {
        return allTimeFallback;
    }

    public void setAllTimeFallback(boolean allTimeFallback) {
        this.allTimeFallback = allTimeFallback;
    }
}
