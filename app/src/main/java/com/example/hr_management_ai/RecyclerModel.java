
package com.example.hr_management_ai;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RecyclerModel {
    String Title;
    String Description;
    String Image;

    public RecyclerModel(String title, String description, String image) {
        Title = title;
        Description = description;
        Image = image;
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
}
