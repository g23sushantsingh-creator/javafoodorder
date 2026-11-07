package com.example.demo.service;

import com.example.demo.dto.FeedbackFormDto;
import com.example.demo.model.Feedback;
import com.example.demo.model.FoodItem;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FoodItemRepository foodItemRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, FoodItemRepository foodItemRepository) {
        this.feedbackRepository = feedbackRepository;
        this.foodItemRepository = foodItemRepository;
    }

    @Transactional
    public Feedback submitFeedback(Long foodItemId, FeedbackFormDto dto) {
        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new IllegalArgumentException("Food item not found with ID: " + foodItemId));

        Feedback feedback = new Feedback();
        feedback.setFoodItem(foodItem);
        feedback.setRating(dto.getRating());
        feedback.setCustomerName(dto.getCustomerName().trim());

        // Comment is optional — store trimmed value, or empty string if blank/null
        String comment = dto.getComment();
        feedback.setComment(comment != null && !comment.trim().isEmpty() ? comment.trim() : "");

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbacksByFoodItem(Long foodItemId) {
        return feedbackRepository.findByFoodItemIdOrderByCreatedAtDesc(foodItemId);
    }

    public Double getAverageRating(Long foodItemId) {
        Double avg = feedbackRepository.calculateAverageRating(foodItemId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    public long getFeedbackCount(Long foodItemId) {
        return feedbackRepository.countByFoodItemId(foodItemId);
    }
}
