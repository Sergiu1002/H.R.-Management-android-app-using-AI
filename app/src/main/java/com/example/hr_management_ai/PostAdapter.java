package com.example.hr_management_ai;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<RecyclerModel> dataSet;

    public PostAdapter(List<RecyclerModel> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerModel data = dataSet.get(position);
        holder.ReviewTitle.setText(data.getTitle());
        holder.ReviewDescription.setText(data.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the new activity here
                Intent intent = new Intent(v.getContext(), ReviewActivity.class);
                // Pass any necessary data to the new activity
                intent.putExtra("title", data.getTitle());
                intent.putExtra("description", data.getDescription());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ReviewTitle;
        TextView ReviewDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ReviewTitle = itemView.findViewById(R.id.ReviewTitle); // Replace with actual IDs
            ReviewDescription = itemView.findViewById(R.id.ReviewDescription); // Replace with actual IDs

        }
    }
}

