package com.unera.social.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.unera.social.R;
import com.unera.social.adapters.ProductAdapter;
import com.unera.social.api.ApiClient;
import com.unera.social.models.Product;

import java.util.ArrayList;
import java.util.List;

public class MarketplaceFragment extends Fragment {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvProducts;
    private ProgressBar pbLoading;
    private EditText etSearch;

    private final List<Product> allProducts = new ArrayList<>();
    private final List<Product> displayedProducts = new ArrayList<>();
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marketplace, container, false);

        swipeRefresh = view.findViewById(R.id.swipe_refresh_marketplace);
        rvProducts = view.findViewById(R.id.rv_products);
        pbLoading = view.findViewById(R.id.pb_marketplace_loading);
        etSearch = view.findViewById(R.id.et_search_marketplace);

        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ProductAdapter(displayedProducts);
        rvProducts.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::loadProducts);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadProducts();

        return view;
    }

    private void filter(String query) {
        displayedProducts.clear();
        if (query.trim().isEmpty()) {
            displayedProducts.addAll(allProducts);
        } else {
            String lower = query.toLowerCase();
            for (Product p : allProducts) {
                if (p.title.toLowerCase().contains(lower) || p.location.toLowerCase().contains(lower)) {
                    displayedProducts.add(p);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadProducts() {
        pbLoading.setVisibility(View.VISIBLE);
        ApiClient.getInstance().fetchProducts(new ApiClient.ApiCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> products) {
                pbLoading.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                allProducts.clear();
                allProducts.addAll(products);
                filter(etSearch.getText().toString());
            }

            @Override
            public void onError(Exception error) {
                pbLoading.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }
        });
    }
}
