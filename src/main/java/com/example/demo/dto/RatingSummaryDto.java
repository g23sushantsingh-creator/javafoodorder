package com.example.demo.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RatingSummaryDto {

    private double averageRating;
    private long totalReviews;
    private long star5Count;
    private long star4Count;
    private long star3Count;
    private long star2Count;
    private long star1Count;

    public RatingSummaryDto() {
    }

    public RatingSummaryDto(double averageRating, long totalReviews, long star5Count,
                            long star4Count, long star3Count, long star2Count, long star1Count) {
        this.averageRating = BigDecimal.valueOf(averageRating).setScale(1, RoundingMode.HALF_UP).doubleValue();
        this.totalReviews = totalReviews;
        this.star5Count = star5Count;
        this.star4Count = star4Count;
        this.star3Count = star3Count;
        this.star2Count = star2Count;
        this.star1Count = star1Count;
    }

    public int getStarPercentage(long count) {
        if (totalReviews == 0) {
            return 0;
        }
        return (int) Math.round(((double) count / totalReviews) * 100);
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(long totalReviews) {
        this.totalReviews = totalReviews;
    }

    public long getStar5Count() {
        return star5Count;
    }

    public void setStar5Count(long star5Count) {
        this.star5Count = star5Count;
    }

    public long getStar4Count() {
        return star4Count;
    }

    public void setStar4Count(long star4Count) {
        this.star4Count = star4Count;
    }

    public long getStar3Count() {
        return star3Count;
    }

    public void setStar3Count(long star3Count) {
        this.star3Count = star3Count;
    }

    public long getStar2Count() {
        return star2Count;
    }

    public void setStar2Count(long star2Count) {
        this.star2Count = star2Count;
    }

    public long getStar1Count() {
        return star1Count;
    }

    public void setStar1Count(long star1Count) {
        this.star1Count = star1Count;
    }
}
