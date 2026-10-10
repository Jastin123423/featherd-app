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
            android.util.Log.e("MainActivity", "Safe startup initialization: ", e);
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
                break;
            case MESSAGE:
                fragment = new MessagesFragment();
                break;
            case PROFILE:
                fragment = new ProfileFragment();
                break;
            case EVENTS:
                fragment = new EventsFragment();
                break;
            case REELS:
                fragment = new ReelsFragment();
                break;
            case HOME:
            default:
                fragment = new FeedFragment();
                break;
        }

        highlightNav(tab);

        try {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            android.util.Log.e("MainActivity", "Error switching tab: ", e);
        }
    }

    private void highlightNav(Tab tab) {
        int activeColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int inactiveColor = ContextCompat.getColor(this, R.color.text_secondary);

        // Home
        if (ivNavHome != null) ivNavHome.setColorFilter(tab == Tab.HOME ? activeColor : inactiveColor);
        if (tvNavHome != null) tvNavHome.setTextColor(tab == Tab.HOME ? activeColor : inactiveColor);
        if (dotNavHome != null) dotNavHome.setVisibility(tab == Tab.HOME ? View.VISIBLE : View.INVISIBLE);

        // Market
        if (ivNavMarket != null) ivNavMarket.setColorFilter(tab == Tab.MARKET ? activeColor : inactiveColor);
        if (tvNavMarket != null) tvNavMarket.setTextColor(tab == Tab.MARKET ? activeColor : inactiveColor);
        if (dotNavMarket != null) dotNavMarket.setVisibility(tab == Tab.MARKET ? View.VISIBLE : View.INVISIBLE);

        // Message
        if (ivNavMessage != null) ivNavMessage.setColorFilter(tab == Tab.MESSAGE ? activeColor : inactiveColor);
        if (tvNavMessage != null) tvNavMessage.setTextColor(tab == Tab.MESSAGE ? activeColor : inactiveColor);
        if (dotNavMessage != null) dotNavMessage.setVisibility(tab == Tab.MESSAGE ? View.VISIBLE : View.INVISIBLE);

        // Menu
        if (ivNavMenu != null) ivNavMenu.setColorFilter(inactiveColor);
        if (tvNavMenu != null) tvNavMenu.setTextColor(inactiveColor);
        if (dotNavMenu != null) dotNavMenu.setVisibility(View.INVISIBLE);
    }

    private void showCreatePostModal() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_create_post);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        EditText etContent = dialog.findViewById(R.id.et_new_post_content);
        View btnCancel = dialog.findViewById(R.id.btn_cancel_post);
        View btnSubmit = dialog.findViewById(R.id.btn_submit_post);

        if (btnCancel != null) btnCancel.setOnClickListener(v -> dialog.dismiss());
        if (btnSubmit != null) {
            btnSubmit.setOnClickListener(v -> {
                String content = etContent != null ? etContent.getText().toString().trim() : "";
                if (content.isEmpty()) {
                    Toast.makeText(this, "Please write something to post", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "Post published successfully!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                switchTab(Tab.HOME);
            });
        }

        dialog.show();
    }

    private void showFullMenuSheet() {
        BottomSheetDialog menuSheet = new BottomSheetDialog(this);
        menuSheet.setContentView(R.layout.bottom_sheet_menu);

        View btnClose = menuSheet.findViewById(R.id.btn_close_menu);
        if (btnClose != null) btnClose.setOnClickListener(v -> menuSheet.dismiss());

        View itemProfile = menuSheet.findViewById(R.id.menu_profile_card);
        if (itemProfile != null) {
            itemProfile.setOnClickListener(v -> {
                menuSheet.dismiss();
                switchTab(Tab.PROFILE);
            });
        }

        View itemMarket = menuSheet.findViewById(R.id.menu_item_marketpoint);
        if (itemMarket != null) {
            itemMarket.setOnClickListener(v -> {
                menuSheet.dismiss();
                switchTab(Tab.MARKET);
            });
        }

        View itemEvents = menuSheet.findViewById(R.id.menu_item_events);
        if (itemEvents != null) {
            itemEvents.setOnClickListener(v -> {
                menuSheet.dismiss();
                switchTab(Tab.EVENTS);
            });
        }

        View itemVideos = menuSheet.findViewById(R.id.menu_item_videos);
        if (itemVideos != null) {
            itemVideos.setOnClickListener(v -> {
                menuSheet.dismiss();
                switchTab(Tab.REELS);
            });
        }

        View itemSaved = menuSheet.findViewById(R.id.menu_item_saved);
        if (itemSaved != null) {
            itemSaved.setOnClickListener(v -> {
                menuSheet.dismiss();
                Toast.makeText(this, "Saved Posts", Toast.LENGTH_SHORT).show();
            });
        }

        View linkPrivacy = menuSheet.findViewById(R.id.menu_link_privacy);
        if (linkPrivacy != null) {
            linkPrivacy.setOnClickListener(v -> {
                menuSheet.dismiss();
                Toast.makeText(this, "Privacy Policy: Feathered adheres to industry-leading data protection.", Toast.LENGTH_LONG).show();
            });
        }

        View linkTerms = menuSheet.findViewById(R.id.menu_link_terms);
        if (linkTerms != null) {
            linkTerms.setOnClickListener(v -> {
                menuSheet.dismiss();
                Toast.makeText(this, "Terms of Service: Connect, Create, Move with Feathered Social.", Toast.LENGTH_LONG).show();
            });
        }

        View linkLogout = menuSheet.findViewById(R.id.menu_link_logout);
        if (linkLogout != null) {
            linkLogout.setOnClickListener(v -> {
                menuSheet.dismiss();
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            });
        }

        menuSheet.show();
    }

    private void showSearchOverlay() {
        Dialog searchDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        searchDialog.setContentView(R.layout.dialog_search_overlay);

        View btnClose = searchDialog.findViewById(R.id.btn_close_search);
        if (btnClose != null) btnClose.setOnClickListener(v -> searchDialog.dismiss());
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
