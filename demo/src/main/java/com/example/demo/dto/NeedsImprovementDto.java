package com.example.demo.dto;

public class NeedsImprovementDto {

    private Long foodItemId;
    private String name;
    private String category;
    private String imageUrl;
    private long lowRatingCount;
    private long totalReviewCount;
    private double lowRatingPercentage;
    private double averageRating;

    public NeedsImprovementDto() {
    }

    public NeedsImprovementDto(Long foodItemId, String name, String category, String imageUrl,
                               long lowRatingCount, long totalReviewCount,
                               double lowRatingPercentage, double averageRating) {
        this.foodItemId = foodItemId;
        this.name = name;
        this.category = category;
        this.imageUrl = imageUrl;
        this.lowRatingCount = lowRatingCount;
        this.totalReviewCount = totalReviewCount;
        this.lowRatingPercentage = Math.round(lowRatingPercentage * 10.0) / 10.0;
        this.averageRating = Math.round(averageRating * 10.0) / 10.0;
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

    public long getLowRatingCount() {
        return lowRatingCount;
    }

    public void setLowRatingCount(long lowRatingCount) {
        this.lowRatingCount = lowRatingCount;
    }

    public long getTotalReviewCount() {
        return totalReviewCount;
    }

    public void setTotalReviewCount(long totalReviewCount) {
        this.totalReviewCount = totalReviewCount;
    }

    public double getLowRatingPercentage() {
        return lowRatingPercentage;
    }

    public void setLowRatingPercentage(double lowRatingPercentage) {
        this.lowRatingPercentage = lowRatingPercentage;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
