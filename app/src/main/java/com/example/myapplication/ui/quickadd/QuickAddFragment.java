package com.example.myapplication.ui.quickadd;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.PasswordService;
import com.example.myapplication.QuickAddAdapter;
import com.example.myapplication.R;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class QuickAddFragment extends Fragment {

    public static final List<PasswordService> services = Arrays.asList(
            new PasswordService("Discord", "https://discord.com/login", R.drawable.ic_discord, Color.parseColor("#5865F2"), "discord"),
            new PasswordService("YouTube", "https://accounts.google.com/ServiceLogin?service=youtube", R.drawable.ic_youtube, Color.parseColor("#ed1d24"), "youtube"),
            new PasswordService("Airbnb", "https://www.airbnb.com/login", R.drawable.ic_airbnb, Color.parseColor("#FF5A5F"), "airbnb"),
            new PasswordService("GitHub", "https://github.com/login", R.drawable.ic_github, Color.BLACK, "github"),
            new PasswordService("Facebook", "https://www.facebook.com/login", R.drawable.ic_facebook, Color.parseColor("#1877F2"), "facebook"),
            new PasswordService("Custom", null, R.drawable.ic_custom, Color.GRAY, "custom")
    );
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_add, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.quick_add_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new QuickAddAdapter(services));
        return view;
    }

    public static List<PasswordService> getKnownServices() {
        return services.subList(0, services.size() - 1); // exclude "Custom"
    }

}
