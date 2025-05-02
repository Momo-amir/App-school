package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Button;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.PasswordDbHelper;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        PasswordDbHelper dbHelper = new PasswordDbHelper(getContext());

        binding.buttonAddPassword.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main_nav);
            navController.navigate(R.id.action_nav_home_to_quickAddFragment);
        });

        updatePasswordList(dbHelper);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updatePasswordList(PasswordDbHelper dbHelper) {
        Cursor cursor = dbHelper.getAllPasswords();
        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            String website = cursor.getString(cursor.getColumnIndexOrThrow(PasswordDbHelper.COLUMN_WEBSITE));
            String username = cursor.getString(cursor.getColumnIndexOrThrow(PasswordDbHelper.COLUMN_USERNAME));
            builder.append(website).append(" - ").append(username).append("\n");
        }
        cursor.close();
        binding.textPasswordsPlaceholder.setText(builder.length() > 0 ? builder.toString() : "No passwords added yet");
    }
}