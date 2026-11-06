package com.example.demo.config;

import com.example.demo.model.Feedback;
import com.example.demo.model.FoodItem;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.FoodItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.AdminUser;
import com.example.demo.repository.AdminUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FoodItemRepository foodItemRepository;
    private final FeedbackRepository feedbackRepository;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(FoodItemRepository foodItemRepository,
                           FeedbackRepository feedbackRepository,
                           AdminUserRepository adminUserRepository,
                           PasswordEncoder passwordEncoder) {
        this.foodItemRepository = foodItemRepository;
        this.feedbackRepository = feedbackRepository;
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 1. Seed Default Admin User (username: admin, password: admin123)
        if (adminUserRepository.count() == 0) {
            AdminUser admin = new AdminUser(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "Cafeteria Operations Manager",
                    "ROLE_ADMIN"
            );
            adminUserRepository.save(admin);
        }
        // Seed the menu ONLY on a brand-new (empty) database.
        // (It used to run when count() < 24, which wiped ALL feedback whenever an admin deleted a dish.)
        if (foodItemRepository.count() == 0) {
            feedbackRepository.deleteAll();
            foodItemRepository.deleteAll();

            List<FoodItem> items = new ArrayList<>();

            // ==================== PIZZA & ITALIAN ====================
            FoodItem margheritaPizza = new FoodItem(
                    "Artisan Margherita Pizza",
                    "Crispy thin crust topped with slow-simmered San Marzano tomato sauce, fresh mozzarella, extra virgin olive oil, and aromatic basil leaves.",
                    299.00,
                    "Pizza & Italian",
                    "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem farmhousePizza = new FoodItem(
                    "Farmhouse Pizza",
                    "Loaded with crisp bell peppers, golden sweet corn, sliced button mushrooms, red onions, and melted mozzarella cheese.",
                    349.00,
                    "Pizza & Italian",
                    "https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem classicCheesePizza = new FoodItem(
                    "Classic Cheese Pizza",
                    "Rich and cheesy pizza layered with a double portion of gooey mozzarella cheese and tangy herb-infused tomato sauce.",
                    259.00,
                    "Pizza & Italian",
                    "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem creamyAlfredoPasta = new FoodItem(
                    "Creamy Alfredo Pasta",
                    "Al dente fettuccine pasta smothered in a rich, velvety parmesan and roasted garlic cream sauce, finished with cracked black pepper.",
                    279.00,
                    "Pizza & Italian",
                    "https://images.unsplash.com/photo-1645112411341-6c4fd023714a?auto=format&fit=crop&w=800&q=80",
                    true
            );

            // ==================== BURGERS ====================
            FoodItem smashCheeseburger = new FoodItem(
                    "Classic Smash Cheeseburger",
                    "Double grilled premium patties layered with melted sharp cheddar, caramelized onions, crisp lettuce, tomato, and house secret sauce on a brioche bun.",
                    189.00,
                    "Burgers",
                    "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem crispyChickenBurger = new FoodItem(
                    "Crispy Chicken Burger",
                    "Golden crunchy chicken fillet seasoned with peri-peri spices, topped with creamy garlic mayo, pickled gherkins, and crisp iceberg lettuce.",
                    219.00,
                    "Burgers",
                    "https://images.unsplash.com/photo-1625813506062-0aeb1d7a094b?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem veggieSupremeBurger = new FoodItem(
                    "Veggie Supreme Burger",
                    "Seasoned wholesome veggie patty with crunchy lettuce, ripe tomatoes, sliced red onions, and tangy mint mayo on a toasted sesame bun.",
                    149.00,
                    "Burgers",
                    "https://images.unsplash.com/photo-1550547660-d9450f859349?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem doubleCheeseBurger = new FoodItem(
                    "Double Cheese Burger",
                    "Double stacked grilled patties with double cheddar cheese slices, pickled relish, caramelized onions, and smoky BBQ sauce.",
                    249.00,
                    "Burgers",
                    "https://images.unsplash.com/photo-1586190848861-99aa4a171e90?auto=format&fit=crop&w=800&q=80",
                    true
            );

            // ==================== WRAPS & ROLLS ====================
            FoodItem crispyRanchWrap = new FoodItem(
                    "Crispy Ranch Chicken Wrap",
                    "Golden fried chicken breast strips wrapped in a warm flour tortilla with crunchy romaine lettuce, diced vine tomatoes, and herb ranch.",
                    179.00,
                    "Wraps & Rolls",
                    "https://images.unsplash.com/photo-1626700051175-6818013e1d4f?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem paneerTikkaWrap = new FoodItem(
                    "Paneer Tikka Wrap",
                    "Tandoor-marinated cottage cheese cubes wrapped with crunchy sliced onions, capsicum, and zesty mint coriander chutney.",
                    169.00,
                    "Wraps & Rolls",
                    "/images/paneer-tikka-wrap.jpg",
                    true
            );

            FoodItem mexicanVeggieRoll = new FoodItem(
                    "Mexican Veggie Roll",
                    "Savory filling of spiced red kidney beans, sweet corn, salsa, pickled jalapenos, and shredded cheese wrapped in a toasted tortilla.",
                    159.00,
                    "Wraps & Rolls",
                    "https://images.unsplash.com/photo-1541518763669-27fef04b14ea?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem spicyChickenKathiRoll = new FoodItem(
                    "Spicy Chicken Kathi Roll",
                    "Flaky paratha rolled with spicy tossed chicken, shredded onions, fresh green chilies, and tangy chaat masala.",
                    199.00,
                    "Wraps & Rolls",
                    "https://images.unsplash.com/photo-1606471191009-63994c53433b?auto=format&fit=crop&w=800&q=80",
                    true
            );

            // ==================== INDIAN SPECIAL ====================
            FoodItem paneerButterMasala = new FoodItem(
                    "Paneer Butter Masala",
                    "Tender cubes of cottage cheese simmered in a creamy, buttery spiced cashew-tomato gravy, served with fragrant basmati cumin rice.",
                    249.00,
                    "Indian Special",
                    "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem chickenBiryani = new FoodItem(
                    "Chicken Biryani",
                    "Fragrant dum-cooked long grain basmati rice layered with juicy spiced chicken, fried onions, fresh mint, and whole aromatic spices.",
                    289.00,
                    "Indian Special",
                    "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem vegHakkaNoodles = new FoodItem(
                    "Veg Hakka Noodles",
                    "Wok-tossed noodles with shredded cabbage, carrots, bell peppers, scallions, and light soy sauce seasoning.",
                    169.00,
                    "Indian Special",
                    "https://images.unsplash.com/photo-1585032226651-759b368d7246?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem masalaDosa = new FoodItem(
                    "Masala Dosa",
                    "Golden crispy fermented crepe filled with spiced potato masala, served with traditional coconut chutney and piping hot sambar.",
                    129.00,
                    "Indian Special",
                    "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?auto=format&fit=crop&w=800&q=80",
                    true
            );

            // ==================== DESSERTS ====================
            FoodItem chocolateBrownie = new FoodItem(
                    "Chocolate Brownie",
                    "Rich, warm fudgy dark chocolate brownie packed with melted chocolate chunks and crunchy roasted walnuts.",
                    119.00,
                    "Desserts",
                    "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem classicCheesecake = new FoodItem(
                    "Classic Cheesecake",
                    "Silky smooth New York style baked cream cheesecake with a buttery graham cracker crust and sweet strawberry glaze.",
                    199.00,
                    "Desserts",
                    "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem chocolateLavaCake = new FoodItem(
                    "Chocolate Lava Cake",
                    "Decadent individual warm chocolate cake with a gooey, molten chocolate fudge center, dusted with icing sugar.",
                    149.00,
                    "Desserts",
                    "https://images.unsplash.com/photo-1624353365286-3f8d62daad51?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem vanillaIceCream = new FoodItem(
                    "Vanilla Ice Cream",
                    "Double scoop of rich, creamy real Madagascar bourbon vanilla bean ice cream served with crisp chocolate wafer sticks.",
                    89.00,
                    "Desserts",
                    "https://images.unsplash.com/photo-1570197788417-0e82375c9371?auto=format&fit=crop&w=800&q=80",
                    true
            );

            // ==================== HEALTHY ====================
            FoodItem freshVegSalad = new FoodItem(
                    "Fresh Vegetable Salad",
                    "Crisp garden greens, English cucumber, cherry tomatoes, black olives, and bell peppers with a zesty lemon-herb dressing.",
                    139.00,
                    "Healthy",
                    "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem grilledChickenBowl = new FoodItem(
                    "Grilled Chicken Bowl",
                    "Herb-marinated lean grilled chicken breast slices served over brown rice, steamed broccoli, and avocado lime dressing.",
                    269.00,
                    "Healthy",
                    "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem fruitYogurtBowl = new FoodItem(
                    "Fruit Yogurt Bowl",
                    "Chilled creamy Greek yogurt topped with seasonal fruits, crunchy honey-almond granola, chia seeds, and fresh berries.",
                    149.00,
                    "Healthy",
                    "https://images.unsplash.com/photo-1590301157890-4810ed352733?auto=format&fit=crop&w=800&q=80",
                    true
            );

            FoodItem grilledPaneerBowl = new FoodItem(
                    "Grilled Paneer Bowl",
                    "Herb-spiced grilled cottage cheese steaks served with quinoa, roasted zucchini, bell peppers, and mint tahini drizzle.",
                    229.00,
                    "Healthy",
                    "https://images.unsplash.com/photo-1540420773420-3366772f4999?auto=format&fit=crop&w=800&q=80",
                    true
            );

            items.addAll(List.of(
                    margheritaPizza, farmhousePizza, classicCheesePizza, creamyAlfredoPasta,
                    smashCheeseburger, crispyChickenBurger, veggieSupremeBurger, doubleCheeseBurger,
                    crispyRanchWrap, paneerTikkaWrap, mexicanVeggieRoll, spicyChickenKathiRoll,
                    paneerButterMasala, chickenBiryani, vegHakkaNoodles, masalaDosa,
                    chocolateBrownie, classicCheesecake, chocolateLavaCake, vanillaIceCream,
                    freshVegSalad, grilledChickenBowl, fruitYogurtBowl, grilledPaneerBowl
            ));

            foodItemRepository.saveAll(items);

            // Seed initial realistic customer feedbacks across diverse categories
            List<Feedback> sampleFeedbacks = List.of(
                    new Feedback(margheritaPizza, 5, "Rahul Sharma", "Best pizza in campus! Perfectly crispy crust and fresh cheese."),
                    new Feedback(margheritaPizza, 4, "Ananya Verma", "Really good flavor, delivered piping hot and fresh."),
                    new Feedback(farmhousePizza, 5, "Siddharth Jain", "Loved the fresh bell peppers and mushrooms. Highly recommended!"),
                    new Feedback(creamyAlfredoPasta, 5, "Sara Ali", "Super creamy sauce, tasted like restaurant quality."),
                    new Feedback(smashCheeseburger, 5, "Vikram Malhotra", "The patty was so juicy and flavorful! 10/10 recommendation."),
                    new Feedback(crispyChickenBurger, 4, "Neha Patel", "Crispy exterior and very juicy inside. Great peri-peri punch."),
                    new Feedback(paneerTikkaWrap, 5, "Karan Singhania", "Tandoori flavor was on point! Very soft paneer."),
                    new Feedback(spicyChickenKathiRoll, 5, "Pooja Hegde", "Authentic kathi roll taste with just the right amount of spice."),
                    new Feedback(paneerButterMasala, 5, "Aditya Rao", "Authentic rich gravy and very soft paneer. Great lunch option."),
                    new Feedback(chickenBiryani, 5, "Farhan Akhtar", "Aroma is unbelievable. Chicken was tender and rice was perfectly cooked."),
                    new Feedback(masalaDosa, 4, "Deepika Nair", "Crispy dosa with delicious potato masala. Sambar was fantastic."),
                    new Feedback(chocolateBrownie, 5, "Tanvi Shah", "Warm, fudgy, and loaded with walnuts. Absolute bliss!"),
                    new Feedback(chocolateLavaCake, 5, "Rohan Mehra", "The molten center was incredible. Best dessert here."),
                    new Feedback(freshVegSalad, 4, "Meera Joshi", "Very fresh and refreshing dressing. Perfect light lunch."),
                    new Feedback(grilledChickenBowl, 5, "Arjun Kapoor", "Excellent healthy meal with high protein. Great post-workout food!"),
                    new Feedback(fruitYogurtBowl, 5, "Simran Kaur", "Super healthy and delicious with plenty of fresh berries and honey.")
            );

            feedbackRepository.saveAll(sampleFeedbacks);
        }

        // One-time, non-destructive fix for databases created before the local image existed:
        // only the Paneer Tikka Wrap row is touched, and only if it still has the old/broken URL.
        for (FoodItem f : foodItemRepository.findAll()) {
            if ("Paneer Tikka Wrap".equalsIgnoreCase(f.getName())
                    && (f.getImageUrl() == null
                        || f.getImageUrl().isBlank()
                        || f.getImageUrl().contains("photo-1644783307270-202958a2d1d4"))) {
                f.setImageUrl("/images/paneer-tikka-wrap.svg");
                foodItemRepository.save(f);
            }
        }

        // Ensure low ratings exist for the "Needs Improvement" dashboard section demonstration
        if (feedbackRepository.countByRatingLessThanEqual(2) == 0 && foodItemRepository.count() >= 1) {
            List<FoodItem> all = foodItemRepository.findAll();
            for (FoodItem f : all) {
                if ("Double Cheese Burger".equalsIgnoreCase(f.getName())) {
                    feedbackRepository.save(new Feedback(f, 2, "Gaurav Sen", "Patty was quite oily and bun was a bit dry today."));
                } else if ("Veggie Supreme Burger".equalsIgnoreCase(f.getName())) {
                    feedbackRepository.save(new Feedback(f, 1, "Ravi Kumar", "Lacked seasoning and arrived lukewarm. Needs more sauce."));
                } else if ("Mexican Veggie Roll".equalsIgnoreCase(f.getName())) {
                    feedbackRepository.save(new Feedback(f, 2, "Manish Pandey", "Too many beans, tortilla was slightly soggy."));
                }
            }
        }
    }
}
