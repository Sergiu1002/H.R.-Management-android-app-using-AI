
package com.example.hr_management_ai;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RecyclerModel {
    String Title;
    String Description;
    String Image;

    RecyclerModel() {
    }

    public RecyclerModel(String title, String description, String image) {
        this.Title = title;
        this.Description = description;
        this.Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage() {
        return Image;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImage(String image) {
        Image = image;
    }
}