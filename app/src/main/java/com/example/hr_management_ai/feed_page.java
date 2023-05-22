package com.example.hr_management_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class feed_page extends AppCompatActivity {
    LinearLayout linearLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    RecyclerView recyclerView; // Added RecyclerView
    PostsAdaptor adapter; // Added adapter

    private String Title;
    private String Description;
    private String Image;
    ArrayList<RecyclerModel> list;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feed_page);

        linearLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_view);
        recyclerView = findViewById(R.id.recyclerView); // Initialize RecyclerView

        ImageButton LogoButton = findViewById(R.id.toolbar_logo_button);
        EditText toolbar_search_bar = findViewById(R.id.toolbar_search_bar);
        ImageButton SearchButton = findViewById(R.id.toolbar_search_button);
        ImageButton dropdownButton = findViewById(R.id.toolbar_dropdown_button);


        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostsAdaptor(this, list);
        recyclerView.setAdapter(adapter);
        for (RecyclerModel item : list) {
            Log.d("RecyclerView", "Title: " + item.getTitle() + ", Description: " + item.getDescription());
        }
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDropdownMenu(view);
                Toast.makeText(getApplicationContext(), "Hello, world!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(feed_page.this, create_post.class);
                startActivity(intent);
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
                Toast.makeText(getApplicationContext(), "Hello, world!", Toast.LENGTH_SHORT).show();
            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    RecyclerModel recyclerModel = dataSnapshot1.getValue(RecyclerModel.class);
                    list.add(recyclerModel);
                }
                adapter.notifyDataSetChanged();

                // Log the retrieved data
                for (RecyclerModel model : list) {
                    Log.d("Data", "Title: " + model.getTitle() + ", Description: " + model.getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(feed_page.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        for (RecyclerModel item : list) {
            Log.d("RecyclerView", "Title: " + item.getTitle() + ", Description: " + item.getDescription());
        }

    }
}
