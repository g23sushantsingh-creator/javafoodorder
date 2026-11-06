package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FeedbackFormDto {

    @NotNull(message = "Please select a star rating (1 to 5)")
    @Min(value = 1, message = "Rating must be at least 1 star")
    @Max(value = 5, message = "Rating cannot exceed 5 stars")
    private Integer rating;

    @NotBlank(message = "Please enter your name or student ID")
    @Size(min = 2, max = 80, message = "Name must be between 2 and 80 characters")
    private String customerName;

    @NotBlank(message = "Please write your feedback comment")
    @Size(min = 3, max = 1000, message = "Comment must be between 3 and 1000 characters")
    private String comment;

    public FeedbackFormDto() {
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
