package com.example.hr_management_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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

        linearLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_view);

        ImageButton LogoButton = findViewById(R.id.toolbar_logo_button);
        EditText toolbar_search_bar = findViewById(R.id.toolbar_search_bar);
        ImageButton SearchButton = findViewById(R.id.toolbar_search_button);
        ImageButton dropdownButton = findViewById(R.id.toolbar_dropdown_button);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<RecyclerModel> postList = new ArrayList<>(); // Your list of data from Firebase

        PostAdapter adapter = new PostAdapter(postList);
        recyclerView.setAdapter(adapter);

        Log.d("FeedPage", "Number of posts: " + postList.size());

        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot postSnapshot : userSnapshot.getChildren()) {
                        String title = postSnapshot.child("title").getValue(String.class);
                        String description = postSnapshot.child("description").getValue(String.class);
                        String userName = postSnapshot.child("userName").getValue(String.class);
                        String profilePictureUrl = postSnapshot.child("profilePictureUrl\n").getValue(String.class);

                        RecyclerModel post = new RecyclerModel(title, description);
                        postList.add(post);
                    }
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
                Log.e("FeedPage", "Error fetching data: " + databaseError.getMessage());
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
                Intent intent = new Intent(feed_page.this, feed_page.class);
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
                        Intent intent1 = new Intent(feed_page.this, AccountActivity.class);
                        startActivity(intent1);
                    case R.id.settings_button:
                        return true;
                    case R.id.create_post_button:
                        Intent intent = new Intent(feed_page.this, create_post.class);
                        startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show();
    }
}

