package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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


        holder.itemView.setOnClickListener(v -> {

            NavController navController = Navigation.findNavController(holder.itemView);
            Bundle bundle = new Bundle();

            if (service.getId().equals("custom")) {
                navController.navigate(R.id.action_quickAddFragment_to_customPasswordListFragment);
                return;
            }

            PasswordDbHelper dbHelper = new PasswordDbHelper(holder.itemView.getContext());
            Cursor cursor = dbHelper.getAllPasswords();
            boolean exists = false;

            while (cursor.moveToNext()) {
                String website = cursor.getString(cursor.getColumnIndexOrThrow(PasswordDbHelper.COLUMN_WEBSITE));
                if (website.equals(service.getUrl())) {
                    exists = true;
                    break;
                }
            }
            cursor.close();

            if (exists) {
                bundle.putString("website", service.getUrl());
                bundle.putString("serviceName", service.getName());
                bundle.putInt("serviceIconResId", service.getIconResId());
                bundle.putInt("serviceColor", service.getBackgroundColor());
                navController.navigate(R.id.action_quickAddFragment_to_viewPasswordFragment, bundle);
            } else {
                bundle.putString("serviceName", service.getName());
                bundle.putString("serviceUrl", service.getUrl());
                bundle.putInt("serviceIconResId", service.getIconResId());
                bundle.putInt("serviceColor", service.getBackgroundColor());
                navController.navigate(R.id.action_quickAddFragment_to_passwordFormFragment, bundle);
            }
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