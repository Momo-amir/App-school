package com.example.myapplication.ui.passwordcustom;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.PasswordDbHelper;
import com.example.myapplication.PasswordService;
import com.example.myapplication.R;

import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CustomPasswordListFragment extends Fragment {

    private List<PasswordService> customEntries;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_add, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.quick_add_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        PasswordDbHelper dbHelper = new PasswordDbHelper(getContext());
        Cursor cursor = dbHelper.getAllPasswords();
        customEntries = new ArrayList<>();

        while (cursor.moveToNext()) {
            String website = cursor.getString(cursor.getColumnIndexOrThrow(PasswordDbHelper.COLUMN_WEBSITE));
            // avoid including known services (if needed)
            customEntries.add(new PasswordService(
                    website,
                    website,
                    R.drawable.ic_custom,
                    Color.GRAY,
                    website.hashCode() + ""
            ));
        }
        cursor.close();

        customEntries.add(new PasswordService("Add new custom entry", null, R.drawable.ic_add, Color.DKGRAY, "new"));

        recyclerView.setAdapter(new CustomPasswordAdapter(customEntries));
        return view;
    }

    private static class CustomPasswordAdapter extends RecyclerView.Adapter<CustomPasswordAdapter.ViewHolder> {

        private final List<PasswordService> entries;

        public CustomPasswordAdapter(List<PasswordService> entries) {
            this.entries = entries;
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
            PasswordService entry = entries.get(position);
            holder.textView.setText(entry.getName());
            holder.imageView.setImageResource(entry.getIconResId());

            holder.itemView.setOnClickListener(v -> {
                NavController navController = Navigation.findNavController(holder.itemView);
                Bundle bundle = new Bundle();

                if (entry.getId().equals("new")) {
                    bundle.putString("serviceName", "Custom Entry");
                    bundle.putInt("serviceIconResId", R.drawable.ic_custom);
                    bundle.putInt("serviceColor", Color.GRAY);
                    navController.navigate(R.id.action_customPasswordListFragment_to_passwordFormFragment, bundle);
                } else {
                    bundle.putString("website", entry.getUrl());
                    bundle.putString("serviceName", entry.getName());
                    bundle.putInt("serviceIconResId", R.drawable.ic_custom);
                    bundle.putInt("serviceColor", Color.GRAY);
                    navController.navigate(R.id.action_customPasswordListFragment_to_viewPasswordFragment, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView textView;
            public final ImageView imageView;

            public ViewHolder(View view) {
                super(view);
                textView = view.findViewById(R.id.service_name);
                imageView = view.findViewById(R.id.service_icon);
            }
        }
    }
}
