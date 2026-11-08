-- ====================================================================
-- IDEMPOTENT DATABASE MIGRATION & SEED SCRIPT
-- Project: Online Food Feedback System
-- Description:
--   1. Inserts food items for the two new categories:
--      - "Snacks & Street Food"
--      - "Tea & Beverages"
--   2. Inserts 10 realistic sample/demo reviews across diverse dishes.
-- Safe & Idempotent: Can be executed multiple times without creating
-- duplicate dishes or reviews.
-- Compatible with MySQL 8.0, TiDB, Render Managed MySQL / Clever Cloud / Railway.
-- ====================================================================

-- --------------------------------------------------------------------
-- STEP 1: Insert Food Items for New Categories (if not already present)
-- --------------------------------------------------------------------

-- 1. Samosa (Snacks & Street Food)
INSERT INTO food_item (name, description, price, category, image_url, available)
SELECT 'Samosa', 
       'Crispy golden fried triangle pastry filled with spiced potatoes, green peas, and whole coriander seeds, served with mint and sweet tamarind chutneys.', 
       30.00, 
       'Snacks & Street Food', 
       'https://images.unsplash.com/photo-1601050690597-df0568f70950?auto=format&fit=crop&w=800&q=80', 
       1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE name = 'Samosa');

-- 2. Vada Pav (Snacks & Street Food)
INSERT INTO food_item (name, description, price, category, image_url, available)
SELECT 'Vada Pav', 
       'Iconic Mumbai street burger featuring a spiced golden potato fritter tucked into a soft bun with fiery red garlic and tangy green chutneys.', 
       35.00, 
       'Snacks & Street Food', 
       'https://images.unsplash.com/photo-1626132647523-66f5bf380027?auto=format&fit=crop&w=800&q=80', 
       1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE name = 'Vada Pav');

-- 3. Kachori (Snacks & Street Food)
INSERT INTO food_item (name, description, price, category, image_url, available)
SELECT 'Kachori', 
       'Flaky, deep-fried puff pastry stuffed with a savory spiced moong dal mixture, served with tangy tamarind saunth and spicy mint dip.', 
       40.00, 
       'Snacks & Street Food', 
       'https://images.unsplash.com/photo-1606491956689-2ea866880c84?auto=format&fit=crop&w=800&q=80', 
       1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE name = 'Kachori');

-- 4. Grilled Vegetable Sandwich (Snacks & Street Food)
INSERT INTO food_item (name, description, price, category, image_url, available)
SELECT 'Grilled Vegetable Sandwich', 
       'Toasted multi-grain bread layered with sliced cucumber, tomatoes, bell peppers, melted cheddar cheese, and fresh cilantro spread.', 
       89.00, 
       'Snacks & Street Food', 
       'https://images.unsplash.com/photo-1528735602780-2552fd46c7af?auto=format&fit=crop&w=800&q=80', 
       1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE name = 'Grilled Vegetable Sandwich');

-- 5. Cutting Chai (Tea & Beverages)
INSERT INTO food_item (name, description, price, category, image_url, available)
SELECT 'Cutting Chai', 
       'Authentic roadside-style strong half-cup tea brewed with freshly crushed ginger, green cardamom, and rich boiled milk.', 
       20.00, 
       'Tea & Beverages', 
       'https://images.unsplash.com/photo-1576092768241-dec231879fc3?auto=format&fit=crop&w=800&q=80', 
       1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE name = 'Cutting Chai');

-- 6. Masala Tea (Tea & Beverages)
INSERT INTO food_item (name, description, price, category, image_url, available)
SELECT 'Masala Tea', 
       'Aromatic Assam black tea simmered with milk, ginger, cloves, cinnamon stick, and crushed green cardamom pods.', 
       30.00, 
       'Tea & Beverages', 
       'https://images.unsplash.com/photo-1544787219-7f47ccb76574?auto=format&fit=crop&w=800&q=80', 
       1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE name = 'Masala Tea');

-- 7. South Indian Filter Coffee (Tea & Beverages)
INSERT INTO food_item (name, description, price, category, image_url, available)
SELECT 'South Indian Filter Coffee', 
       'Traditional frothy filter coffee brewed from freshly roasted chicory beans and blended with hot frothed milk.', 
       45.00, 
       'Tea & Beverages', 
       'https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=800&q=80', 
       1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE name = 'South Indian Filter Coffee');

-- 8. Classic Cold Coffee (Tea & Beverages)
INSERT INTO food_item (name, description, price, category, image_url, available)
SELECT 'Classic Cold Coffee', 
       'Chilled blended espresso drink prepared with creamy milk, chocolate drizzle, and topped with rich coffee froth.', 
       79.00, 
       'Tea & Beverages', 
       'https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?auto=format&fit=crop&w=800&q=80', 
       1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE name = 'Classic Cold Coffee');

-- 9. Sweet Mango Lassi (Tea & Beverages)
INSERT INTO food_item (name, description, price, category, image_url, available)
SELECT 'Sweet Mango Lassi', 
       'Traditional thick, creamy yogurt shake infused with sweet Alphonso mango pulp, cardamom, and sliced pistachios.', 
       65.00, 
       'Tea & Beverages', 
       'https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?auto=format&fit=crop&w=800&q=80', 
       1
WHERE NOT EXISTS (SELECT 1 FROM food_item WHERE name = 'Sweet Mango Lassi');

