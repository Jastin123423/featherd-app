package com.unera.social.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.unera.social.R;
import com.unera.social.adapters.ReelsAdapter;
import com.unera.social.api.ApiClient;
import com.unera.social.models.Reel;

import java.util.ArrayList;
import java.util.List;

public class ReelsFragment extends Fragment {
    private RecyclerView rvReels;
    private ProgressBar pbLoading;
    private final List<Reel> reelsList = new ArrayList<>();
    private ReelsAdapter reelsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reels, container, false);

        rvReels = view.findViewById(R.id.rv_reels);
        pbLoading = view.findViewById(R.id.pb_reels_loading);

        rvReels.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvReels);

        reelsAdapter = new ReelsAdapter(reelsList);
        rvReels.setAdapter(reelsAdapter);

        loadReels();

        return view;
    }

    private void loadReels() {
        if (pbLoading != null) pbLoading.setVisibility(View.VISIBLE);
        ApiClient.getInstance().fetchReels(new ApiClient.ApiCallback<List<Reel>>() {
            @Override
            public void onSuccess(List<Reel> reels) {
                if (!isAdded() || getContext() == null) return;
                if (pbLoading != null) pbLoading.setVisibility(View.GONE);
                reelsList.clear();
                reelsList.addAll(reels);
                if (reelsAdapter != null) reelsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception error) {
                if (!isAdded() || getContext() == null) return;
                if (pbLoading != null) pbLoading.setVisibility(View.GONE);
            }
        });
    }
}
