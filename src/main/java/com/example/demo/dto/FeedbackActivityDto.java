package com.example.demo.dto;

/**
 * DTO representing the number of feedback submissions on a single calendar date.
 * Used to populate the Feedback Activity line chart on the Analytics page.
 */
public class FeedbackActivityDto {

    /** Date label displayed on the X-axis (e.g. "Nov 07"). */
    private String dateLabel;

    /** ISO date string used as the chart dataset label (e.g. "2026-11-07"). */
    private String dateKey;

    /** Number of feedback submissions recorded on this date. */
    private long count;

    public FeedbackActivityDto() {
    }

    public FeedbackActivityDto(String dateLabel, String dateKey, long count) {
        this.dateLabel = dateLabel;
        this.dateKey = dateKey;
        this.count = count;
    }

    public String getDateLabel() {
        return dateLabel;
    }

    public void setDateLabel(String dateLabel) {
        this.dateLabel = dateLabel;
    }

    public String getDateKey() {
        return dateKey;
    }

    public void setDateKey(String dateKey) {
        this.dateKey = dateKey;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
