package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuickAddAdapter extends RecyclerView.Adapter<QuickAddAdapter.ViewHolder> {

    private final List<PasswordService> serviceList;
    public QuickAddAdapter(List<PasswordService> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PasswordService service = serviceList.get(position);
        holder.textView.setText(service.getName());
        holder.imageView.setImageResource(service.getIconResId());

        // TODO: Add click listener to navigate to input form

        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.service_name);
            imageView = view.findViewById(R.id.service_icon);
        }
    }
}