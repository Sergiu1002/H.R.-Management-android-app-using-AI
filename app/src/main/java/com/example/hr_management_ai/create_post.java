package com.example.hr_management_ai;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private Uri selectedImageUri;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private StorageReference storageRef;
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
                            selectedImageUri = result; // Update the selected image URI
                            imageIdentifier = selectedImageUri.toString(); // or selectedImageUri.getLastPathSegment()

                            Glide.with(getApplicationContext())
                                    .load(selectedImageUri)
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
                if (selectedImageUri != null) {
                    InsertData  (selectedImageUri); // Pass the selected image URI to InsertData method
                } else {
                    Toast.makeText(create_post.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void InsertData(Uri selectedImageUri) {
        String title = PostTitle.getText().toString();
        String description = PostDescription.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        DatabaseReference newPostRef = myRef.child("Posts").child(userID).push(); // Generate a unique key for the new data
        String newPostKey = newPostRef.getKey(); // Get the generated key

        imageIdentifier = selectedImageUri.toString(); // Update the image identifier with the selected image URI

        RecyclerModel post = new RecyclerModel(title, description, selectedImageUri.toString()); // Pass the image URL to the constructor
        newPostRef.setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Image upload success
                        // Upload the image file to Firebase Storage and update the database
                        uploadImageToStorage(newPostKey, selectedImageUri, title, description, newPostRef);
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

    private void uploadImageToStorage(String newPostKey, Uri imageUri, String title, String description, DatabaseReference newPostRef) {
        storageRef = FirebaseStorage.getInstance().getReference(); // Initialize the storage reference

        StorageReference imageRef = storageRef.child("images/" + newPostKey);
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image upload success
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUrl) {
                                        String imageUrl = downloadUrl.toString();
                                        // Update the image URL in the database
                                        newPostRef.child("image").setValue(imageUrl) // Update the "image" field
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Database update success
                                                        Toast.makeText(create_post.this, "Post Created", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(create_post.this, feed_page.class));
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Error occurred while updating the database
                                                        Toast.makeText(create_post.this, "Failed to update post", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Error occurred while getting the download URL
                                        Toast.makeText(create_post.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Image upload failed
                        Toast.makeText(create_post.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}