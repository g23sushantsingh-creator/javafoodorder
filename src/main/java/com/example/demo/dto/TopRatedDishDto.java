package com.example.demo.dto;

/**
 * DTO representing a top-rated food item for the "Top 5 Best-Rated Dishes"
 * section on the Analytics page.
 */
public class TopRatedDishDto {

    private Long id;
    private String name;
    private String imageUrl;
    private String category;
    private double averageRating;
    private long reviewCount;
    private int rank;

    public TopRatedDishDto() {
    }

    public TopRatedDishDto(Long id, String name, String imageUrl, String category,
                           double averageRating, long reviewCount, int rank) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