-- --------------------------------------------------------------------
-- STEP 2: Insert 10 Sample / Demo Reviews (if not already present)
-- --------------------------------------------------------------------

-- Review 1: Samosa (5 Stars, 1 day ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 5, 'Aditi Sharma [Demo Reviewer]', 
       '[Sample Review] Crispy golden crust with a perfectly spiced potato and pea stuffing. Served piping hot with tangy tamarind chutney!', 
       DATE_SUB(NOW(), INTERVAL 1 DAY)
FROM food_item fi WHERE fi.name = 'Samosa'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Crispy golden crust with a perfectly spiced potato and pea stuffing. Served piping hot with tangy tamarind chutney!');

-- Review 2: Cutting Chai (5 Stars, 2 days ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 5, 'Kunal Verma (Sample Review)', 
       '[Sample Review] Strong, brisk cutting chai with a great ginger kick. The ideal campus refreshment during afternoon breaks.', 
       DATE_SUB(NOW(), INTERVAL 2 DAY)
FROM food_item fi WHERE fi.name = 'Cutting Chai'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Strong, brisk cutting chai with a great ginger kick. The ideal campus refreshment during afternoon breaks.');

-- Review 3: Vada Pav (4 Stars, 4 days ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 4, 'Pooja Hegde [Demo Reviewer]', 
       '[Sample Review] Authentic Mumbai flavor with spicy garlic chutney. Soft pav and freshly fried vada. Really enjoyed it.', 
       DATE_SUB(NOW(), INTERVAL 4 DAY)
FROM food_item fi WHERE fi.name = 'Vada Pav'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Authentic Mumbai flavor with spicy garlic chutney. Soft pav and freshly fried vada. Really enjoyed it.');

-- Review 4: Masala Tea (5 Stars, 6 days ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 5, 'Vikram Iyer (Sample Review)', 
       '[Sample Review] Rich cardamom aroma and great balance of spices. High quality brew that tastes genuinely homemade.', 
       DATE_SUB(NOW(), INTERVAL 6 DAY)
FROM food_item fi WHERE fi.name = 'Masala Tea'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Rich cardamom aroma and great balance of spices. High quality brew that tastes genuinely homemade.');

-- Review 5: Artisan Margherita Pizza (4 Stars, 9 days ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 4, 'Sneha Patel [Demo Reviewer]', 
       '[Sample Review] Thin crust with flavorful tomato sauce and plenty of mozzarella. A solid 4-star choice for lunch.', 
       DATE_SUB(NOW(), INTERVAL 9 DAY)
FROM food_item fi WHERE fi.name = 'Artisan Margherita Pizza'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Thin crust with flavorful tomato sauce and plenty of mozzarella. A solid 4-star choice for lunch.');

-- Review 6: Classic Smash Cheeseburger (4 Stars, 13 days ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 4, 'Rohan Joshi (Sample Review)', 
       '[Sample Review] Juicy double patty with melted cheddar and caramelized onions. Good balance of flavors and fresh bun.', 
       DATE_SUB(NOW(), INTERVAL 13 DAY)
FROM food_item fi WHERE fi.name = 'Classic Smash Cheeseburger'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Juicy double patty with melted cheddar and caramelized onions. Good balance of flavors and fresh bun.');

-- Review 7: Classic Cold Coffee (5 Stars, 17 days ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 5, 'Tanvi Shah [Demo Reviewer]', 
       '[Sample Review] Thick, frothy, and perfectly chilled with a generous chocolate drizzle. Excellent quality and taste!', 
       DATE_SUB(NOW(), INTERVAL 17 DAY)
FROM food_item fi WHERE fi.name = 'Classic Cold Coffee'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Thick, frothy, and perfectly chilled with a generous chocolate drizzle. Excellent quality and taste!');

-- Review 8: Grilled Vegetable Sandwich (3 Stars, 21 days ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 3, 'Arjun Mehta (Sample Review)', 
       '[Sample Review] Fresh cucumber, tomatoes, and cheese slices. Good taste, though the bread could have been toasted slightly crispier.', 
       DATE_SUB(NOW(), INTERVAL 21 DAY)
FROM food_item fi WHERE fi.name = 'Grilled Vegetable Sandwich'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Fresh cucumber, tomatoes, and cheese slices. Good taste, though the bread could have been toasted slightly crispier.');

-- Review 9: Kachori (3 Stars, 25 days ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 3, 'Meera Nair [Demo Reviewer]', 
       '[Sample Review] Flaky outer crust with spicy dal filling. Good flavor, but was served slightly lukewarm.', 
       DATE_SUB(NOW(), INTERVAL 25 DAY)
FROM food_item fi WHERE fi.name = 'Kachori'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Flaky outer crust with spicy dal filling. Good flavor, but was served slightly lukewarm.');

-- Review 10: Sweet Mango Lassi (4 Stars, 30 days ago)
INSERT INTO feedback (food_item_id, rating, customer_name, comment, created_at)
SELECT fi.id, 4, 'Rajesh Kannan (Sample Review)', 
       '[Sample Review] Very smooth and refreshing mango flavor. Nicely garnished with pistachios. Great dessert drink.', 
       DATE_SUB(NOW(), INTERVAL 30 DAY)
FROM food_item fi WHERE fi.name = 'Sweet Mango Lassi'
  AND NOT EXISTS (SELECT 1 FROM feedback f WHERE f.comment = '[Sample Review] Very smooth and refreshing mango flavor. Nicely garnished with pistachios. Great dessert drink.');
