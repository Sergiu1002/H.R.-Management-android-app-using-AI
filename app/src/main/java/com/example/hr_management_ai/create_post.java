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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

        // Ascunde bara de titlu și afișează activitatea în modul ecran complet
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_post);

        // Inițializarea barei de instrumente (toolbar)
        toolbar = findViewById(R.id.toolbar_view);

        // Inițializarea elementelor de interfață
        Button CreatePostButton = findViewById(R.id.CreatePostButton);
        PostTitle = findViewById(R.id.PostTitle);
        PostDescription = findViewById(R.id.PostDescription);

        // Inițializarea instanțelor Firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        // Acțiunea de creare a postării la apăsarea butonului "Create Post"
        CreatePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PostTitle != null || PostDescription != null) {
                    InsertData();
                } else {
                    Toast.makeText(create_post.this, "Missing title or description", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Metoda pentru inserarea datelor în baza de date Firebase
    private void InsertData() {
        String title = PostTitle.getText().toString();
        String description = PostDescription.getText().toString();

        // Obține utilizatorul autentificat curent
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        // Crează o referință nouă către locația postării în baza de date
        DatabaseReference newPostRef = myRef.child("Posts").child(userID).push();
        String newPostKey = newPostRef.getKey();

        // Creează un obiect RecyclerModel pentru postare
        RecyclerModel post = new RecyclerModel(title, description);

        // Adaugă postarea în baza de date Firebase
        newPostRef.setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Redirecționează utilizatorul către pagina de feed
                        Intent intent = new Intent(create_post.this, feed_page.class);
                        startActivity(intent);
                        finish(); // Închide activitatea curentă pentru a preveni revenirea accidentală
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(create_post.this, "Failed to create post", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
