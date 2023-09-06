package com.example.hr_management_ai;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<RecyclerModel> dataSet;

    // Constructorul adapterului
    public PostAdapter(List<RecyclerModel> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflația aspectului pentru fiecare rând al listei RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerModel data = dataSet.get(position);

        // Setarea datelor în elementele vizuale din rândul RecyclerView
        holder.ReviewTitle.setText(data.getTitle());
        holder.ReviewDescription.setText(data.getDescription());

        // Atribuirea unui ascultător pentru acțiunea de clic pe un rând din listă
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crearea unei intenții pentru a deschide o nouă activitate (ReviewActivity)
                Intent intent = new Intent(v.getContext(), ReviewActivity.class);
                intent.putExtra("title", data.getTitle());
                intent.putExtra("description", data.getDescription());
                v.getContext().startActivity(intent); // Pornirea activității ReviewActivity
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size(); // Numărul de elemente din setul de date
    }

    // Clasa ViewHolder pentru a reține și gestiona elementele vizuale ale fiecărui rând din listă
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ReviewTitle;
        TextView ReviewDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ReviewTitle = itemView.findViewById(R.id.ReviewTitle); // Înlocuiți cu ID-urile reale
            ReviewDescription = itemView.findViewById(R.id.ReviewDescription); // Înlocuiți cu ID-urile reale
        }
    }
}
