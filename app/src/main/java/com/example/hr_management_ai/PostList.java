package com.example.hr_management_ai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostList extends AppCompatActivity {
    private String Title;
    private String Description;
    private String Image;
    RecyclerView recyclerView;
    ArrayList<RecyclerModel> list;
    DatabaseReference databaseReference;
    PostsAdaptor adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PostList.this, feed_page.class));
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_page);

        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostsAdaptor(this, list);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    RecyclerModel recyclerModel = dataSnapshot1.getValue(RecyclerModel.class);
                    list.add(recyclerModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PostList.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
