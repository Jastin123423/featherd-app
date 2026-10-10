package com.unera.social;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.unera.social.fragments.EventsFragment;
import com.unera.social.fragments.FeedFragment;
import com.unera.social.fragments.MarketplaceFragment;
import com.unera.social.fragments.ProfileFragment;
import com.unera.social.fragments.ReelsFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private int currentTabId = R.id.nav_feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            configureSystemBars();
            setupHeaderActions();
            setupBottomNavigation();

            if (savedInstanceState == null) {
                switchFragment(new FeedFragment());
            }
        } catch (Exception e) {
            android.util.Log.e("MainActivity", "Safe startup initialization error: ", e);
        }
    }

    private void configureSystemBars() {
        Window window = getWindow();
        if (window == null) return;

        try {
            int darkNavy = Color.parseColor("#050B18");
            window.setStatusBarColor(darkNavy);
            window.setNavigationBarColor(darkNavy);

            WindowInsetsControllerCompat controller =
                    new WindowInsetsControllerCompat(window, window.getDecorView());

            // Light text/icons on dark status bar (time, battery, wifi, network)
            controller.setAppearanceLightStatusBars(false);
            controller.setAppearanceLightNavigationBars(false);
            controller.show(WindowInsetsCompat.Type.systemBars());
        } catch (Exception ignored) {}
    }

    private void setupHeaderActions() {
        ImageView btnSearch = findViewById(R.id.btn_header_search);
        ImageView btnNotifications = findViewById(R.id.btn_header_notifications);
        ImageView btnMessages = findViewById(R.id.btn_header_messages);

        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> {
                bottomNavigation.setSelectedItemId(R.id.nav_marketplace);
            });
        }

        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v ->
                    Toast.makeText(this, "You have 3 new notifications", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnMessages != null) {
            btnMessages.setOnClickListener(v ->
                    Toast.makeText(this, "Feathered Chat & Messages", Toast.LENGTH_SHORT).show()
            );
        }
    }

    private void setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        if (bottomNavigation == null) return;

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == currentTabId) return true;

            currentTabId = itemId;
            if (itemId == R.id.nav_feed) {
                switchFragment(new FeedFragment());
                return true;
            } else if (itemId == R.id.nav_reels) {
                switchFragment(new ReelsFragment());
                return true;
            } else if (itemId == R.id.nav_marketplace) {
                switchFragment(new MarketplaceFragment());
                return true;
            } else if (itemId == R.id.nav_events) {
                switchFragment(new EventsFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                switchFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (currentTabId != R.id.nav_feed) {
            bottomNavigation.setSelectedItemId(R.id.nav_feed);
        } else {
            super.onBackPressed();
        }
    }
}
