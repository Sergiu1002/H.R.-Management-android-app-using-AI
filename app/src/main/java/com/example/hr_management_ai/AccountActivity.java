package com.example.hr_management_ai;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AccountActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    Toolbar toolbar;
    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ImageView profile_picture;
    private StorageReference storageRef;
    private String imageIdentifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_account);

        coordinatorLayout = findViewById(R.id.account_settings_layout);
        toolbar = findViewById(R.id.toolbar_view);

        ImageButton LogoButton = findViewById(R.id.toolbar_logo_button);
        EditText toolbar_search_bar = findViewById(R.id.toolbar_search_bar);
        ImageButton SearchButton = findViewById(R.id.toolbar_search_button);
        ImageButton dropdownButton = findViewById(R.id.toolbar_dropdown_button);

        EditText Name = findViewById(R.id.Name);
        EditText JobTitle = findViewById(R.id.JobTitle);
        EditText Gender = findViewById(R.id.Gender);
        EditText Pronouns = findViewById(R.id.Pronouns);
        Button change_account_settings_button = findViewById(R.id.change_account_settings_button);
        Button revert_account_settings_button = findViewById(R.id.revert_account_settings_button);
        Button change_picture_button = findViewById(R.id.change_picture_button);
        ImageView profile_picture = findViewById(R.id.profile_picture);

        String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference().child("UserProfiles").child(currentUserUID);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            selectedImageUri = result;
                            imageIdentifier = selectedImageUri.toString();

                            Glide.with(getApplicationContext())
                                    .load(selectedImageUri)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(profile_picture);
                        }
                    }
                });

        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String jobTitle = dataSnapshot.child("jobTitle").getValue(String.class);
                    String gender = dataSnapshot.child("gender").getValue(String.class);
                    String pronouns = dataSnapshot.child("pronouns").getValue(String.class);

                    Name.setText(name);
                    JobTitle.setText(jobTitle);
                    Gender.setText(gender);
                    Pronouns.setText(pronouns);

                    String profilePictureUrl = dataSnapshot.child("profilePictureUrl").getValue(String.class);
                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                        // Load profile picture using an image loading library
                        Glide.with(AccountActivity.this)
                                .load(profilePictureUrl)
                                .into(profile_picture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        change_account_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Updating profile...", Toast.LENGTH_SHORT).show();
                String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                UserProfile userProfile = new UserProfile();
                userProfile.setName(Name.getText().toString());
                userProfile.setJobTitle(JobTitle.getText().toString());
                userProfile.setGender(Gender.getText().toString());
                userProfile.setPronouns(Pronouns.getText().toString());

                if (selectedImageUri != null) {
                    uploadImageToStorage(currentUserUID, selectedImageUri, userProfile);
                } else {
                    userProfileRef.child(currentUserUID).setValue(userProfile)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error while updating profile", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });



        revert_account_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                            String name = userProfile.getName();
                            String jobTitle = userProfile.getJobTitle();
                            String gender = userProfile.getGender();
                            String pronouns = userProfile.getPronouns();
                            Name.setText(name);
                            JobTitle.setText(jobTitle);
                            Gender.setText(gender);
                            Pronouns.setText(pronouns);

                            String profilePictureUrl = userProfile.getProfilePictureUrl();
                            if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                Glide.with(AccountActivity.this)
                                        .load(profilePictureUrl)
                                        .into(profile_picture);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });


        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Intent intent = new Intent(AccountActivity.this, feed_page.class);
                startActivity(intent);
            }
        });

        change_picture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryLauncher.launch("image/*");
            }
        });
    }
    private void uploadImageToStorage(String currentUserUID, Uri imageUri, UserProfile userProfile) {
        storageRef = FirebaseStorage.getInstance().getReference();
        DatabaseReference userProfileRef = FirebaseDatabase.getInstance().getReference().child("UserProfiles").child(currentUserUID);

        StorageReference imageRef = storageRef.child("profile_pictures").child(currentUserUID + ".jpg");
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUrl) {
                                        String imageUrl = downloadUrl.toString();
                                        userProfile.setProfilePictureUrl(imageUrl);
                                        userProfileRef.setValue(userProfile)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(AccountActivity.this, "Profile Picture Updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AccountActivity.this, "Failed to update profile picture", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AccountActivity.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AccountActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
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
                        Intent intent1 = new Intent(AccountActivity.this, AccountActivity.class);
                        startActivity(intent1);
                    case R.id.settings_button:
                        return true;
                    case R.id.create_post_button:
                        Intent intent = new Intent(AccountActivity.this, create_post.class);
                        startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show();
    }
}