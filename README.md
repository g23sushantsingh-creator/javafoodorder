# Online Food Feedback System 🍔⭐

A production-ready full-stack Java Spring Boot web application designed for cafeteria feedback management, dynamic rating calculations, customer review submissions, and administrative analytics.

---

## 📖 Table of Contents
1. [Project Overview](#1-project-overview)
2. [Tech Stack](#2-tech-stack)
3. [Architecture & Folder Structure](#3-architecture--folder-structure)
4. [Database Schema & JPA Design](#4-database-schema--jpa-design)
5. [Key Features & Upgrades](#5-key-features--upgrades)
   - [Feature 1: Admin Dashboard & Analytics](#feature-1-admin-dashboard--analytics)
   - [Feature 2: Category Navigation with Icons](#feature-2-category-navigation-with-icons)
   - [Feature 3: Interactive & Animated Star Rating System](#feature-3-interactive--animated-star-rating-system)
6. [Security & Admin Access](#6-security--admin-access)
7. [How to Run the Application](#7-how-to-run-the-application)
8. [Automated Testing & Verification](#8-automated-testing--verification)
9. [College Viva / Presentation Guide](#9-college-viva--presentation-guide)

---

## 1. Project Overview

- **Project Title:** Online Food Feedback System with Admin Analytics
- **Objective:** Enable cafeteria customers to view available dishes across multiple categories, submit ratings (1–5 stars) and detailed reviews, and immediately view dynamically calculated average ratings and breakdown charts. Provide restaurant administrators with a secure dashboard to track ratings, monitor items needing improvement, manage food item availability and catalog, and moderate feedback.
- **Data Persistence:** All food items, customer ratings, comments, and admin credentials are automatically stored and retrieved from MySQL database (`food_feedback_db`).

---

## 2. Tech Stack

| Layer | Technology | Details |
|---|---|---|
| **Language** | Java 27 / 21+ | Modern, strongly-typed object-oriented language |
| **Backend Framework** | Spring Boot | Auto-configuration, embedded Tomcat web server |
| **Security** | Spring Security & BCrypt | Role-based authorization, BCrypt password hashing, CSRF protection |
| **MVC Architecture** | Spring MVC | `@Controller`, Model, View resolution, Post-Redirect-Get (PRG) pattern |
| **ORM & Persistence** | Hibernate & Spring Data JPA | Entity mappings, repository interfaces, JPQL aggregation queries |
| **Frontend Templates** | Thymeleaf | Server-side template rendering with dynamic expressions |
| **UI & Styling** | Custom CSS, Font Awesome 6, Bootstrap 5 | Signature orange-and-white food theme, responsive cards, smooth animations |
| **Database** | MySQL 8.0 | Relational database (`food_feedback_db`) |
| **Build Tool** | Apache Maven (`mvnw`) | Maven wrapper included for reproducible execution |

---

## 3. Architecture & Folder Structure

```
c:\Wallpaper\demo\
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java        # Seeds initial 24 food items, sample feedbacks & admin user
│   │   │   │   └── SecurityConfig.java         # Spring Security routes, BCrypt encoder, CSRF & login/logout
│   │   │   ├── controller/
│   │   │   │   ├── AdminController.java        # Admin dashboard, analytics, food CRUD & feedback moderation
│   │   │   │   └── FoodFeedbackController.java # Public customer routes (home page & feedback submission)
│   │   │   ├── dto/
│   │   │   │   ├── AdminDashboardStatsDto.java # Aggregated metrics for admin overview cards
│   │   │   │   ├── FeedbackFormDto.java        # Form input data with Bean Validation
│   │   │   │   ├── FoodItemFormDto.java        # Admin food item creation & edit form DTO
│   │   │   │   ├── NeedsImprovementDto.java    # Monitor for 1-star & 2-star feedback frequency & percentage
│   │   │   │   ├── RatingSummaryDto.java       # Holds calculated averages & star counts
│   │   │   │   ├── TopDiscussedItemDto.java    # Top 3 most reviewed dishes
│   │   │   │   └── WeeklyTopDishDto.java       # Highest rated dish during current week
│   │   │   ├── model/
│   │   │   │   ├── AdminUser.java              # JPA Entity for admin_user table (BCrypt credentials)
│   │   │   │   ├── Feedback.java               # JPA Entity for feedback table (FK)
│   │   │   │   └── FoodItem.java               # JPA Entity for food_item table
│   │   │   ├── repository/
│   │   │   │   ├── AdminUserRepository.java    # Repository for looking up admin credentials
│   │   │   │   ├── FeedbackRepository.java     # Aggregation queries (AVG, weekly top dish, low ratings)
│   │   │   │   └── FoodItemRepository.java     # Spring Data JPA CRUD repository
│   │   │   ├── service/
│   │   │   │   ├── AdminService.java           # Admin business logic, metrics calculations & moderation
│   │   │   │   ├── CustomUserDetailsService.java # Spring Security user details integration
│   │   │   │   ├── FeedbackService.java        # Customer feedback submission & transaction management
│   │   │   │   └── FoodItemService.java        # Food item retrieval & rating summaries
│   │   │   └── DemoApplication.java            # Spring Boot main entry point
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/styles.css              # Custom styling, orange theme, star animations, admin layout
│   │       │   └── js/app.js                   # Category filtering, live search, interactive star rating widget
│   │       ├── templates/
│   │       │   ├── admin/
│   │       │   │   ├── analytics.html          # Detailed quality reports and ratings breakdown
│   │       │   │   ├── dashboard.html          # Overview cards, weekly top dish, top 3, needs improvement
│   │       │   │   ├── feedback-list.html      # Moderation table with dish/rating filters & delete
│   │       │   │   ├── food-form.html          # Add / Edit dish form with image preview
│   │       │   │   ├── food-list.html          # Food catalog management with availability toggles
│   │       │   │   ├── login.html              # Secure admin login form
│   │       │   │   └── sidebar.html            # Shared admin navigation sidebar
│   │       │   ├── food-details.html           # Details page: Rating form & review history
│   │       │   └── index.html                  # Home page: Category filters & food card grid
│   │       └── application.properties          # MySQL datasource and Hibernate settings
│   └── test/
│       └── java/com/example/demo/
│           ├── DemoApplicationTests.java       # Spring Boot context load test
│           └── FoodFeedbackIntegrationTests.java # 9 integration tests covering public & admin endpoints
├── pom.xml                                     # Project dependencies & plugins
├── run.bat                                     # One-click Windows batch launcher
├── run.ps1                                     # PowerShell launcher
└── mvnw.cmd / mvnw                             # Maven wrapper scripts
```

---

## 4. Database Schema & JPA Design

The MySQL database is named **`food_feedback_db`**.

### 1. `food_item` Table
- `id` (BIGINT, Primary Key, Auto-Increment)
- `name` (VARCHAR(150), Not Null)
- `description` (VARCHAR(1000))
- `price` (DOUBLE, Not Null) - Displayed in Indian Rupees (₹)
- `category` (VARCHAR(50))
- `image_url` (VARCHAR(500))
- `available` (BIT / BOOLEAN, Not Null, Default: true)

### 2. `feedback` Table
- `id` (BIGINT, Primary Key, Auto-Increment)
- `food_item_id` (BIGINT, Foreign Key referencing `food_item(id)`)
- `rating` (INT, 1 to 5, Check Constraint)
- `customer_name` (VARCHAR(100))
- `comment` (VARCHAR(1000), Not Null)
- `created_at` (DATETIME(6), Auto-populated timestamp)

### 3. `admin_user` Table
- `id` (BIGINT, Primary Key, Auto-Increment)
- `username` (VARCHAR(50), Unique, Not Null)
- `password` (VARCHAR(255), BCrypt hashed, Not Null)
- `role` (VARCHAR(30), Default: `ROLE_ADMIN`)

---

## 5. Key Features & Upgrades

### Feature 1: Admin Dashboard & Analytics (`/admin`)

- **Secure Authentication:** Protected with Spring Security session management and BCrypt password encryption. Unauthorized requests to `/admin/**` are redirected to `/admin/login`.
- **A. Overview Cards:**
  - Total Food Items
  - Total Feedback Received
  - Overall Average Rating across all cafeteria items
  - Total Low-Rated Reviews (1★ and 2★)
- **B. Highest Rated Dish This Week:**
  - Real-time calculation using feedback submitted in the rolling 7-day window.
  - Displays food image, name, average rating, and weekly review count.
- **C. Top 3 Most Discussed Items:**
  - Ranks dishes by total feedback count with visual ranking badges (#1, #2, #3).
- **D. "Needs Improvement" Monitor:**
  - Identifies dishes receiving 1-star or 2-star ratings.
  - Computes total low reviews, percentage of low reviews, and average rating.
  - Highlighted with a warning card design (`#fff3cd` / `#dc3545` accents) so chefs and managers can take immediate action.
- **E. Recent Customer Feedback:**
  - Displays the 5 most recent customer feedback submissions with customer name, dish, star rating, comment, and formatted submission date.
- **F. Food Item Management (`/admin/foods`):**
  - Add new dishes, edit existing dishes, toggle availability (instantly reflects on customer cards as "Available" or "Sold Out"), and delete dishes.
- **G. Feedback Moderation (`/admin/feedback`):**
  - Browse feedback table with filters for specific dishes or star ratings. Delete inappropriate or duplicate reviews with one click.
- **H. Detailed Analytics (`/admin/analytics`):**
  - Category-wise quality breakdown, rating distribution bars, and health indicators.

---

### Feature 2: Category Navigation with Icons

The customer navigation bar features dedicated icons for all 7 menu categories matching the signature orange-and-white theme (`#ff6b35`):

- **All Items:** `fa-solid fa-utensils`
- **Pizza & Italian:** `fa-solid fa-pizza-slice`
- **Burgers:** `fa-solid fa-burger`
- **Wraps:** `fa-solid fa-bacon`
- **Indian Special:** `fa-solid fa-bowl-rice`
- **Desserts:** `fa-solid fa-ice-cream`
- **Healthy:** `fa-solid fa-carrot`

Clicking any category instantly filters the 24 food items without reloading the page.

---

### Feature 3: Interactive & Animated Star Rating System

- **5-Star Visual Display:** Starts with 5 empty stars (`fa-regular fa-star`).
- **Dynamic Hover Highlighting:** Hovering over star 3 highlights stars 1, 2, and 3 with gold coloring (`#f59e0b`).
- **Selection Persistence:** Clicking a star locks the rating, updates the hidden form input, and highlights the stars with a soft pulsing shadow.
- **Dynamic Descriptive Text:**
  - 1 Star: *"1 Star: Very Bad - Poor quality or cold"*
  - 2 Stars: *"2 Stars: Bad - Needs significant improvement"*
  - 3 Stars: *"3 Stars: Average - Acceptable, but room to improve"*
  - 4 Stars: *"4 Stars: Good - Tasty and well-prepared"*
  - 5 Stars: *"5 Stars: Excellent! Outstanding taste and service!"*
- **Celebratory 5-Star Animation:** Selecting 5 stars triggers an animated bounce (`@keyframes starBounce`) and celebration popup (`@keyframes celebrationPop`) with celebration banner: *"🎉 Fantastic! Thank you for the 5-star love!"*.
- **Keyboard Accessible:** Fully operable using Tab and Arrow keys (`ArrowLeft`, `ArrowRight`, `Enter`, `Space`) with `role="radiogroup"` and `aria-label`.
- **Full CSRF Protection:** Seamlessly passes Spring Security CSRF tokens on form submission.

---

## 6. Security & Admin Access

### Default Admin Credentials
When the application starts for the first time, `DataInitializer.java` automatically seeds the default administrator account in MySQL:

- **Login URL:** [`http://localhost:8080/admin/login`](http://localhost:8080/admin/login)
- **Username:** `admin`
- **Password:** `admin123`

*(Password is securely stored as a BCrypt hash: `$2a$10$...` and is never exposed in frontend code).*

---

## 7. How to Run the Application

### Option 1: Using the Batch or PowerShell Script
Double-click `run.bat` or run in PowerShell:
```powershell
.\run.ps1
```

### Option 2: Using Maven Wrapper Directly
```powershell
.\mvnw.cmd spring-boot:run
```

### Option 3: In VS Code
1. Open `DemoApplication.java` (`src/main/java/com/example/demo/DemoApplication.java`).
2. Click the **Run** button above `main`.

Once started, open your browser:
- Customer Portal: 👉 **`http://localhost:8080/`**
- Admin Dashboard: 👉 **`http://localhost:8080/admin`**

---

## 8. Automated Testing & Verification

Run the full integration test suite with:
```powershell
.\mvnw.cmd test
```

### Test Coverage (10 Tests Passing):
1. `contextLoads` - Verifies Spring application context and database connectivity.
2. `testHomePage` - Tests home page loading and food card rendering.
3. `testFoodDetailsPage` - Tests details view, rating summaries, and review form.
4. `testSubmitValidFeedback` - Tests feedback validation and MySQL persistence.
5. `testSubmitInvalidFeedback` - Tests rejection of invalid ratings and blank comments.
6. `testTotalFoodItemsCount` - Verifies all 24 menu items are loaded from MySQL.
7. `testAdminUnauthorizedAccessRedirectsToLogin` - Confirms unauthenticated `/admin` requests redirect to `/admin/login`.
8. `testAdminAuthorizedAccessDashboard` - Confirms authenticated admin access to `/admin` renders dashboard with statistics.
9. `testAdminFoodListAccess` - Verifies admin food items management page.
10. `testAdminToggleAvailability` - Verifies availability status toggles accurately in MySQL.

---

## 9. College Viva / Presentation Guide

- **Q: How does Spring Security protect the admin portal?**
  *A:* In `SecurityConfig.java`, we configure a `SecurityFilterChain`. Public paths (`/`, `/food/**`, static resources, `/admin/login`) are permitted to all users (`permitAll()`), while any request under `/admin/**` requires the `ROLE_ADMIN` authority (`hasRole("ADMIN")`). Unauthenticated requests are automatically redirected to our custom `/admin/login` page.

- **Q: How are passwords secured?**
  *A:* We use `BCryptPasswordEncoder` with a work factor of 10. Passwords are never stored as plain text; BCrypt automatically generates a salt and one-way cryptographic hash.

- **Q: How does the "Needs Improvement" algorithm work?**
  *A:* `AdminService` queries `FeedbackRepository` for all feedbacks where rating is `<= 2`. It groups low ratings by food item, counts the frequency, computes the percentage of total reviews for that item that are negative, and sorts items by low rating frequency descending.

- **Q: How is the Post-Redirect-Get (PRG) pattern applied?**
  *A:* When customer feedback or admin food modifications are submitted via HTTP POST, the controller processes the database change and returns a 302 redirect (`redirect:/admin/foods` or `redirect:/food/{id}`). This prevents duplicate form submissions if the user refreshes the page.
