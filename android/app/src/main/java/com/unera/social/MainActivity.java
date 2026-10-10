package com.unera.social;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.unera.social.fragments.EventsFragment;
import com.unera.social.fragments.FeedFragment;
import com.unera.social.fragments.MarketplaceFragment;
import com.unera.social.fragments.MessagesFragment;
import com.unera.social.fragments.ProfileFragment;
import com.unera.social.fragments.ReelsFragment;
import com.unera.social.models.Post;
import com.unera.social.utils.ImageLoader;

public class MainActivity extends AppCompatActivity {
    private enum Tab { HOME, MARKET, MESSAGE, PROFILE, EVENTS, REELS }
    private Tab currentTab = Tab.HOME;

    // Bottom Navigation views
    private View navBtnHome, navBtnMarket, navBtnPost, navBtnMessage, navBtnMenu;
    private ImageView ivNavHome, ivNavMarket, ivNavMessage, ivNavMenu;
    private TextView tvNavHome, tvNavMarket, tvNavMessage, tvNavMenu;
    private View dotNavHome, dotNavMarket, dotNavMessage, dotNavMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            configureSystemBars();
            setupHeader();
            setupBottomNavigation();

            if (savedInstanceState == null) {
                switchTab(Tab.HOME);
            }
        } catch (Exception e) {
            android.util.Log.e("MainActivity", "Safe startup error: ", e);
        }
    }

    private void configureSystemBars() {
        Window window = getWindow();
        if (window == null) return;
        try {
            int darkNavy = Color.parseColor("#0B1120");
            window.setStatusBarColor(darkNavy);
            window.setNavigationBarColor(darkNavy);

            WindowInsetsControllerCompat controller =
                    new WindowInsetsControllerCompat(window, window.getDecorView());
            controller.setAppearanceLightStatusBars(false);
            controller.setAppearanceLightNavigationBars(false);
            controller.show(WindowInsetsCompat.Type.systemBars());
        } catch (Exception ignored) {}
    }

    private void setupHeader() {
        View btnBrandHome = findViewById(R.id.btn_brand_home);
        View btnMarket = findViewById(R.id.btn_header_marketplace);
        View btnSearch = findViewById(R.id.btn_header_search);
        View btnNotifs = findViewById(R.id.btn_header_notifications);
        View btnProfile = findViewById(R.id.btn_header_profile);
        ImageView ivAvatar = findViewById(R.id.iv_header_avatar);

        if (ivAvatar != null) {
            ImageLoader.getInstance().load("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150", ivAvatar);
        }

        if (btnBrandHome != null) {
            btnBrandHome.setOnClickListener(v -> switchTab(Tab.HOME));
        }

        if (btnMarket != null) {
            btnMarket.setOnClickListener(v -> switchTab(Tab.MARKET));
        }

        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> showSearchOverlay());
        }

        if (btnNotifs != null) {
            btnNotifs.setOnClickListener(v -> showNotificationsSheet());
        }

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> switchTab(Tab.PROFILE));
        }
    }

    private void setupBottomNavigation() {
        navBtnHome = findViewById(R.id.nav_btn_home);
        navBtnMarket = findViewById(R.id.nav_btn_market);
        navBtnPost = findViewById(R.id.nav_btn_post);
        navBtnMessage = findViewById(R.id.nav_btn_message);
        navBtnMenu = findViewById(R.id.nav_btn_menu);

        ivNavHome = findViewById(R.id.iv_nav_home);
        ivNavMarket = findViewById(R.id.iv_nav_market);
        ivNavMessage = findViewById(R.id.iv_nav_message);
        ivNavMenu = findViewById(R.id.iv_nav_menu);

        tvNavHome = findViewById(R.id.tv_nav_home);
        tvNavMarket = findViewById(R.id.tv_nav_market);
        tvNavMessage = findViewById(R.id.tv_nav_message);
        tvNavMenu = findViewById(R.id.tv_nav_menu);

        dotNavHome = findViewById(R.id.dot_nav_home);
        dotNavMarket = findViewById(R.id.dot_nav_market);
        dotNavMessage = findViewById(R.id.dot_nav_message);
        dotNavMenu = findViewById(R.id.dot_nav_menu);

        if (navBtnHome != null) navBtnHome.setOnClickListener(v -> switchTab(Tab.HOME));
        if (navBtnMarket != null) navBtnMarket.setOnClickListener(v -> switchTab(Tab.MARKET));
        if (navBtnPost != null) navBtnPost.setOnClickListener(v -> showCreatePostModal());
        if (navBtnMessage != null) navBtnMessage.setOnClickListener(v -> switchTab(Tab.MESSAGE));
        if (navBtnMenu != null) navBtnMenu.setOnClickListener(v -> showFullMenuSheet());
    }

    private void switchTab(Tab tab) {
        currentTab = tab;
        Fragment fragment;

        switch (tab) {
            case MARKET:
                fragment = new MarketplaceFragment();
                highlightNav(Tab.MARKET);
                break;
            case MESSAGE:
                fragment = new MessagesFragment();
                highlightNav(Tab.MESSAGE);
                break;
            case PROFILE:
                fragment = new ProfileFragment();
                highlightNav(Tab.HOME); // keep home tab unselected
                break;
            case EVENTS:
                fragment = new EventsFragment();
                highlightNav(Tab.HOME);
                break;
            case REELS:
                fragment = new ReelsFragment();
                highlightNav(Tab.HOME);
                break;
            case HOME:
            default:
                fragment = new FeedFragment();
                highlightNav(Tab.HOME);
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void highlightNav(Tab tab) {
        int activeColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int inactiveColor = ContextCompat.getColor(this, R.color.text_secondary);

        // Home
        ivNavHome.setColorFilter(tab == Tab.HOME ? activeColor : inactiveColor);
        tvNavHome.setTextColor(tab == Tab.HOME ? activeColor : inactiveColor);
        dotNavHome.setVisibility(tab == Tab.HOME ? View.VISIBLE : View.INVISIBLE);

        // Market
        ivNavMarket.setColorFilter(tab == Tab.MARKET ? activeColor : inactiveColor);
        tvNavMarket.setTextColor(tab == Tab.MARKET ? activeColor : inactiveColor);
        dotNavMarket.setVisibility(tab == Tab.MARKET ? View.VISIBLE : View.INVISIBLE);

        // Message
        ivNavMessage.setColorFilter(tab == Tab.MESSAGE ? activeColor : inactiveColor);
        tvNavMessage.setTextColor(tab == Tab.MESSAGE ? activeColor : inactiveColor);
        dotNavMessage.setVisibility(tab == Tab.MESSAGE ? View.VISIBLE : View.INVISIBLE);

        // Menu
        ivNavMenu.setColorFilter(inactiveColor);
        tvNavMenu.setTextColor(inactiveColor);
        dotNavMenu.setVisibility(View.INVISIBLE);
    }

    private void showCreatePostModal() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_create_post);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        EditText etContent = dialog.findViewById(R.id.et_new_post_content);
        EditText etImageUrl = dialog.findViewById(R.id.et_new_post_image_url);

        dialog.findViewById(R.id.btn_cancel_post).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btn_submit_post).setOnClickListener(v -> {
            String content = etContent.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "Please write something to post", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Post published successfully!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            switchTab(Tab.HOME);
        });

        dialog.show();
    }

    private void showFullMenuSheet() {
        BottomSheetDialog menuSheet = new BottomSheetDialog(this);
        menuSheet.setContentView(R.layout.bottom_sheet_menu);

        menuSheet.findViewById(R.id.btn_close_menu).setOnClickListener(v -> menuSheet.dismiss());

        menuSheet.findViewById(R.id.menu_profile_card).setOnClickListener(v -> {
            menuSheet.dismiss();
            switchTab(Tab.PROFILE);
        });

        menuSheet.findViewById(R.id.menu_item_marketpoint).setOnClickListener(v -> {
            menuSheet.dismiss();
            switchTab(Tab.MARKET);
        });

        menuSheet.findViewById(R.id.menu_item_events).setOnClickListener(v -> {
            menuSheet.dismiss();
            switchTab(Tab.EVENTS);
        });

        menuSheet.findViewById(R.id.menu_item_videos).setOnClickListener(v -> {
            menuSheet.dismiss();
            switchTab(Tab.REELS);
        });

        menuSheet.findViewById(R.id.menu_item_saved).setOnClickListener(v -> {
            menuSheet.dismiss();
            Toast.makeText(this, "Saved Posts", Toast.LENGTH_SHORT).show();
        });

        menuSheet.findViewById(R.id.menu_link_privacy).setOnClickListener(v -> {
            menuSheet.dismiss();
            Toast.makeText(this, "Privacy Policy: Feathered adheres to industry-leading data protection.", Toast.LENGTH_LONG).show();
        });

        menuSheet.findViewById(R.id.menu_link_terms).setOnClickListener(v -> {
            menuSheet.dismiss();
            Toast.makeText(this, "Terms of Service: Connect, Create, Move with Feathered Social.", Toast.LENGTH_LONG).show();
        });

        menuSheet.findViewById(R.id.menu_link_logout).setOnClickListener(v -> {
            menuSheet.dismiss();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        });

        menuSheet.show();
    }

    private void showSearchOverlay() {
        Dialog searchDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        searchDialog.setContentView(R.layout.dialog_search_overlay);

        searchDialog.findViewById(R.id.btn_close_search).setOnClickListener(v -> searchDialog.dismiss());
        searchDialog.show();
    }

    private void showNotificationsSheet() {
        Toast.makeText(this, "You have 5 new notifications", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (currentTab != Tab.HOME) {
            switchTab(Tab.HOME);
        } else {
            super.onBackPressed();
        }
    }
}
