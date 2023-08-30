package com.example.hr_management_ai;

public class Review {
    private String description;

    public Review() {
        // Default constructor required for Firebase
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
