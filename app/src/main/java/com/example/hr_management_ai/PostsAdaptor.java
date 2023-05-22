package com.example.hr_management_ai;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import java.util.List;

public class PostsAdaptor extends FirebaseRecyclerAdapter<RecyclerModel, PostsAdaptor.PostsViewHolder> {
    private static final String TAG = "PostsAdaptor";

    public PostsAdaptor(@NonNull FirebaseRecyclerOptions<RecyclerModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public PostsAdaptor.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new PostsViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostsAdaptor.PostsViewHolder holder, int position, @NonNull RecyclerModel model) {
        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());
        // Check if the image is a content URI
        if (isContentUri(model.getImage())) {
            // Convert content URI to direct image URL
            String imageUrl = getContentUriImageUrl(holder.itemView.getContext(), model.getImage());

            // Load the image with Glide using the direct image URL
            Glide.with(holder.itemView)
                    .load(imageUrl)
                    .into(holder.image);
        } else {
            // The image is already a direct image URL, load it with Glide directly
            Glide.with(holder.itemView)
                    .load(model.getImage())
                    .into(holder.image);
        }
    }
    // Helper method to check if the image is a content URI
    private boolean isContentUri(String imageUrl) {
        return imageUrl != null && imageUrl.startsWith("content://");
    }
    // Helper method to convert content URI to direct image URL
    private String getContentUriImageUrl(Context context, String contentUri) {
        Uri imageUri = Uri.parse(contentUri);
        String filePath = getFilePathFromUri(context, imageUri);
        return filePath != null ? "file://" + filePath : null;
    }
    // Helper method to retrieve file path from content URI
    private String getFilePathFromUri(Context context, Uri uri) {
        String filePath = null;
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                filePath = cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return filePath;
    }
    static class PostsViewHolder extends RecyclerView.ViewHolder {
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