package com.example.hr_management_ai;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RecyclerModel {
    String Title;
    String Description;

    // Constructorul clasei RecyclerModel
    public RecyclerModel(String title, String description) {
        this.Title = title;
        this.Description = description;
    }

    // Metoda pentru obținerea titlului
    public String getTitle() {
        return Title;
    }

    // Metoda pentru obținerea descrierii
    public String getDescription() {
        return Description;
    }

    // Metoda pentru setarea titlului
    public void setTitle(String title) {
        Title = title;
    }

    // Metoda pentru setarea descrierii
    public void setDescription(String description) {
        Description = description;
    }

}
