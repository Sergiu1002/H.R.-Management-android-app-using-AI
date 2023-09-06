package com.example.hr_management_ai;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.Sentiment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.cloud.language.v1.LanguageServiceClient;
import java.io.InputStream;


public class ReviewActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView user_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        FirebaseApp.initializeApp(this);
        toolbar = findViewById(R.id.toolbar_view);
        ImageButton LogoButton = findViewById(R.id.toolbar_logo_button);
        EditText toolbar_search_bar = findViewById(R.id.toolbar_search_bar);
        ImageButton SearchButton = findViewById(R.id.toolbar_search_button);
        ImageButton dropdownButton = findViewById(R.id.toolbar_dropdown_button);
        TextView original_title = findViewById(R.id.ReviewTitle);
        TextView original_description = findViewById(R.id.ReviewDescription);
        EditText user_review = findViewById(R.id.user_review);
        user_score = findViewById(R.id.user_score);
        Button submit_review = findViewById(R.id.submit_review);

        // Obținerea datelor trimise prin intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String description = extras.getString("description");
            original_title.setText(title);
            original_description.setText(description);
        }

        // Acțiunea de submit a recenziei
        submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userReview = user_review.getText().toString();
                Toast.makeText(ReviewActivity.this, "Review submitted successfully", Toast.LENGTH_SHORT).show();
                performSentimentAnalysis(userReview);
            }
        });

        // Listener pentru butonul dropdown
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(dropdownButton);
            }
        });

        // Listener pentru butonul de căutare
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello, world!", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener pentru butonul de logo
        LogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReviewActivity.this, feed_page.class);
                startActivity(intent);
            }
        });
    }

    // Realizarea analizei de sentiment
    private void performSentimentAnalysis(String userReview) {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.application_default_credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            LanguageServiceSettings settings = LanguageServiceSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();
            try (LanguageServiceClient languageServiceClient = LanguageServiceClient.create(settings)) {
                Document doc = Document.newBuilder()
                        .setContent(userReview)
                        .setType(Document.Type.PLAIN_TEXT)
                        .build();
                Sentiment sentiment = languageServiceClient.analyzeSentiment(doc).getDocumentSentiment();

                // Determinarea etichetei de sentiment
                String sentimentLabel;
                if (sentiment.getScore() > 0.25) {
                    sentimentLabel = "Positive";
                } else if (sentiment.getScore() < -0.25) {
                    sentimentLabel = "Negative";
                } else {
                    sentimentLabel = "Neutral";
                }

                Toast.makeText(this, "Sentiment: " + sentimentLabel, Toast.LENGTH_SHORT).show();
                user_score.setText("Predicted experience: " + sentimentLabel);

                // Salvarea rezultatelor analizei în Firebase
                saveSentimentToFirebase(sentiment.getScore(), sentimentLabel, userReview);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error analyzing sentiment", Toast.LENGTH_SHORT).show();
        }
    }

    // Salvarea rezultatelor analizei în Firebase
    private void saveSentimentToFirebase(float sentimentScore, String sentimentLabel, String userReview) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserID = currentUser.getUid();

        DatabaseReference reviewsRef = FirebaseDatabase.getInstance().getReference().child("Reviews").child(currentUserID);

        String reviewID = reviewsRef.push().getKey();

        Review newReview = new Review();
        newReview.setDescription(userReview);
        newReview.setSentimentScore(sentimentScore);
        newReview.setSentimentLabel(sentimentLabel);

        // Setarea datelor recenziei în Firebase
        reviewsRef.child(reviewID).setValue(newReview);
    }

    // Afișarea meniului dropdown
    private void showPopupMenu(ImageButton dropdownButton) {
        PopupMenu popupMenu = new PopupMenu(this, dropdownButton);
        popupMenu.inflate(R.menu.dropdown_menu); // Inflarea resursei meniului
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Gestionarea acțiunilor pentru fiecare element din meniu
                switch (item.getItemId()) {
                    case R.id.account_button:
                        Intent intent1 = new Intent(ReviewActivity.this, AccountActivity.class);
                        startActivity(intent1);
                    case R.id.settings_button:
                        // Tratarea acțiunii pentru setări
                        return true;
                    case R.id.create_post_button:
                        Intent intent = new Intent(ReviewActivity.this, create_post.class);
                        startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show(); // Afișarea meniului popup
    }
}
