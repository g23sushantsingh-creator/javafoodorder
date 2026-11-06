package com.example.demo.controller;

import com.example.demo.dto.AdminDashboardStatsDto;
import com.example.demo.dto.FoodItemFormDto;
import com.example.demo.model.Feedback;
import com.example.demo.model.FoodItem;
import com.example.demo.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Admin Login Page
     */
    @GetMapping("/login")
    public String adminLoginPage(@RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "logout", required = false) String logout,
                                 Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }
        return "admin/login";
    }

    /**
     * Admin Dashboard with dynamic overview cards, weekly top dish, top discussed, needs improvement, and recent reviews.
     */
    @GetMapping({"", "/", "/dashboard"})
    public String adminDashboard(Model model) {
        AdminDashboardStatsDto stats = adminService.getDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("activeNav", "dashboard");
        return "admin/dashboard";
    }

    /**
     * Food Items Management List
     */
    @GetMapping("/foods")
    public String listFoodItems(Model model) {
        List<FoodItem> foodItems = adminService.getAllFoodItems();
        model.addAttribute("foodItems", foodItems);
        model.addAttribute("activeNav", "foods");
        return "admin/food-list";
    }

    /**
     * Food Item Creation Form
     */
    @GetMapping("/foods/new")
    public String newFoodItemForm(Model model) {
        FoodItemFormDto formDto = new FoodItemFormDto();
        formDto.setAvailable(true);
        model.addAttribute("foodForm", formDto);
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "foods");
        return "admin/food-form";
    }

    /**
     * Save new Food Item
     */
    @PostMapping("/foods/create")
    public String createFoodItem(@Valid @ModelAttribute("foodForm") FoodItemFormDto formDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            model.addAttribute("activeNav", "foods");
            model.addAttribute("errorMessage", "Please correct the form errors below.");
            return "admin/food-form";
        }
        adminService.createFoodItem(formDto);
        redirectAttributes.addFlashAttribute("successMessage", "Food item \"" + formDto.getName() + "\" added successfully.");
        return "redirect:/admin/foods";
    }

    /**
     * Edit Food Item Form
     */
    @GetMapping("/foods/edit/{id}")
    public String editFoodItemForm(@PathVariable("id") Long id, Model model) {
        FoodItem item = adminService.getFoodItemById(id);
        FoodItemFormDto formDto = new FoodItemFormDto();
        formDto.setId(item.getId());
        formDto.setName(item.getName());
        formDto.setDescription(item.getDescription());
        formDto.setPrice(item.getPrice());
        formDto.setCategory(item.getCategory());
        formDto.setImageUrl(item.getImageUrl());
        formDto.setAvailable(item.isAvailable());

        model.addAttribute("foodForm", formDto);
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "foods");
        return "admin/food-form";
    }

    /**
     * Update existing Food Item
     */
    @PostMapping("/foods/edit/{id}")
    public String updateFoodItem(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("foodForm") FoodItemFormDto formDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            formDto.setId(id);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeNav", "foods");
            model.addAttribute("errorMessage", "Please correct the form errors below.");
            return "admin/food-form";
        }
        adminService.updateFoodItem(id, formDto);
        redirectAttributes.addFlashAttribute("successMessage", "Food item \"" + formDto.getName() + "\" updated successfully.");
        return "redirect:/admin/foods";
    }

    /**
     * Delete Food Item
     */
    @PostMapping("/foods/delete/{id}")
    public String deleteFoodItem(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        FoodItem item = adminService.getFoodItemById(id);
        String name = item.getName();
        adminService.deleteFoodItem(id);
        redirectAttributes.addFlashAttribute("successMessage", "Food item \"" + name + "\" and its reviews were deleted.");
        return "redirect:/admin/foods";
    }

    /**
     * Toggle Food Item Availability
     */
    @PostMapping("/foods/toggle/{id}")
    public String toggleAvailability(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean status = adminService.toggleFoodAvailability(id);
        redirectAttributes.addFlashAttribute("successMessage",
                "Dish availability updated to: " + (status ? "Available" : "Unavailable"));
        return "redirect:/admin/foods";
    }

    /**
     * Customer Feedback Management & Moderation
     */
    @GetMapping("/feedbacks")
    public String listFeedbacks(@RequestParam(value = "foodItemId", required = false) Long foodItemId,
                                @RequestParam(value = "rating", required = false) Integer rating,
                                Model model) {
        List<Feedback> feedbacks = adminService.getFilteredFeedbacks(foodItemId, rating);
        List<FoodItem> foodItems = adminService.getAllFoodItems();

        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("foodItems", foodItems);
        model.addAttribute("selectedFoodItemId", foodItemId);
        model.addAttribute("selectedRating", rating);
        model.addAttribute("activeNav", "feedbacks");
        return "admin/feedback-list";
    }

    /**
     * Delete Inappropriate Feedback
     */
    @PostMapping("/feedbacks/delete/{id}")
    public String deleteFeedback(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        adminService.deleteFeedback(id);
        redirectAttributes.addFlashAttribute("successMessage", "Customer review #" + id + " was deleted successfully.");
        return "redirect:/admin/feedbacks";
    }

    /**
     * Analytics Overview Page
     */
    @GetMapping("/analytics")
    public String analyticsPage(Model model) {
        AdminDashboardStatsDto stats = adminService.getDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("foodItems", adminService.getAllFoodItems());
        model.addAttribute("activeNav", "analytics");
        return "admin/analytics";
    }
}
