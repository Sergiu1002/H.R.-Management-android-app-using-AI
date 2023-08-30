
package com.example.hr_management_ai;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RecyclerModel {
    String Title;
    String Description;
    String name;

    public RecyclerModel(String title, String description) {
        this.Title = title;
        this.Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

}