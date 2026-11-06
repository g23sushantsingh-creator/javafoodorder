package com.example.demo.controller;

import com.example.demo.dto.FeedbackFormDto;
import com.example.demo.dto.RatingSummaryDto;
import com.example.demo.model.Feedback;
import com.example.demo.model.FoodItem;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.FoodItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FoodFeedbackController {

    private final FoodItemService foodItemService;
    private final FeedbackService feedbackService;

    public FoodFeedbackController(FoodItemService foodItemService, FeedbackService feedbackService) {
        this.foodItemService = foodItemService;
        this.feedbackService = feedbackService;
    }

    /**
     * Home Page: Displays all food items with cards, prices, and average ratings.
     */
    @GetMapping({"/", "/foods"})
    public String viewHomePage(Model model) {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        model.addAttribute("foodItems", foodItems);
        return "index";
    }

    /**
     * Food Details & Feedback Page:
     * Shows detailed info about a single food item, its feedback history, and submission form.
     */
    @GetMapping("/food/{id}")
    public String viewFoodDetails(@PathVariable("id") Long id, Model model) {
        FoodItem foodItem = foodItemService.getFoodItemById(id);
        List<Feedback> feedbacks = feedbackService.getFeedbacksByFoodItem(id);
        RatingSummaryDto ratingSummary = foodItemService.getRatingSummary(id);

        model.addAttribute("foodItem", foodItem);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("ratingSummary", ratingSummary);

        if (!model.containsAttribute("feedbackForm")) {
            // Rating is left empty on purpose: the user must actively choose 1-5 stars.
            model.addAttribute("feedbackForm", new FeedbackFormDto());
        }

        return "food-details";
    }

    /**
     * Handles feedback submission with validation.
     */
    @PostMapping("/food/{id}/feedback")
    public String submitFeedback(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("feedbackForm") FeedbackFormDto formDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        FoodItem foodItem = foodItemService.getFoodItemById(id);

        if (bindingResult.hasErrors()) {
            List<Feedback> feedbacks = feedbackService.getFeedbacksByFoodItem(id);
            RatingSummaryDto ratingSummary = foodItemService.getRatingSummary(id);

            model.addAttribute("foodItem", foodItem);
            model.addAttribute("feedbacks", feedbacks);
            model.addAttribute("ratingSummary", ratingSummary);
            model.addAttribute("errorMessage", "Please fix the errors in your feedback submission.");
            return "food-details";
        }

        feedbackService.submitFeedback(id, formDto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Thank you! Your feedback for \"" + foodItem.getName() + "\" has been submitted successfully.");

        return "redirect:/food/" + id;
    }

    /**
     * An invalid food ID (e.g. /food/9999) used to produce an ugly 500 error page.
     * Now the user is sent back to the menu with a clear message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleInvalidFoodItem(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/";
    }
}
