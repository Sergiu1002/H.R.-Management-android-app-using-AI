package com.example.hr_management_ai;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class create_post extends AppCompatActivity {
    Toolbar toolbar;
    EditText PostTitle, PostDescription;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_post);
        toolbar = findViewById(R.id.toolbar_view);

        Button CreatePostButton = (Button) findViewById(R.id.CreatePostButton);
        PostTitle = findViewById(R.id.PostTitle);
        PostDescription = findViewById(R.id.PostDescription);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        CreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PostTitle != null || PostDescription != null) {
                    InsertData  ();
                } else {
                    Toast.makeText(create_post.this, "Missing title or description", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void InsertData() {
        String title = PostTitle.getText().toString();
        String description = PostDescription.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        DatabaseReference newPostRef = myRef.child("Posts").child(userID).push(); // Generate a unique key for the new data
        String newPostKey = newPostRef.getKey(); // Get the generated key


        RecyclerModel post = new RecyclerModel(title, description);
        newPostRef.setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(create_post.this, feed_page.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while creating the post
                        Toast.makeText(create_post.this, "Failed to create post", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}