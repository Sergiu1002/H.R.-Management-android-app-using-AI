package com.example.hr_management_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class feed_page extends AppCompatActivity {
    LinearLayout linearLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feed_page);

        // Inițializarea elementelor de interfață
        linearLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_view);
        ImageButton LogoButton = findViewById(R.id.toolbar_logo_button);
        EditText toolbar_search_bar = findViewById(R.id.toolbar_search_bar);
        ImageButton SearchButton = findViewById(R.id.toolbar_search_button);
        ImageButton dropdownButton = findViewById(R.id.toolbar_dropdown_button);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Inițializarea adapterului RecyclerView
        List<RecyclerModel> postList = new ArrayList<>();
        PostAdapter adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);

        // Inițializarea și adăugarea listener-ului pentru baza de date Firebase
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                // Iterarea prin datele din baza de date și adăugarea în lista RecyclerView
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot : userSnapshot.getChildren()) {
                        String title = postSnapshot.child("title").getValue(String.class);
                        String description = postSnapshot.child("description").getValue(String.class);
                        RecyclerModel post = new RecyclerModel(title, description);
                        postList.add(post);
                    }
                }
                adapter.notifyDataSetChanged(); // Notificarea adapterului despre schimbări în lista de date
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FeedPage", "Error fetching data: " + databaseError.getMessage());
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
                Intent intent = new Intent(feed_page.this, feed_page.class);
                startActivity(intent);
            }
        });
    }

    // Metodă pentru afișarea meniului dropdown
    private void showPopupMenu(ImageButton dropdownButton) {
        PopupMenu popupMenu = new PopupMenu(this, dropdownButton);
        popupMenu.inflate(R.menu.dropdown_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.account_button:
                        Intent intent1 = new Intent(feed_page.this, AccountActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.settings_button:
                        // Acțiunea pentru setări
                        return true;
                    case R.id.create_post_button:
                        Intent intent = new Intent(feed_page.this, create_post.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
