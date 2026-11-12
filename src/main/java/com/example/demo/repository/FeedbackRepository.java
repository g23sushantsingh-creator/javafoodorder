package com.example.demo.repository;

import com.example.demo.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByFoodItemIdOrderByCreatedAtDesc(Long foodItemId);

    long countByFoodItemId(Long foodItemId);

    @Query("SELECT COALESCE(AVG(f.rating), 0.0) FROM Feedback f WHERE f.foodItem.id = :foodItemId")
    Double calculateAverageRating(@Param("foodItemId") Long foodItemId);

    long countByFoodItemIdAndRating(Long foodItemId, Integer rating);

    boolean existsByFoodItemIdAndComment(Long foodItemId, String comment);

    boolean existsByComment(String comment);

    // Queries for Admin Dashboard & Analytics
    long countByRatingLessThanEqual(Integer rating);

    List<Feedback> findTop10ByOrderByCreatedAtDesc();

    List<Feedback> findAllByOrderByCreatedAtDesc();

    List<Feedback> findByRatingOrderByCreatedAtDesc(Integer rating);

    List<Feedback> findByFoodItemIdAndRatingOrderByCreatedAtDesc(Long foodItemId, Integer rating);

    @Query("SELECT COALESCE(AVG(f.rating), 0.0) FROM Feedback f")
    Double calculateOverallAverageRating();

    @Query("SELECT f FROM Feedback f WHERE f.createdAt >= :startDate")
    List<Feedback> findFeedbacksSince(@Param("startDate") LocalDateTime startDate);
}
