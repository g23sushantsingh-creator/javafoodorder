package com.example.demo;

import com.example.demo.model.FoodItem;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.FoodItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FoodFeedbackIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Test
    @DisplayName("1. Home page should render food items with average ratings")
    void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("foodItems"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Food Feedback System")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Available Food Items")));
    }

    @Test
    @DisplayName("2. Food details page should load item details, form and review history")
    void testFoodDetailsPage() throws Exception {
        FoodItem pizza = foodItemRepository.findAll().get(0);

        mockMvc.perform(get("/food/" + pizza.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("food-details"))
                .andExpect(model().attributeExists("foodItem", "feedbacks", "ratingSummary", "feedbackForm"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString(pizza.getName())))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Rate This Food")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("rate-star-btn")));
    }

    @Test
    @DisplayName("3. Submitting valid feedback should save to database and redirect with flash message")
    void testSubmitValidFeedback() throws Exception {
        FoodItem pizza = foodItemRepository.findAll().get(0);
        long initialFeedbackCount = feedbackRepository.countByFoodItemId(pizza.getId());

        mockMvc.perform(post("/food/" + pizza.getId() + "/feedback")
                        .with(csrf())
                        .param("rating", "5")
                        .param("customerName", "Aditi Rao")
                        .param("comment", "Absolutely delicious and hot! Loved every bite."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/food/" + pizza.getId()))
                .andExpect(flash().attributeExists("successMessage"));

        long newCount = feedbackRepository.countByFoodItemId(pizza.getId());
        assertThat(newCount).isEqualTo(initialFeedbackCount + 1);
    }

    @Test
    @DisplayName("4. Submitting invalid feedback should fail validation and re-render form")
    void testSubmitInvalidFeedback() throws Exception {
        FoodItem pizza = foodItemRepository.findAll().get(0);

        mockMvc.perform(post("/food/" + pizza.getId() + "/feedback")
                        .with(csrf())
                        .param("rating", "0") // invalid rating
                        .param("customerName", "") // blank name
                        .param("comment", "ab")) // less than 3 chars
                .andExpect(status().isOk())
                .andExpect(view().name("food-details"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("feedbackForm", "rating", "customerName", "comment"));
    }

    @Test
    @DisplayName("5. All 24 expanded food items should be loaded from MySQL database")
    void testTotalFoodItemsCount() {
        long totalItems = foodItemRepository.count();
        assertThat(totalItems).isGreaterThanOrEqualTo(24);
    }

    @Test
    @DisplayName("6. Unauthenticated access to /admin should redirect to /admin/login")
    void testAdminUnauthorizedAccessRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @Test
    @DisplayName("7. Authenticated admin access to /admin should render dashboard with metrics")
    void testAdminAuthorizedAccessDashboard() throws Exception {
        mockMvc.perform(get("/admin").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists("stats"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cafeteria Analytics Dashboard")));
    }

    @Test
    @DisplayName("8. Authenticated admin can view food items list")
    void testAdminFoodListAccess() throws Exception {
        mockMvc.perform(get("/admin/foods").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/food-list"))
                .andExpect(model().attributeExists("foodItems"));
    }

    @Test
    @DisplayName("9. Authenticated admin can toggle food availability")
    void testAdminToggleAvailability() throws Exception {
        FoodItem food = foodItemRepository.findAll().get(0);
        boolean initialStatus = food.isAvailable();

        mockMvc.perform(post("/admin/foods/toggle/" + food.getId())
                        .with(user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/foods"))
                .andExpect(flash().attributeExists("successMessage"));

        FoodItem updated = foodItemRepository.findById(food.getId()).orElseThrow();
        assertThat(updated.isAvailable()).isEqualTo(!initialStatus);
    }

    @Test
    @DisplayName("10. Submitting 1-star, 3-star, and 5-star ratings should save to MySQL and update average rating")
    void testSubmitMultipleStarRatings() throws Exception {
        FoodItem wrap = foodItemRepository.findAll().stream()
                .filter(f -> f.getName().contains("Paneer Tikka"))
                .findFirst().orElseThrow();

        long initialCount = feedbackRepository.countByFoodItemId(wrap.getId());

        // Test 1-Star Submission
        mockMvc.perform(post("/food/" + wrap.getId() + "/feedback")
                        .with(csrf())
                        .param("rating", "1")
                        .param("customerName", "Student Reviewer 1")
                        .param("comment", "Needs more seasoning and mint chutney."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/food/" + wrap.getId()));

        // Test 3-Star Submission
        mockMvc.perform(post("/food/" + wrap.getId() + "/feedback")
                        .with(csrf())
                        .param("rating", "3")
                        .param("customerName", "Student Reviewer 3")
                        .param("comment", "Average taste, decent portion size."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/food/" + wrap.getId()));

        // Test 5-Star Submission
        mockMvc.perform(post("/food/" + wrap.getId() + "/feedback")
                        .with(csrf())
                        .param("rating", "5")
                        .param("customerName", "Student Reviewer 5")
                        .param("comment", "Fantastic paneer tikka wrap, perfectly toasted!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/food/" + wrap.getId()));

        long finalCount = feedbackRepository.countByFoodItemId(wrap.getId());
        assertThat(finalCount).isEqualTo(initialCount + 3);

        // Verify that Paneer Tikka Wrap image is set to /images/paneer-tikka-wrap.jpg
        FoodItem refreshedWrap = foodItemRepository.findById(wrap.getId()).orElseThrow();
        assertThat(refreshedWrap.getImageUrl()).isEqualTo("/images/paneer-tikka-wrap.jpg");
    }

    @Test
    @DisplayName("11. Submitting feedback with empty rating should fail validation")
    void testSubmitEmptyRatingFailsValidation() throws Exception {
        FoodItem item = foodItemRepository.findAll().get(0);

        mockMvc.perform(post("/food/" + item.getId() + "/feedback")
                        .with(csrf())
                        .param("customerName", "Anonymous Student")
                        .param("comment", "Forgot to select stars"))
                .andExpect(status().isOk())
                .andExpect(view().name("food-details"))
                .andExpect(model().attributeHasFieldErrors("feedbackForm", "rating"));
    }

    @Test
    @DisplayName("12. Admin login page should render modern login card with password toggle and CSRF")
    void testAdminLoginPageRenders() throws Exception {
        mockMvc.perform(get("/admin/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Admin Portal")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("togglePasswordBtn")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("btn-admin-login")));
    }

    @Test
    @DisplayName("13. Admin login with invalid credentials should fail and display error alert")
    void testAdminLoginInvalidCredentials() throws Exception {
        mockMvc.perform(post("/admin/login")
                        .with(csrf())
                        .param("username", "admin")
                        .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login?error=true"));

        mockMvc.perform(get("/admin/login?error=true"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid username or password")));
    }

    @Test
    @DisplayName("14. Admin login with valid credentials should authenticate and redirect to dashboard")
    void testAdminLoginValidCredentials() throws Exception {
        mockMvc.perform(post("/admin/login")
                        .with(csrf())
                        .param("username", "admin")
                        .param("password", "admin123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    @DisplayName("15. Admin logout should redirect to login page with logout success alert")
    void testAdminLogout() throws Exception {
        mockMvc.perform(post("/admin/logout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login?logout=true"));

        mockMvc.perform(get("/admin/login?logout=true"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("You have been logged out successfully")));
    }
}
