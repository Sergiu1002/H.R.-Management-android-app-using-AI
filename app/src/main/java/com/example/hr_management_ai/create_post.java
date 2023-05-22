package com.example.hr_management_ai;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    ImageView imageView;
    private String imageIdentifier;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_page);
        toolbar = findViewById(R.id.toolbar_view);

        Button CreatePostButton = (Button) findViewById(R.id.CreatePostButton);
        Button SelectImageButton = (Button) findViewById(R.id.SelectImageButton);
        PostTitle = findViewById(R.id.PostTitle);
        PostDescription = findViewById(R.id.PostDescription);
        ImageView imageView = findViewById(R.id.PostImage);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            imageIdentifier = result.toString(); // or result.getLastPathSegment()

                            Glide.with(getApplicationContext())
                                    .load(result)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Optional: Adjust caching strategy
                                    .into(imageView);
                        }
                    }
                });

        SelectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });
        CreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();

                // don t forget to also send back to recycler view aka feed page
            }
        });

    }
    private void InsertData() {
        String title = PostTitle.getText().toString();
        String description = PostDescription.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        DatabaseReference newPostRef = myRef.child(userID).push(); // Generate a unique key for the new data
        String newPostKey = newPostRef.getKey(); // Get the generated key

        RecyclerModel post = new RecyclerModel(title, description, imageIdentifier);
        newPostRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(create_post.this, "Post Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(create_post.this, feed_page.class));
                finish();
            }
        });
    }}