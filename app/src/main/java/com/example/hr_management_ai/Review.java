package com.example.hr_management_ai;

public class Review {
    private String description;
    private float sentimentScore;
    private String sentimentLabel;

    // Constructorul implicit necesar pentru Firebase
    public Review() {
        // Default constructor required for Firebase
    }

    // Metoda pentru obținerea descrierii recenziei
    public String getDescription() {
        return description;
    }

    // Metoda pentru setarea descrierii recenziei
    public void setDescription(String description) {
        this.description = description;
    }

    // Metoda pentru obținerea scorului de sentiment al recenziei
    public float getSentimentScore() {
        return sentimentScore;
    }

    // Metoda pentru setarea scorului de sentiment al recenziei
    public void setSentimentScore(float sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    // Metoda pentru obținerea etichetei de sentiment a recenziei
    public String getSentimentLabel() {
        return sentimentLabel;
    }

    // Metoda pentru setarea etichetei de sentiment a recenziei
    public void setSentimentLabel(String sentimentLabel) {
        this.sentimentLabel = sentimentLabel;
    }
}
