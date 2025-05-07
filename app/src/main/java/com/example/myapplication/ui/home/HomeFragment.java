package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Button;
    import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.PasswordService;
import com.example.myapplication.QuickAddAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.PasswordDbHelper;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        PasswordDbHelper dbHelper = new PasswordDbHelper(getContext());

        binding.buttonAddPassword.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_home_to_quickAddFragment);
        });

        binding.buttonContactManager.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_nav_home_to_contactManagerFragment);
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
        java.util.List<PasswordService> recentEntries = new java.util.ArrayList<>();
        int maxItems = 6;

        java.util.List<PasswordService> knownServices = com.example.myapplication.ui.quickadd.QuickAddFragment.getKnownServices();

        while (cursor.moveToNext() && recentEntries.size() < maxItems) {
            String website = cursor.getString(cursor.getColumnIndexOrThrow(PasswordDbHelper.COLUMN_WEBSITE));
            String matchedName = website;
            int icon = R.drawable.ic_custom;
            int color = ContextCompat.getColor(requireContext(), R.color.grey_200);
            for (PasswordService known : knownServices) {
                if (website.equalsIgnoreCase(known.getUrl())) {
                    matchedName = known.getName();
                    icon = known.getIconResId();
                    color = known.getBackgroundColor();
                    break;
                }
            }

            recentEntries.add(new PasswordService(
                    matchedName,
                    website,
                    icon,
                    color,
                    website.hashCode() + ""
            ));
        }
        cursor.close();

        RecyclerView recyclerView = binding.recentPasswordsRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new QuickAddAdapter(recentEntries));
        androidx.appcompat.widget.Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.pop_color));        }
    }
}