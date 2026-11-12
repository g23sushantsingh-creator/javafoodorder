package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.Feedback;
import com.example.demo.model.FoodItem;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    private final FoodItemRepository foodItemRepository;
    private final FeedbackRepository feedbackRepository;

    public AdminService(FoodItemRepository foodItemRepository, FeedbackRepository feedbackRepository) {
        this.foodItemRepository = foodItemRepository;
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * Aggregates all live statistics for the Admin Dashboard dynamically from MySQL.
     */
    @Transactional(readOnly = true)
    public AdminDashboardStatsDto getDashboardStats() {
        AdminDashboardStatsDto stats = new AdminDashboardStatsDto();

        // 1. Overview Cards
        stats.setTotalFoodItems(foodItemRepository.count());
        stats.setTotalFeedbacks(feedbackRepository.count());

        Double overallAvg = feedbackRepository.calculateOverallAverageRating();
        stats.setOverallAverageRating(overallAvg != null
                ? BigDecimal.valueOf(overallAvg).setScale(1, RoundingMode.HALF_UP).doubleValue()
                : 0.0);

        stats.setTotalLowRatings(feedbackRepository.countByRatingLessThanEqual(2));

        // 2. Highest Rated Dish This Week
        stats.setHighestRatedDishThisWeek(calculateHighestRatedDishThisWeek());

        // 3. Top 3 Most Discussed Items
        stats.setTop3DiscussedItems(calculateTop3DiscussedItems());

        // 4. Needs Improvement Section (Items with 1 or 2 stars)
        stats.setNeedsImprovementItems(calculateNeedsImprovementItems());

        // 5. Recent Customer Feedback (Latest 10)
        stats.setRecentFeedbacks(feedbackRepository.findTop10ByOrderByCreatedAtDesc());

        // 6. Feedback Activity — daily counts for the last 7 days
        stats.setFeedbackActivity(calculateFeedbackActivity());

        // 7. Top 5 Best-Rated Dishes
        stats.setTop5RatedDishes(calculateTop5RatedDishes());

        return stats;
    }

    private WeeklyTopDishDto calculateHighestRatedDishThisWeek() {
        LocalDateTime startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        List<Feedback> weeklyFeedbacks = feedbackRepository.findFeedbacksSince(startOfWeek);

        boolean isFallback = false;
        if (weeklyFeedbacks.isEmpty()) {
            // Check past 7 days
            weeklyFeedbacks = feedbackRepository.findFeedbacksSince(LocalDateTime.now().minusDays(7));
        }

        if (weeklyFeedbacks.isEmpty()) {
            // Fallback to all feedbacks
            weeklyFeedbacks = feedbackRepository.findAll();
            isFallback = true;
        }

        if (weeklyFeedbacks.isEmpty()) {
            List<FoodItem> allFoods = foodItemRepository.findAll();
            if (!allFoods.isEmpty()) {
                FoodItem first = allFoods.get(0);
                return new WeeklyTopDishDto(first.getId(), first.getName(), first.getImageUrl(), first.getCategory(), 0.0, 0, true);
            }
            return new WeeklyTopDishDto(null, "No Dishes Available", "", "", 0.0, 0, true);
        }

        // Group by food item and compute average rating
        Map<FoodItem, List<Feedback>> grouped = weeklyFeedbacks.stream()
                .filter(fb -> fb != null && fb.getFoodItem() != null)
                .collect(Collectors.groupingBy(fb -> Objects.requireNonNull(fb.getFoodItem())));

        FoodItem topFood = null;
        double bestAvg = -1.0;
        long bestCount = 0;

        for (Map.Entry<FoodItem, List<Feedback>> entry : grouped.entrySet()) {
            List<Feedback> fbs = entry.getValue();
            double avg = fbs.stream()
                    .filter(fb -> fb != null && fb.getRating() != null)
                    .mapToInt(fb -> {
                        Integer rating = fb.getRating();
                        return rating != null ? rating.intValue() : 0;
                    })
                    .average()
                    .orElse(0.0);
            if (avg > bestAvg || (avg == bestAvg && fbs.size() > bestCount)) {
                bestAvg = avg;
                bestCount = fbs.size();
                topFood = entry.getKey();
            }
        }

        if (topFood != null) {
            double roundedAvg = BigDecimal.valueOf(bestAvg).setScale(1, RoundingMode.HALF_UP).doubleValue();
            return new WeeklyTopDishDto(topFood.getId(), topFood.getName(), topFood.getImageUrl(),
                    topFood.getCategory(), roundedAvg, bestCount, isFallback);
        }

        return new WeeklyTopDishDto(null, "No Data", "", "", 0.0, 0, true);
    }

    private List<TopDiscussedItemDto> calculateTop3DiscussedItems() {
        List<FoodItem> allFoods = foodItemRepository.findAll();
        return allFoods.stream()
                .sorted((a, b) -> Integer.compare(b.getFeedbackCount(), a.getFeedbackCount()))
                .limit(3)
                .map(food -> new TopDiscussedItemDto(
                        food.getId(),
                        food.getName(),
                        food.getCategory(),
                        food.getImageUrl(),
                        food.getFeedbackCount(),
                        food.getAverageRating()
                ))
                .collect(Collectors.toList());
    }

    private List<NeedsImprovementDto> calculateNeedsImprovementItems() {
        List<FoodItem> allFoods = foodItemRepository.findAll();
        List<NeedsImprovementDto> list = new ArrayList<>();

        for (FoodItem food : allFoods) {
            long star1 = feedbackRepository.countByFoodItemIdAndRating(food.getId(), 1);
            long star2 = feedbackRepository.countByFoodItemIdAndRating(food.getId(), 2);
            long lowCount = star1 + star2;

            if (lowCount > 0) {
                long total = food.getFeedbackCount();
                double pct = total > 0 ? ((double) lowCount / total) * 100.0 : 0.0;
                list.add(new NeedsImprovementDto(
                        food.getId(),
                        food.getName(),
                        food.getCategory(),
                        food.getImageUrl(),
                        lowCount,
                        total,
                        pct,
                        food.getAverageRating()
                ));
            }
        }

        // Sort primarily by low-rating frequency descending, secondarily by percentage descending
        list.sort((a, b) -> {
            int cmp = Long.compare(b.getLowRatingCount(), a.getLowRatingCount());
            return cmp != 0 ? cmp : Double.compare(b.getLowRatingPercentage(), a.getLowRatingPercentage());
        });

        return list;
    }

    // ==================== FOOD ITEM MANAGEMENT ====================

    @Transactional(readOnly = true)
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public FoodItem getFoodItemById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found with ID: " + id));
    }

    public FoodItem createFoodItem(FoodItemFormDto dto) {
        FoodItem item = new FoodItem(
                dto.getName().trim(),
                dto.getDescription().trim(),
                dto.getPrice(),
                dto.getCategory().trim(),
                dto.getImageUrl().trim(),
                dto.getAvailable() != null ? dto.getAvailable() : true
        );
        return foodItemRepository.save(item);
    }

    public FoodItem updateFoodItem(Long id, FoodItemFormDto dto) {
        FoodItem item = getFoodItemById(id);
        item.setName(dto.getName().trim());
        item.setDescription(dto.getDescription().trim());
        item.setPrice(dto.getPrice());
        item.setCategory(dto.getCategory().trim());
        item.setImageUrl(dto.getImageUrl().trim());
        item.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : true);
        return foodItemRepository.save(item);
    }

    public void deleteFoodItem(Long id) {
        FoodItem item = getFoodItemById(id);
        foodItemRepository.delete(item);
    }

    public boolean toggleFoodAvailability(Long id) {
        FoodItem item = getFoodItemById(id);
        boolean newStatus = !item.isAvailable();
        item.setAvailable(newStatus);
        foodItemRepository.save(item);
        return newStatus;
    }

    // ==================== FEEDBACK MANAGEMENT ====================

    @Transactional(readOnly = true)
    public List<Feedback> getFilteredFeedbacks(Long foodItemId, Integer rating) {
        if (foodItemId != null && rating != null) {
            return feedbackRepository.findByFoodItemIdAndRatingOrderByCreatedAtDesc(foodItemId, rating);
        } else if (foodItemId != null) {
            return feedbackRepository.findByFoodItemIdOrderByCreatedAtDesc(foodItemId);
        } else if (rating != null) {
            return feedbackRepository.findByRatingOrderByCreatedAtDesc(rating);
        } else {
            return feedbackRepository.findAllByOrderByCreatedAtDesc();
        }
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    // ==================== ANALYTICS: FEEDBACK ACTIVITY (FEATURE 1) ====================

    /**
     * Builds a list of daily feedback submission counts for the last 7 days
     * (today inclusive), including days with zero submissions so the chart
     * always shows a full 7-point dataset.
     */
    @Transactional(readOnly = true)
    public List<FeedbackActivityDto> calculateFeedbackActivity() {
        // Fetch all feedbacks from 6 days ago (start of day) up to now
        LocalDate today = LocalDate.now();
        LocalDateTime since = today.minusDays(6).atStartOfDay();
        List<Feedback> recentFeedbacks = feedbackRepository.findFeedbacksSince(since);

        // Build a map of date → count using the feedback's createdAt date
        Map<LocalDate, Long> countByDate = recentFeedbacks.stream()
                .collect(Collectors.groupingBy(
                        f -> f.getCreatedAt().toLocalDate(),
                        Collectors.counting()
                ));

        // Date formatters
        DateTimeFormatter labelFmt = DateTimeFormatter.ofPattern("MMM dd");
        DateTimeFormatter keyFmt   = DateTimeFormatter.ISO_LOCAL_DATE;

        // Build the result list covering each of the last 7 days (oldest first)
        List<FeedbackActivityDto> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            long count = countByDate.getOrDefault(date, 0L);
            result.add(new FeedbackActivityDto(
                    date.format(labelFmt),
                    date.format(keyFmt),
                    count
            ));
        }
        return result;
    }

    // ==================== ANALYTICS: TOP 5 RATED DISHES (FEATURE 2) ====================

    /**
     * Returns the top 5 food items ranked by their average customer rating.
     * Dishes with zero reviews are excluded.
     * Ties in average rating are broken by the dish with more reviews.
     * Average ratings are rounded to one decimal place.
     */
    @Transactional(readOnly = true)
    public List<TopRatedDishDto> calculateTop5RatedDishes() {
        List<FoodItem> allFoods = foodItemRepository.findAll();

        // Build a scored list, excluding items with no reviews
        List<TopRatedDishDto> scored = new ArrayList<>();
        for (FoodItem food : allFoods) {
            long count = food.getFeedbackCount();
            if (count == 0) {
                continue; // exclude unreviewed items
            }
            double avg = food.getAverageRating(); // already rounded to 1dp in the entity
            scored.add(new TopRatedDishDto(
                    food.getId(),
                    food.getName(),
                    food.getImageUrl(),
                    food.getCategory(),
                    avg,
                    count,
                    0 // rank assigned below
            ));
        }

        // Sort: descending average rating, then descending review count for ties
        scored.sort((a, b) -> {
            int cmp = Double.compare(b.getAverageRating(), a.getAverageRating());
            return cmp != 0 ? cmp : Long.compare(b.getReviewCount(), a.getReviewCount());
        });

        // Take top 5 and assign ranks 1–5
        List<TopRatedDishDto> top5 = scored.stream().limit(5).collect(Collectors.toList());
        for (int i = 0; i < top5.size(); i++) {
            top5.get(i).setRank(i + 1);
        }
        return top5;
    }
}
