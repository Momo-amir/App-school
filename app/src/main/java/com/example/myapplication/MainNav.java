package com.example.myapplication;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.fragment.NavHostFragment;
import com.example.myapplication.databinding.ActivityMainNavBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainNav extends AppCompatActivity {

    private ActivityMainNavBinding binding;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar); // Uses toolbar from XML layout

        BottomNavigationView bottomNav = binding.bottomNav;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main_nav);
        NavController navController = navHostFragment.getNavController();

        // Top-level destinations for action bar
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_quick_add
                // TODO ADD FOR CONTACT_MANAGER
        ).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNav, navController);

        // Custom bottom nav item clicks
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                navController.navigate(R.id.nav_home);
                return true;
            } else if (id == R.id.nav_quick_add) {
                navController.navigate(R.id.quickAddFragment);
                return true;
            } else if (id == R.id.nav_back) {
                if (!navController.popBackStack()) {
                    finish(); // fallback if no back stack
                }
                return true;
            } else if (id == R.id.nav_forward) {
                // TODO  Future Momo: implement forward navigation
                return true;
            } else if (id == R.id.nav_more) {
                // TODO Future Momo: open a bottom sheet or popup menu
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_nav);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}
