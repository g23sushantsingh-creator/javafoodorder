package com.example.demo.dto;

import com.example.demo.model.Feedback;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboardStatsDto {

    private long totalFoodItems;
    private long totalFeedbacks;
    private double overallAverageRating;
    private long totalLowRatings;

    private WeeklyTopDishDto highestRatedDishThisWeek;
    private List<TopDiscussedItemDto> top3DiscussedItems = new ArrayList<>();
    private List<NeedsImprovementDto> needsImprovementItems = new ArrayList<>();
    private List<Feedback> recentFeedbacks = new ArrayList<>();

    // Feature 1: Feedback Activity (last 7 days, daily counts)
    private List<FeedbackActivityDto> feedbackActivity = new ArrayList<>();

    // Feature 2: Top 5 Best-Rated Dishes
    private List<TopRatedDishDto> top5RatedDishes = new ArrayList<>();

    public AdminDashboardStatsDto() {
    }

    public long getTotalFoodItems() {
        return totalFoodItems;
    }

    public void setTotalFoodItems(long totalFoodItems) {
        this.totalFoodItems = totalFoodItems;
    }

    public long getTotalFeedbacks() {
        return totalFeedbacks;
    }

    public void setTotalFeedbacks(long totalFeedbacks) {
        this.totalFeedbacks = totalFeedbacks;
    }

    public double getOverallAverageRating() {
        return overallAverageRating;
    }

    public void setOverallAverageRating(double overallAverageRating) {
        this.overallAverageRating = overallAverageRating;
    }

    public long getTotalLowRatings() {
        return totalLowRatings;
    }

    public void setTotalLowRatings(long totalLowRatings) {
        this.totalLowRatings = totalLowRatings;
    }

    public WeeklyTopDishDto getHighestRatedDishThisWeek() {
        return highestRatedDishThisWeek;
    }

    public void setHighestRatedDishThisWeek(WeeklyTopDishDto highestRatedDishThisWeek) {
        this.highestRatedDishThisWeek = highestRatedDishThisWeek;
    }

    public List<TopDiscussedItemDto> getTop3DiscussedItems() {
        return top3DiscussedItems;
    }

    public void setTop3DiscussedItems(List<TopDiscussedItemDto> top3DiscussedItems) {
        this.top3DiscussedItems = top3DiscussedItems;
    }

    public List<NeedsImprovementDto> getNeedsImprovementItems() {
        return needsImprovementItems;
    }

    public void setNeedsImprovementItems(List<NeedsImprovementDto> needsImprovementItems) {
        this.needsImprovementItems = needsImprovementItems;
    }

    public List<Feedback> getRecentFeedbacks() {
        return recentFeedbacks;
    }

    public void setRecentFeedbacks(List<Feedback> recentFeedbacks) {
        this.recentFeedbacks = recentFeedbacks;
    }

    public List<FeedbackActivityDto> getFeedbackActivity() {
        return feedbackActivity;
    }

    public void setFeedbackActivity(List<FeedbackActivityDto> feedbackActivity) {
        this.feedbackActivity = feedbackActivity;
    }

    public List<TopRatedDishDto> getTop5RatedDishes() {
        return top5RatedDishes;
    }

    public void setTop5RatedDishes(List<TopRatedDishDto> top5RatedDishes) {
        this.top5RatedDishes = top5RatedDishes;
    }
}
