package com.example.hr_management_ai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        toolbar = findViewById(R.id.toolbar_view);
        ImageButton LogoButton = findViewById(R.id.toolbar_logo_button);
        EditText toolbar_search_bar = findViewById(R.id.toolbar_search_bar);
        ImageButton SearchButton = findViewById(R.id.toolbar_search_button);
        ImageButton dropdownButton = findViewById(R.id.toolbar_dropdown_button);

        TextView original_title = findViewById(R.id.ReviewTitle);
        TextView original_description = findViewById(R.id.ReviewDescription);
        EditText user_review = findViewById(R.id.user_review);

        Button submit_review = findViewById(R.id.submit_review);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String description = extras.getString("description");

            original_title.setText(title);
            original_description.setText(description);}

        submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userReview = user_review.getText().toString();
                // Get the current user's ID from FirebaseAuth
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                // Get the current user's ID (You might get it from FirebaseAuth)
                String currentUserID = currentUser.getUid(); // Replace with actual user ID

                DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews").child(currentUserID);

                // Generate a unique review ID (You can use push() or your own method)
                String reviewID = reviewsRef.push().getKey();

                // Create a Review object and set its properties
                Review newReview = new Review();
                newReview.setDescription(user_review.getText().toString());

                // Save the review data to the database
                reviewsRef.child(reviewID).setValue(newReview);

                // Optionally, show a success message or navigate back to the previous activity
                Toast.makeText(ReviewActivity.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReviewActivity.this, feed_page.class);
                startActivity(intent);
            }
        });

        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDropdownMenu(view);
                showPopupMenu(dropdownButton);
            }
        });

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello, world!", Toast.LENGTH_SHORT).show();
            }
        });

        LogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, feed_page.class);
                startActivity(intent);
            }
        });

    }
    private void showPopupMenu(ImageButton dropdownButton) {
        PopupMenu popupMenu = new PopupMenu(this, dropdownButton);
        popupMenu.inflate(R.menu.dropdown_menu); // Inflate the menu resource
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks here
                switch (item.getItemId()) {
                    case R.id.account_button:
                        Intent intent1 = new Intent(ReviewActivity.this, AccountActivity.class);
                        startActivity(intent1);
                    case R.id.settings_button:
                        return true;
                    case R.id.create_post_button:
                        Intent intent = new Intent(ReviewActivity.this, create_post.class);
                        startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show();
    }}