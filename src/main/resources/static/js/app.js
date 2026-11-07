// Interactive JavaScript for Online Food Feedback System

document.addEventListener('DOMContentLoaded', () => {
    // 1. Live Food Card Search Filter
    const searchInput = document.getElementById('foodSearchInput');
    const foodCards = document.querySelectorAll('.food-item-col');

    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase().trim();
            foodCards.forEach(col => {
                const title = col.getAttribute('data-name')?.toLowerCase() || '';
                const desc = col.getAttribute('data-desc')?.toLowerCase() || '';
                const category = col.getAttribute('data-category')?.toLowerCase() || '';

                if (title.includes(query) || desc.includes(query) || category.includes(query)) {
                    col.style.display = '';
                } else {
                    col.style.display = 'none';
                }
            });
            updateEmptyState();
        });
    }

    // 2. Category Filter Buttons
    const filterButtons = document.querySelectorAll('.filter-btn');
    if (filterButtons.length > 0) {
        filterButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                filterButtons.forEach(b => b.classList.remove('active'));
                btn.classList.add('active');

                const selectedCategory = (btn.getAttribute('data-filter') || '').toLowerCase();
                foodCards.forEach(col => {
                    const itemCat = (col.getAttribute('data-category') || '').toLowerCase();
                    if (selectedCategory === 'all' || 
                        itemCat === selectedCategory || 
                        (selectedCategory.includes('wrap') && itemCat.includes('wrap')) ||
                        (selectedCategory.includes('snack') && itemCat.includes('snack')) ||
                        (selectedCategory.includes('tea') && (itemCat.includes('tea') || itemCat.includes('beverage')))) {
                        col.style.display = '';
                    } else {
                        col.style.display = 'none';
                    }
                });
                updateEmptyState();
            });
        });
    }

    function updateEmptyState() {
        const noResults = document.getElementById('noResultsMsg');
        if (!noResults) return;

        let visibleCount = 0;
        foodCards.forEach(col => {
            if (col.style.display !== 'none') visibleCount++;
        });

        if (visibleCount === 0) {
            noResults.classList.remove('d-none');
        } else {
            noResults.classList.add('d-none');
        }
    }

    // 3. Clean, Modern and Interactive Star Rating Component
    const starWidget = document.getElementById('starRatingWidget') || document.querySelector('.rating-stars') || document.querySelector('.rate-star-row');
    const ratingInput = document.getElementById('selectedRatingInput') || document.getElementById('rating') || document.querySelector('input[name="rating"]');
    const ratingTextSpan = document.getElementById('ratingText') || document.querySelector('.rating-message');
    const clientError = document.getElementById('ratingClientError');
    const feedbackForm = document.getElementById('feedbackFormElement');

    const ratingMessages = {
        1: '1 Star: Very Bad',
        2: '2 Stars: Poor',
        3: '3 Stars: Average',
        4: '4 Stars: Good',
        5: '5 Stars: Excellent!'
    };

    if (starWidget) {
        const starButtons = Array.from(starWidget.querySelectorAll('.rating-star, .rate-star-btn, .star-btn'));
        let currentRating = parseInt(ratingInput ? ratingInput.value : '', 10);
        if (isNaN(currentRating) || currentRating < 1 || currentRating > 5) {
            currentRating = 0;
            if (ratingInput) ratingInput.value = '';
        }

        // Accurately render preview highlighting on hover
        function previewStars(hoverVal) {
            starButtons.forEach(btn => {
                const val = parseInt(btn.getAttribute('data-value'), 10);
                if (val <= hoverVal) {
                    btn.classList.add('is-preview');
                    btn.classList.remove('is-selected');
                } else {
                    btn.classList.remove('is-preview');
                    btn.classList.remove('is-selected');
                }
            });

            if (ratingTextSpan) {
                if (hoverVal > 0 && ratingMessages[hoverVal]) {
                    ratingTextSpan.textContent = ratingMessages[hoverVal];
                    ratingTextSpan.className = 'rating-message rate-rating-pill active-pill';
                }
            }
        }

        // Accurately restore persisted selection or empty state
        function restoreSelection() {
            starButtons.forEach(btn => {
                btn.classList.remove('is-preview');
                const val = parseInt(btn.getAttribute('data-value'), 10);
                if (currentRating > 0 && val <= currentRating) {
                    btn.classList.add('is-selected');
                } else {
                    btn.classList.remove('is-selected');
                }
            });

            if (ratingTextSpan) {
                if (currentRating > 0 && ratingMessages[currentRating]) {
                    ratingTextSpan.textContent = ratingMessages[currentRating];
                    ratingTextSpan.className = 'rating-message rate-rating-pill active-pill';
                } else {
                    ratingTextSpan.textContent = 'Tap a star to rate';
                    ratingTextSpan.className = 'rating-message rate-rating-pill';
                }
            }
        }

        // Set and lock the chosen rating
        function setRating(val, animate) {
            currentRating = val;
            if (ratingInput) {
                ratingInput.value = val;
            }
            restoreSelection();

            if (clientError) {
                clientError.classList.add('d-none');
            }

            if (animate) {
                const clickedBtn = starWidget.querySelector(`button[data-value="${val}"]`);
                if (clickedBtn) {
                    clickedBtn.classList.remove('pop-anim');
                    void clickedBtn.offsetWidth; // force DOM reflow
                    clickedBtn.classList.add('pop-anim');
                }
                if (ratingTextSpan) {
                    ratingTextSpan.classList.remove('pulse-anim');
                    void ratingTextSpan.offsetWidth;
                    ratingTextSpan.classList.add('pulse-anim');
                }
            }
        }

        // Initialize display state
        restoreSelection();

        // Attach Click and Hover listeners to each star button
        starButtons.forEach(btn => {
            const val = parseInt(btn.getAttribute('data-value'), 10);

            // Hover: highlight that star and all previous stars
            btn.addEventListener('mouseenter', () => {
                previewStars(val);
            });

            // Click: retain the selected rating
            btn.addEventListener('click', (e) => {
                e.preventDefault();
                setRating(val, true);
            });
        });

        // Mouse leaves the widget: restore selected rating or empty state
        starWidget.addEventListener('mouseleave', () => {
            restoreSelection();
        });

        // Accessible Keyboard Navigation (ArrowLeft, ArrowRight, ArrowUp, ArrowDown, 1-5 keys)
        starWidget.addEventListener('keydown', (e) => {
            let targetRating = currentRating;
            if (e.key === 'ArrowRight' || e.key === 'ArrowUp') {
                e.preventDefault();
                targetRating = Math.min(5, (currentRating || 0) + 1);
                setRating(targetRating, true);
            } else if (e.key === 'ArrowLeft' || e.key === 'ArrowDown') {
                e.preventDefault();
                targetRating = Math.max(1, (currentRating || 2) - 1);
                setRating(targetRating, true);
            } else if (['1', '2', '3', '4', '5'].includes(e.key)) {
                e.preventDefault();
                setRating(parseInt(e.key, 10), true);
            }
        });

        // Form Submission validation:
        if (feedbackForm) {
            feedbackForm.addEventListener('submit', (e) => {
                const val = parseInt(ratingInput ? ratingInput.value : '', 10);
                if (isNaN(val) || val < 1 || val > 5) {
                    e.preventDefault(); // Stop submission only if rating is empty or invalid
                    if (clientError) {
                        clientError.classList.remove('d-none');
                    }
                    starWidget.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    return false;
                }
                // Valid! Allow natural form submission to Spring Boot!
            });
        }
    }

    // 4. Character Counter for Comment Textarea
    const commentArea = document.getElementById('comment');
    const charCounter = document.getElementById('charCount');
    if (commentArea && charCounter) {
        commentArea.addEventListener('input', () => {
            charCounter.textContent = commentArea.value.length;
        });
    }

    // 5. Auto dismiss alerts after 6 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
            if (bsAlert) {
                bsAlert.close();
            }
        }, 6000);
    });
});
