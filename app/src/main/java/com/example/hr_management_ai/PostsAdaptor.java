package com.example.hr_management_ai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.data.model.User;

import java.util.List;

public class PostsAdaptor extends RecyclerView.Adapter<PostsAdaptor.PostsViewHolder>{
    Context context;
    List<RecyclerModel> list;

    public PostsAdaptor(Context context, List<RecyclerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);
        return new PostsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdaptor.PostsViewHolder holder, int position) {
        RecyclerModel user = list.get(position);
        holder.title.setText(user.getTitle());
        holder.description.setText(user.getDescription());
        Glide.with(holder.itemView)
                .load(user.getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageView image;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.PostTitle);
            description = itemView.findViewById(R.id.PostDescription);
            image = itemView.findViewById(R.id.PostImage);
        }
    }
}