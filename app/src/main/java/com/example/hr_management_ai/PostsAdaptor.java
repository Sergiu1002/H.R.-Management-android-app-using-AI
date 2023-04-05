/*
package com.example.hr_management_ai;

import androidx.recyclerview.widget.RecyclerView;

public class PostsAdaptor  extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostsAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.titleTextView.setText(post.getTitle());
        holder.bodyTextView.setText(post.getBody());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView bodyTextView;

        public PostViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            bodyTextView = itemView.findViewById(R.id.bodyTextView);
        }
    }
}*/
