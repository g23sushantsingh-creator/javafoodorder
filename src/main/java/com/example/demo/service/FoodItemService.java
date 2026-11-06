package com.example.demo.service;

import com.example.demo.dto.RatingSummaryDto;
import com.example.demo.model.FoodItem;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private final FeedbackRepository feedbackRepository;

    public FoodItemService(FoodItemRepository foodItemRepository, FeedbackRepository feedbackRepository) {
        this.foodItemRepository = foodItemRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    public FoodItem getFoodItemById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found with ID: " + id));
    }

    @Transactional
    public FoodItem saveFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    public RatingSummaryDto getRatingSummary(Long foodItemId) {
        Double avgRating = feedbackRepository.calculateAverageRating(foodItemId);
        long totalCount = feedbackRepository.countByFoodItemId(foodItemId);
        long star5 = feedbackRepository.countByFoodItemIdAndRating(foodItemId, 5);
        long star4 = feedbackRepository.countByFoodItemIdAndRating(foodItemId, 4);
        long star3 = feedbackRepository.countByFoodItemIdAndRating(foodItemId, 3);
        long star2 = feedbackRepository.countByFoodItemIdAndRating(foodItemId, 2);
        long star1 = feedbackRepository.countByFoodItemIdAndRating(foodItemId, 1);

        return new RatingSummaryDto(
                avgRating != null ? avgRating : 0.0,
                totalCount,
                star5,
                star4,
                star3,
                star2,
                star1
        );
    }
}
